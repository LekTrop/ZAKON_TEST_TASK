package ua.zhytariuk.model.mapper;

import org.springframework.stereotype.Component;
import ua.zhytariuk.endpoint.QuestionDto;
import ua.zhytariuk.model.Question;

/**
 * Class that is used for mapping questions
 *
 * @author (ozhytary)
 */
@Component
public class QuestionMapper {
    public QuestionDto toQuestionDto(final Question question) {
        if (question == null) {
            return null;
        }

        return new QuestionDto(question.getText());
    }
}
