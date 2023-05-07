package ua.zhytariuk.model;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;
import java.util.Objects;

/**
 * Comparator for comparing a Pair of a Question and a Double value, used in Tree data structures.
 * Comparison is first done by the Double value, and if they are equal, by the text of the Question.
 * The text comparison is not important for ordering, so the shortest text is prioritized.
 *
 * @author (ozhytary)
 */
public class QuestionPairComparator implements Comparator<Pair<Question, Double>> {
    @Override
    public int compare(final Pair<Question, Double> o1, final Pair<Question, Double> o2) {
        final Comparator<Pair<Question, Double>> comparator = Comparator.comparing(Pair::getRight);
        int result = comparator.compare(o1, o2);

        //If double value are equals than compare by question text
        if (result == 0) {
            final String text1 = o1.getLeft()
                                   .getText();
            final String text2 = o2.getLeft()
                                   .getText();

            //If text are equals then objects are equals
            if (Objects.equals(text1, text2)) return 0;

            //Not important comparing because I don`t have any requirements for ordering of text
            return text1.compareTo(text2);
        }

        return result;
    }
}
