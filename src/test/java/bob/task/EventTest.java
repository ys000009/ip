package bob.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Event}.
 */
public class EventTest {

    @Test
    public void toString_unmarkedAndMarked_formattedCorrectly() {
        LocalDateTime from = LocalDateTime.of(2026, 11, 11, 14, 0);
        LocalDateTime to = LocalDateTime.of(2026, 11, 11, 16, 0);
        Event event = new Event("project meeting", from, to);

        assertEquals(
                "[E][ ] project meeting (from: 11 November 2026, 1400 hrs to: 11 November 2026, 1600 hrs)",
                event.toString());

        event.mark();
        assertEquals(
                "[E][X] project meeting (from: 11 November 2026, 1400 hrs to: 11 November 2026, 1600 hrs)",
                event.toString());
    }

    @Test
    public void export_unmarkedAndMarked_formattedCorrectly() {
        LocalDateTime from = LocalDateTime.of(2026, 11, 11, 14, 0);
        LocalDateTime to = LocalDateTime.of(2026, 11, 11, 16, 0);
        Event event = new Event("project meeting", from, to);

        assertEquals("E | 0 | project meeting | 2026-11-11T14:00:00 | 2026-11-11T16:00:00", event.export());

        event.mark();
        assertEquals("E | 1 | project meeting | 2026-11-11T14:00:00 | 2026-11-11T16:00:00", event.export());
    }
}
