package ua.zhytariuk.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This utility class contains methods for searching and processing questions.
 *
 * @author (ozhytary)
 */
@Component
public class QuestionSearchUtils {
    /**
     * Get first word
     *
     * @param text where to find first word
     * @return first word
     */
    public String getFirstWord(final String text) {
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException("Text can`t be empty");
        }
        return text.substring(0, findEndIndexOfFirstWord(text));
    }

    /**
     * Returns a list of all the words in a given text string that are longer than a specified minimum length
     *
     * @param minLength the minimum length of words to include in the result list
     * @param text      the text string from which to extract the words
     * @return a list of words longer than the specified minimum length
     */
    public List<String> getWordsThatLargerThan(final int minLength, final String text) {
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException("Text can`t be empty");
        }

        if (minLength < 0) {
            throw new IllegalArgumentException("Min text length should be larger then 0");
        }

        return Arrays.stream(text.split("\\s+"))
                     .filter(str -> str.length() > minLength)
                     .collect(Collectors.toList());
    }

    /**
     * Find end index of first word
     *
     * @param text where to find end index of first word
     * @return index of first word
     */
    private int findEndIndexOfFirstWord(final String text) {

        return text.contains(" ")
                ? text.indexOf(" ")
                : text.length();
    }
}
