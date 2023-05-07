package ua.zhytariuk.service;


import ua.zhytariuk.model.Question;

import java.util.List;

/**
 * Represent interface that provide operations related to {@link Question} entity
 *
 * @author (ozhytary)
 */
public interface QuestionService {
    List<Question> findTopLongestQuestions(final Integer number);

    List<Question> findMostSimilarOrCreateIfNotExist(final String question, final Integer number);

    Question save(final String question);
}
