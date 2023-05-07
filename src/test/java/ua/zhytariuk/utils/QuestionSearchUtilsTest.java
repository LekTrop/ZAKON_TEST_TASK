package ua.zhytariuk.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ua.zhytariuk.TestData.TEXT_1;

/**
 * @author (ozhytary)
 */
@ExtendWith(MockitoExtension.class)
class QuestionSearchUtilsTest {

    @InjectMocks
    private QuestionSearchUtils unit;

    @Test
    void getFirstWord_exception() {
        //WHEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> unit.getFirstWord(null));
    }

    @Test
    void getFirstWord_with_space_success() {
        //GIVEN
        final String text = "FIRST WORD";

        //WHEN
        final String result = unit.getFirstWord(text);

        //THEN
        assertEquals(result, "FIRST");
    }

    @Test
    void getFirstWord_without_space_success() {
        //GIVEN
        final String text = "FIRST";

        //WHEN
        final String result = unit.getFirstWord(text);

        //THEN
        assertEquals(result, "FIRST");
    }

    @Test
    void getWordsThatLargerThan() {
        //WHEN
        final List<String> result = unit.getWordsThatLargerThan(3, TEXT_1);

        //THEN
        assertNotNull(result);
        assertEquals(result.size(), TEXT_1.split("\\s+").length);
    }

    @Test
    void getWordsThatLargerThan_number_less_then_0_exception() {
        //WHEN
        assertThrows(IllegalArgumentException.class, () -> unit.getWordsThatLargerThan(-1, TEXT_1));
    }

    @Test
    void getWordsThatLargerThan_empty_text() {
        //WHEN
        assertThrows(IllegalArgumentException.class, () -> unit.getWordsThatLargerThan(1, null));
    }
}