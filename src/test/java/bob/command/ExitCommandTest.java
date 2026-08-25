package bob.command;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ExitCommand}.
 */
public class ExitCommandTest extends CommandTestBase {

    @Test
    public void isExit_returnsTrue() {
        ExitCommand command = new ExitCommand();
        assertTrue(command.isExit());
    }

    @Test
    public void execute_showsGoodbyeWithoutError() {
        ExitCommand command = new ExitCommand();
        assertDoesNotThrow(() -> command.execute(tasks, ui, storage));
    }
}
