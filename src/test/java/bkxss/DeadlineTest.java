package bkxss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/** Tests deadline formatting and persistence parsing. */
class DeadlineTest {
    @Test
    void deadline_getFormattedBy_formatsDateForDisplay() {
        Deadline deadline = new Deadline("return book", LocalDateTime.of(2019, 12, 2, 18, 0));

        assertEquals("Dec 02 2019 18:00", deadline.getFormattedBy());
    }

    @Test
    void deadline_parseFormattedBy_validText_returnsOriginalDateTime() {
        String formattedDate = "Feb 28 2019 18:00";

        assertEquals(LocalDateTime.of(2019, 2, 28, 18, 0),
                Deadline.parseFormattedBy(formattedDate));
    }

    @Test
    void deadline_parseFormattedBy_invalidText_throwsException() {
        assertThrows(Exception.class, () -> Deadline.parseFormattedBy("not a date"));
    }
}
