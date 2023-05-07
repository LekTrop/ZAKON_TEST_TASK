package ua.zhytariuk.service;

import java.util.List;

/**
 * Interface to provide functionality for calculating words similarity
 *
 * @author ozhytary
 */
public interface SimilarityCalculator {

    /**
     * Calculates the similarity score between two lists of strings
     *
     * @param similarTo   a list of strings to compare
     * @param similarFrom a list of strings to compare against
     * @return the similarity score between the two lists of strings
     */
    Double calculateSimilarity(final List<String> similarTo, final List<String> similarFrom);
}