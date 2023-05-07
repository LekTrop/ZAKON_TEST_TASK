package ua.zhytariuk.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.zhytariuk.config.ElasticSearchProperties;

import java.util.List;

import static ua.zhytariuk.TestData.TEXT_1;

/**
 * @author (ozhytary)
 */
@ExtendWith(MockitoExtension.class)
class LevenshteinDistanceSimilarityCalculatorTest {

    @InjectMocks
    private LevenshteinDistanceSimilarityCalculator unit;
    @Mock
    private ElasticSearchProperties mockEsProperties;

    @Test
    void calculateSimilarity_with_similar() {
        //GIVE
        final List<String> similarFrom = List.of(TEXT_1.split("\\s+"));
        final List<String> similarTo = List.of(TEXT_1.split("\\s+"));

        //WHEN
        final Double similarity = unit.calculateSimilarity(similarTo, similarFrom);

        //THEN
        Assertions.assertEquals(similarity, 1D);
    }

    @Test
    void calculateSimilarity_without_similar() {
        //GIVE
        final List<String> similarFrom = List.of("random", "random", "testiiiing");
        final List<String> similarTo = List.of(TEXT_1.split("\\s+"));

        //WHEN
        final Double similarity = unit.calculateSimilarity(similarTo, similarFrom);

        //THEN
        Assertions.assertEquals(similarity, -1D);
    }
}