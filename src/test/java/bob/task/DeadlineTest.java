package bob.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DeadlineTest {

    @Test
    public void toString_unmarkedAndMarked_formattedCorrectly() {
        LocalDateTime time = LocalDateTime.of(2026, 11, 11, 18, 45);
        Deadline deadline = new Deadline("submit assignment", time);

        assertEquals("[D][ ] submit assignment (by: 11 November 2026, 1845 hrs)", deadline.toString());

        deadline.mark();
        assertEquals("[D][X] submit assignment (by: 11 November 2026, 1845 hrs)", deadline.toString());
    }

    @Test
    public void export_unmarkedAndMarked_formattedCorrectly() {
        LocalDateTime time = LocalDateTime.of(2026, 11, 11, 18, 45);
        Deadline deadline = new Deadline("submit assignment", time);

        assertEquals("D | 0 | submit assignment | 2026-11-11T18:45:00", deadline.export());

        deadline.mark();
        assertEquals("D | 1 | submit assignment | 2026-11-11T18:45:00", deadline.export());
    }
}
