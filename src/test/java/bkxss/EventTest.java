package bkxss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/** Tests parsing event boundaries for schedule calculations. */
class EventTest {
    @Test
    void event_datedBoundaries_returnsParsedDateTimes() {
        Event event = new Event("lecture", "2026-09-12 0900", "2026-09-12 1100");

        assertTrue(event.hasScheduledTimes());
        assertEquals(LocalDateTime.of(2026, 9, 12, 9, 0), event.getFromDateTime().orElseThrow());
        assertEquals(LocalDateTime.of(2026, 9, 12, 11, 0), event.getToDateTime().orElseThrow());
    }

    @Test
    void event_legacyFreeTextBoundaries_isNotScheduledButKeepsDisplayText() {
        Event event = new Event("meeting", "Mon 2pm", "4pm");

        assertFalse(event.hasScheduledTimes());
        assertEquals("[E][ ] meeting (from: Mon 2pm to: 4pm)", event.toString());
    }

    @Test
    void event_endBeforeStart_isNotScheduled() {
        Event event = new Event("meeting", "2026-09-12 1200", "2026-09-12 1100");

        assertFalse(event.hasScheduledTimes());
    }
}
