package ua.zhytariuk.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import ua.zhytariuk.config.ElasticSearchProperties;
import ua.zhytariuk.model.Question;
import ua.zhytariuk.repository.QuestionRepository;
import ua.zhytariuk.service.SimilarityCalculator;
import ua.zhytariuk.utils.QuestionSearchUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;
import static ua.zhytariuk.TestData.*;

/**
 * @author (ozhytary)
 */
@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @InjectMocks
    private QuestionServiceImpl unit;
    @Mock
    private QuestionRepository mockQuestionRepository;
    @Mock
    private ElasticsearchOperations mockElasticsearchOperations;
    @Mock
    private QuestionSearchUtils mockQuestionSearchUtils;
    @Mock
    private SimilarityCalculator mockSimilarityCalculator;
    @Mock
    private ExecutorService mockExecutorService;
    @Mock
    private ElasticSearchProperties mockEsProperties;

    @Test
    void findTopLongestQuestions() {
        //GIVEN
        final int number = 2;

        final SearchHits<Question> searchHits = mock(SearchHits.class);
        final SearchHit<Question> searchHit1 = mock(SearchHit.class);
        final SearchHit<Question> searchHit2 = mock(SearchHit.class);

        when(mockElasticsearchOperations.search(any(Query.class), eq(Question.class))).thenReturn(searchHits);
        when(searchHits.getSearchHits()).thenReturn(List.of(searchHit1, searchHit2));
        when(searchHit1.getContent()).thenReturn(QUESTION_1);
        when(searchHit2.getContent()).thenReturn(QUESTION_2);

        //WHEN
        final var result = unit.findTopLongestQuestions(number);

        //THEN
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals(result.size(), number),
                () -> Assertions.assertEquals(result, List.of(QUESTION_1, QUESTION_2))
        );

        verify(mockElasticsearchOperations).search(any(Query.class), eq(Question.class));
        verify(searchHits).getSearchHits();
        verify(searchHit1).getContent();
        verify(searchHit2).getContent();
        verifyNoMoreInteractions(mockElasticsearchOperations, searchHit1, searchHit2, searchHits);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1})
    @NullSource
    void findTopLongestQuestions_number_less_then_0_or_null_exception(final Integer number){
        //WHEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> unit.findTopLongestQuestions(number));
    }

    @Test
    void save_successful() {
        //GIVEN
        when(mockQuestionRepository.save(QUESTION_1)).thenReturn(QUESTION_1);

        //WHEN
        final Question save = unit.save(TEXT_1);

        //THEN
        assertAll(
                () -> Assertions.assertNotNull(save),
                () -> Assertions.assertEquals(save.getText(), TEXT_1)
        );

        verify(mockQuestionRepository).save(QUESTION_1);
        verifyNoMoreInteractions(mockQuestionRepository);
    }

    @Test
    void save_empty_text_exception() {
        //WHEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> unit.save(null));
    }
}