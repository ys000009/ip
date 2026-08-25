package bob.parser;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bob.command.AddCommand;
import bob.command.Command;
import bob.command.DeleteCommand;
import bob.command.ExitCommand;
import bob.command.FindCommand;
import bob.command.ListCommand;
import bob.command.MarkCommand;
import bob.exception.BobException;

public class ParserTest {

    @Test
    public void parse_byeCommand_returnsExitCommand() throws BobException {
        Command command = Parser.parse("bye");
        assertInstanceOf(ExitCommand.class, command);
        assertTrue(command.isExit());
    }

    @Test
    public void parse_listCommand_returnsListCommand() throws BobException {
        Command command = Parser.parse("list");
        assertInstanceOf(ListCommand.class, command);
    }

    @Test
    public void parse_markCommand_returnsMarkCommand() throws BobException {
        Command command = Parser.parse("mark 2");
        assertInstanceOf(MarkCommand.class, command);
    }

    @Test
    public void parse_unmarkCommand_returnsMarkCommand() throws BobException {
        Command command = Parser.parse("unmark 2");
        assertInstanceOf(MarkCommand.class, command);
    }

    @Test
    public void parse_markInvalidInteger_throwsBobException() {
        assertThrows(BobException.class, () -> Parser.parse("mark abc"));
        assertThrows(BobException.class, () -> Parser.parse("mark"));
    }

    @Test
    public void parse_deleteCommand_returnsDeleteCommand() throws BobException {
        Command command = Parser.parse("delete 1");
        assertInstanceOf(DeleteCommand.class, command);
    }

    @Test
    public void parse_deleteInvalidInteger_throwsBobException() {
        assertThrows(BobException.class, () -> Parser.parse("delete xyz"));
        assertThrows(BobException.class, () -> Parser.parse("delete"));
    }

    @Test
    public void parse_todoCommand_returnsAddCommand() throws BobException {
        Command command = Parser.parse("todo read book");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void parse_todoEmptyDescription_throwsBobException() {
        assertThrows(BobException.class, () -> Parser.parse("todo"));
        assertThrows(BobException.class, () -> Parser.parse("todo   "));
    }

    @Test
    public void parse_deadlineCommand_returnsAddCommand() throws BobException {
        Command command = Parser.parse("deadline submit report /by 11/11/26 18:45");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void parse_deadlineMissingByOrDate_throwsBobException() {
        assertThrows(BobException.class, () -> Parser.parse("deadline submit report"));
        assertThrows(BobException.class, () -> Parser.parse("deadline submit report /by "));
        assertThrows(BobException.class, () -> Parser.parse("deadline /by 11/11/26 18:45"));
    }

    @Test
    public void parse_deadlineInvalidDateFormat_throwsBobException() {
        assertThrows(BobException.class, () -> Parser.parse("deadline submit report /by 2026-11-11"));
        assertThrows(BobException.class, () -> Parser.parse("deadline submit report /by not-a-date"));
    }

    @Test
    public void parse_eventCommand_returnsAddCommand() throws BobException {
        Command command = Parser.parse("event team meeting /from 11/11/26 14:00 /to 11/11/26 16:00");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void parse_eventMissingParts_throwsBobException() {
        assertThrows(BobException.class, () -> Parser.parse("event meeting /from 11/11/26 14:00"));
        assertThrows(BobException.class, () -> Parser.parse("event meeting /to 11/11/26 16:00"));
        assertThrows(BobException.class, () -> Parser.parse("event /from 11/11/26 14:00 /to 11/11/26 16:00"));
    }

    @Test
    public void parse_eventInvalidDateFormat_throwsBobException() {
        assertThrows(BobException.class, () -> Parser.parse("event meeting /from invalid /to 11/11/26 16:00"));
        assertThrows(BobException.class, () -> Parser.parse("event meeting /from 11/11/26 14:00 /to invalid"));
    }

    @Test
    public void parse_findCommand_returnsFindCommand() throws BobException {
        Command command = Parser.parse("find book");
        assertInstanceOf(FindCommand.class, command);
    }

    @Test
    public void parse_findEmptyKeyword_throwsBobException() {
        assertThrows(BobException.class, () -> Parser.parse("find"));
        assertThrows(BobException.class, () -> Parser.parse("find   "));
    }

    @Test
    public void parse_unknownOrEmptyCommand_throwsBobException() {
        assertThrows(BobException.class, () -> Parser.parse("foobar"));
        assertThrows(BobException.class, () -> Parser.parse(""));
        assertThrows(BobException.class, () -> Parser.parse("   "));
        assertThrows(BobException.class, () -> Parser.parse(null));
    }
}
