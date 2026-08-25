package bob.command;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import bob.task.ToDo;

/**
 * Unit tests for {@link ListCommand}.
 */
public class ListCommandTest extends CommandTestBase {

    @Test
    public void isExit_returnsFalse() {
        ListCommand command = new ListCommand();
        assertFalse(command.isExit());
    }

    @Test
    public void execute_showsListWithoutError() {
        tasks.add(new ToDo("task 1"));
        ListCommand command = new ListCommand();
        assertDoesNotThrow(() -> command.execute(tasks, ui, storage));
    }
}
