package ua.zhytariuk.endpoint;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.zhytariuk.model.Question;
import ua.zhytariuk.model.mapper.QuestionMapper;
import ua.zhytariuk.service.QuestionService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ua.zhytariuk.TestData.*;

/**
 * Test class for {@link QuestionEndpoint}
 *
 * @author (ozhytary)
 */
@ExtendWith(MockitoExtension.class)
class QuestionEndpointTest {

    @InjectMocks
    private QuestionEndpoint unit;
    @Mock
    private QuestionService mockQuestionService;
    @Mock
    private QuestionMapper mockQuestionMapper;

    @Test
    void findTopLongestQuestions() {
        //GIVEN
        final Integer number = 2;
        when(mockQuestionService.findTopLongestQuestions(number)).thenReturn(List.of(QUESTION_1, QUESTION_2));
        when(mockQuestionMapper.toQuestionDto(QUESTION_1)).thenReturn(QUESTION_DTO_1);
        when(mockQuestionMapper.toQuestionDto(QUESTION_2)).thenReturn(QUESTION_DTO_2);

        //WHEN
        final List<QuestionDto> result = unit.findTopLongestQuestions(number);

        //THEN
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals(result.size(), number),
                () -> Assertions.assertEquals(result.get(0), QUESTION_DTO_1),
                () -> Assertions.assertEquals(result.get(1), QUESTION_DTO_2)
        );

        verify(mockQuestionService).findTopLongestQuestions(number);
        verify(mockQuestionMapper, times(result.size())).toQuestionDto(any(Question.class));
        verifyNoMoreInteractions(mockQuestionMapper, mockQuestionService);
    }

    @Test
    void findMostSimilarOrCreateIfNotExist() {
        //GIVEN
        final Integer number = 2;
        when(mockQuestionService.findMostSimilarOrCreateIfNotExist(TEXT_1, number))
                .thenReturn(List.of(QUESTION_1, QUESTION_2));
        when(mockQuestionMapper.toQuestionDto(QUESTION_1))
                .thenReturn(QUESTION_DTO_1);
        when(mockQuestionMapper.toQuestionDto(QUESTION_2))
                .thenReturn(QUESTION_DTO_2);
        //WHEN
        final List<QuestionDto> result = unit.findMostSimilarOrCreateIfNotExist(TEXT_1, number);

        //THEN
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals(result.size(), number),
                () -> Assertions.assertEquals(result.get(0), QUESTION_DTO_1),
                () -> Assertions.assertEquals(result.get(1), QUESTION_DTO_2)
        );

        verify(mockQuestionService).findMostSimilarOrCreateIfNotExist(TEXT_1, number);
        verify(mockQuestionMapper, times(result.size())).toQuestionDto(any(Question.class));
        verifyNoMoreInteractions(mockQuestionMapper, mockQuestionService);
    }
}