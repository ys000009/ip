package bob.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ToDo}.
 */
public class ToDoTest {

    @Test
    public void toString_unmarkedAndMarked_formattedCorrectly() {
        ToDo todo = new ToDo("buy milk");
        assertEquals("[T][ ] buy milk", todo.toString());

        todo.mark();
        assertEquals("[T][X] buy milk", todo.toString());

        todo.unmark();
        assertEquals("[T][ ] buy milk", todo.toString());
    }

    @Test
    public void export_unmarkedAndMarked_formattedCorrectly() {
        ToDo todo = new ToDo("buy milk");
        assertEquals("T | 0 | buy milk", todo.export());

        todo.mark();
        assertEquals("T | 1 | buy milk", todo.export());

        todo.unmark();
        assertEquals("T | 0 | buy milk", todo.export());
    }
}
