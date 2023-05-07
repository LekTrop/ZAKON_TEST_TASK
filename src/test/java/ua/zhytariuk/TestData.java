package ua.zhytariuk;

import ua.zhytariuk.endpoint.QuestionDto;
import ua.zhytariuk.model.Question;

/**
 * @author (ozhytary)
 */
public class TestData {
    public static final Integer MIN_SIMILARITY_WORD_LENGTH = 3;

    public static final String TEXT_1 = "text with number test text first";
    public static final String TEXT_2 = "text with number test text second";
    public static final String TEXT_3 = "text with number test text third";

    public static final Question QUESTION_1 = new Question(TEXT_1);
    public static final Question QUESTION_2 = new Question(TEXT_2);
    public static final Question QUESTION_3 = new Question(TEXT_3);

    public static final QuestionDto QUESTION_DTO_1 = new QuestionDto(TEXT_1);
    public static final QuestionDto QUESTION_DTO_2 = new QuestionDto(TEXT_2);
    public static final QuestionDto QUESTION_DTO_3 = new QuestionDto(TEXT_3);
}
