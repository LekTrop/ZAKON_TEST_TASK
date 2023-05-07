package ua.zhytariuk.service.impl;

import co.elastic.clients.elasticsearch._types.ScriptSortType;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOptionsBuilders;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ua.zhytariuk.config.ElasticSearchProperties;
import ua.zhytariuk.endpoint.QuestionDto;
import ua.zhytariuk.model.Question;
import ua.zhytariuk.model.QuestionPairComparator;
import ua.zhytariuk.repository.QuestionRepository;
import ua.zhytariuk.service.QuestionService;
import ua.zhytariuk.service.SimilarityCalculator;
import ua.zhytariuk.utils.QuestionSearchUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static co.elastic.clients.elasticsearch._types.SortOrder.Desc;
import static org.springframework.data.elasticsearch.client.elc.QueryBuilders.matchAllQueryAsQuery;
import static ua.zhytariuk.model.Question.TEXT_FIELD;
import static ua.zhytariuk.model.Question.TEXT_KEYWORD_SUFFIX;

/**
 * Service that expose base functionality for interacting with {@link Question} data
 *
 * @author (ozhytary)
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    private static final String SCRIPT_DOC_QUESTION_KEYWORD_VALUE_LENGTH = "doc['text.keyword'].value.length()";
    private static final Integer MINIMUM_DOC_FREQ = 1;
    private static final Integer MIN_TERM_FREQ = 1;
    private static final Integer PAGE_SIZE = 100;

    private final QuestionRepository questionRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final QuestionSearchUtils questionSearchUtils;
    private final SimilarityCalculator similarityCalculator;
    private final ExecutorService executorService;
    private final ElasticSearchProperties esProperties;

    public QuestionServiceImpl(final ElasticsearchOperations elasticsearchOperations,
                               final QuestionRepository questionRepository,
                               final QuestionSearchUtils questionSearchUtils,
                               final SimilarityCalculator similarityCalculator,
                               final ExecutorService executorService,
                               final ElasticSearchProperties esProperties) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.questionRepository = questionRepository;
        this.questionSearchUtils = questionSearchUtils;
        this.similarityCalculator = similarityCalculator;
        this.executorService = executorService;
        this.esProperties = esProperties;
    }

    /**
     * Find the top {@param number} longest questions
     *
     * @param number of the longest questions
     * @return {@link List<QuestionDto> Questions}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Question> findTopLongestQuestions(final Integer number) {
        if (number == null || number < 0) {
            throw new IllegalArgumentException("Number can`t be null or less then 0");
        }

        final SortOptions sortOption = SortOptionsBuilders.script()
                                                          .script(scriptBuilder -> scriptBuilder.inline(in -> in.source(SCRIPT_DOC_QUESTION_KEYWORD_VALUE_LENGTH)))
                                                          .order(Desc)
                                                          .type(ScriptSortType.Number)
                                                          .build()
                                                          ._toSortOptions();

        final Query query = NativeQuery.builder()
                                       .withQuery(matchAllQueryAsQuery())
                                       .withSort(sortOption)
                                       .withPageable(PageRequest.ofSize(number))
                                       .build();

        final SearchHits<Question> search = elasticsearchOperations.search(query, Question.class);

        return search.getSearchHits()
                     .stream()
                     .map(SearchHit::getContent)
                     .collect(Collectors.toList());
    }

    /**
     * Find most similar questions or create new if they do not exist
     *
     * @param requestQuestion to search most similar
     * @param number          of questions to return
     * @return {@link List<Question>} most similar questions
     */
    @Override
    @Transactional
    public List<Question> findMostSimilarOrCreateIfNotExist(final String requestQuestion,
                                                            final Integer number) {
        if (number == null || number < 1) {
            throw new IllegalArgumentException("Number can`t be null or less then 0");
        }

        final var prefixQuery = NativeQuery.builder()
                                           .withQuery(q -> q.prefix(prefix -> prefix.field(TEXT_FIELD + "." + TEXT_KEYWORD_SUFFIX)
                                                                                    .value(questionSearchUtils.getFirstWord(requestQuestion))))
                                           .getQuery();

        final var mostLikeThisQuery = NativeQuery.builder()
                                                 .withQuery(q -> q.moreLikeThis(mlt -> mlt.fields(TEXT_FIELD)
                                                                                          .like(like -> like.text(requestQuestion))
                                                                                          .minDocFreq(MINIMUM_DOC_FREQ)
                                                                                          .minTermFreq(MIN_TERM_FREQ)))
                                                 .getQuery();

        final var boolQuery = NativeQuery.builder()
                                         .withQuery(q -> q.bool(bool -> bool.must(List.of(prefixQuery, mostLikeThisQuery))))
                                         .withPageable(Pageable.ofSize(PAGE_SIZE))
                                         .build();

        final Set<Question> searchedQuestions = elasticsearchOperations.search(boolQuery, Question.class)
                                                                       .stream()
                                                                       .map(SearchHit::getContent)
                                                                       .collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(searchedQuestions)) {
            return Collections.singletonList(save(requestQuestion));
        }

        final Set<Pair<Question, Double>> questions = new TreeSet<>(new QuestionPairComparator());
        final List<Callable<Pair<Question, Double>>> callables = new ArrayList<>();

        final var requestedWords =
                questionSearchUtils.getWordsThatLargerThan(esProperties.getMinSimilarityWordLength(), requestQuestion);

        searchedQuestions.forEach(question -> callables.add(() -> {
            final var questionWords =
                    questionSearchUtils.getWordsThatLargerThan(esProperties.getMinSimilarityWordLength(), question.getText());
            return Pair.of(question, similarityCalculator.calculateSimilarity(questionWords, requestedWords));
        }));

        try {
            final var futures = executorService.invokeAll(callables);

            for (final var pairFuture : futures) {
                questions.add(pairFuture.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return questions.stream()
                        .filter(questionDoublePair -> questionDoublePair.getRight() > 0)
                        .map(Pair::getLeft)
                        .limit(number)
                        .collect(Collectors.toList());
    }

    /**
     * Save {@link Question} to the db
     *
     * @param question to save
     * @return saved entity
     */
    @Transactional
    @Override
    public Question save(final String question) {
        if (StringUtils.isBlank(question)) {
            throw new IllegalArgumentException("Question can`t be blank");
        }

        return questionRepository.save(new Question(question));
    }
}
