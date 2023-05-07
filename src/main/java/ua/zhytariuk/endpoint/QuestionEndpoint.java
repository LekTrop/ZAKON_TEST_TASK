package ua.zhytariuk.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;
import ua.zhytariuk.model.Question;
import ua.zhytariuk.model.mapper.QuestionMapper;
import ua.zhytariuk.service.QuestionService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Endpoint that exposes an API to interact with {@link Question}
 *
 * @author (ozhytary)
 */
@RestController
@RequestMapping("api/v1/questions")
public class QuestionEndpoint {

    private final QuestionService questionService;
    private final QuestionMapper questionMapper;

    public QuestionEndpoint(final QuestionService questionService,
                            final QuestionMapper questionMapper) {
        this.questionService = questionService;
        this.questionMapper = questionMapper;
    }

    /**
     * Find the top {@param number} longest questions
     *
     * @param number of the longest questions
     * @return {@link List<QuestionDto>}
     */
    @GetMapping("/longest")
    @Operation(summary = "Get top longest questions")
    public List<QuestionDto> findTopLongestQuestions(final @RequestParam("number") Integer number) {
        return questionService.findTopLongestQuestions(number)
                              .stream()
                              .map(questionMapper::toQuestionDto)
                              .collect(Collectors.toList());
    }

    /**
     * Find most similar questions or create new if they do not exist
     *
     * @param question to search most similar
     * @param number   of questions to return
     * @return {@link List<QuestionDto>} most similar questions
     */
    @PostMapping("/similar")
    @Operation(summary = "Find most similar or create new question")
    public List<QuestionDto> findMostSimilarOrCreateIfNotExist(final @Parameter(description = "Requested question to search most similar") @RequestParam("question") String question,
                                                               final @Parameter(description = "Number of similar question to return") @RequestParam("number") Integer number) {
        return questionService.findMostSimilarOrCreateIfNotExist(question, number)
                              .stream()
                              .map(questionMapper::toQuestionDto)
                              .collect(Collectors.toList());
    }
}
