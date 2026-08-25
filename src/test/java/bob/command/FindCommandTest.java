package bob.command;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import bob.exception.BobException;
import bob.task.ToDo;

/**
 * Unit tests for {@link FindCommand}.
 */
public class FindCommandTest extends CommandTestBase {

    @Test
    public void execute_matchingKeyword_displaysMatchingTasks() throws BobException {
        tasks.add(new ToDo("read book"));
        tasks.add(new ToDo("return book"));
        tasks.add(new ToDo("buy milk"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outContent));
            FindCommand command = new FindCommand("book");
            command.execute(tasks, ui, storage);
        } finally {
            System.setOut(originalOut);
        }

        String output = outContent.toString();
        assertTrue(output.contains("Here are the matching tasks in your list:"));
        assertTrue(output.contains("1.[T][ ] read book"));
        assertTrue(output.contains("2.[T][ ] return book"));
        assertFalse(output.contains("buy milk"));
    }

    @Test
    public void isExit_returnsFalse() {
        FindCommand command = new FindCommand("test");
        assertFalse(command.isExit());
    }
}
