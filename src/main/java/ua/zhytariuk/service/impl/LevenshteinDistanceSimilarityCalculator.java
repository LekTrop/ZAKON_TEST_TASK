package ua.zhytariuk.service.impl;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;
import ua.zhytariuk.config.ElasticSearchProperties;
import ua.zhytariuk.service.SimilarityCalculator;

import java.util.List;

/**
 * Provide Levenshtein Distance algo for calculating similarity
 *
 * @author (ozhytary)
 */
@Component
public class LevenshteinDistanceSimilarityCalculator implements SimilarityCalculator {

    private static final Integer MIN_WORD_LENGTH = 3;

    /**
     * Calculates the similarity score between two lists of strings based on the following criteria:
     * 1. Only strings longer than 3 characters are considered for comparison.
     * 2. For each string in `similarTo`, find the most similar string in `similarFrom` using Levenshtein distance.
     * 3. If the Levenshtein distance between two strings is less than length of the string divided by 3,
     * then the two strings are considered similar.
     *
     * @param similarTo   a list of strings to compare
     * @param similarFrom a list of strings to compare against
     * @return the similarity score between the two lists of strings
     */
    @Override
    public Double calculateSimilarity(final List<String> similarTo, final List<String> similarFrom) {
        int countOfMatchedWords = 0;

        for (final String fromWord : similarFrom) {
            for (final String toWord : similarTo) {
                final int levenshteinDistance = LevenshteinDistance.getDefaultInstance()
                                                                   .apply(fromWord, toWord);

                if (levenshteinDistance == 0 || levenshteinDistance < fromWord.length() / MIN_WORD_LENGTH) {
                    countOfMatchedWords++;
                    break;
                }
            }
        }

        return countOfMatchedWords > 0 ? (double) countOfMatchedWords / similarTo.size() : -1D;
    }
}
