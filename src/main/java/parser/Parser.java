package parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import commands.AddCommand;
import commands.Command;
import commands.DeleteCommand;
import commands.ExitCommand;
import commands.ListCommand;
import commands.MarkCommand;
import exceptions.BobException;
import tasks.Deadline;
import tasks.Event;
import tasks.ToDo;
import util.DatetimeHelper;

/**
 * Parses user input into executable Command objects.
 */
public class Parser {

    /**
     * Parses the full user command string into a specific Command.
     *
     * @param fullCommand the raw input entered by the user
     * @return the Command corresponding to the input
     * @throws BobException if the command is unrecognized or has invalid arguments
     */
    public static Command parse(String fullCommand) throws BobException {
        if (fullCommand == null || fullCommand.isBlank()) {
            throw new BobException("What's that?");
        }

        String[] parts = fullCommand.trim().split(" ", 2);
        String commandWord = parts[0];
        String arguments = parts.length > 1 ? parts[1].trim() : "";

        switch (commandWord) {
            case "bye":
                return new ExitCommand();

            case "list":
                return new ListCommand();

            case "mark":
                return parseMarkCommand(arguments, true);

            case "unmark":
                return parseMarkCommand(arguments, false);

            case "delete":
                return parseDeleteCommand(arguments);

            case "todo":
                return parseTodoCommand(arguments);

            case "deadline":
                return parseDeadlineCommand(arguments);

            case "event":
                return parseEventCommand(arguments);

            default:
                throw new BobException("What's that?");
        }
    }

    private static Command parseMarkCommand(String args, boolean isDone) throws BobException {
        if (args.isEmpty()) {
            throw new BobException("Error: Argument must be an integer");
        }
        try {
            int taskId = Integer.parseInt(args);
            return new MarkCommand(taskId, isDone);
        } catch (NumberFormatException e) {
            throw new BobException("Error: Argument must be an integer");
        }
    }

    private static Command parseDeleteCommand(String args) throws BobException {
        if (args.isEmpty()) {
            throw new BobException("Error: Argument must be an integer");
        }
        try {
            int taskId = Integer.parseInt(args);
            return new DeleteCommand(taskId);
        } catch (NumberFormatException e) {
            throw new BobException("Error: Argument must be an integer");
        }
    }

    private static Command parseTodoCommand(String args) throws BobException {
        if (args.isEmpty()) {
            throw new BobException("todo needs a description");
        }
        return new AddCommand(new ToDo(args));
    }

    private static Command parseDeadlineCommand(String args) throws BobException {
        if (args.isEmpty()) {
            throw new BobException("deadline needs a description");
        }

        String[] parts = args.split(" /by ", 2);
        if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new BobException("""
                    Error: No deadline set for deadline task
                    Usage: deadline ___ \\by ___
                    """);
        }

        try {
            LocalDateTime by = LocalDateTime.parse(parts[1], DatetimeHelper.INPUT_FORMATTER);
            return new AddCommand(new Deadline(parts[0], by));
        } catch (DateTimeParseException e) {
            throw new BobException("""
                    Error: Cannot parse date
                    Date Format: dd/MM/yy HH:mm
                    """);
        }
    }

    private static Command parseEventCommand(String args) throws BobException {
        if (args.isEmpty()) {
            throw new BobException("event needs a description");
        }

        String[] parts = args.split(" /from ", 2);
        if (parts.length < 2 || parts[0].isBlank()) {
            throw new BobException("""
                    Error: Missing either /from or /to
                    Usage: event ___ /from ___ /to ___
                    """);
        }

        String[] dateParts = parts[1].split(" /to ", 2);
        if (dateParts.length < 2 || dateParts[0].isBlank() || dateParts[1].isBlank()) {
            throw new BobException("""
                    Error: Missing either /from or /to
                    Usage: event ___ /from ___ /to ___
                    """);
        }

        try {
            LocalDateTime from = LocalDateTime.parse(dateParts[0], DatetimeHelper.INPUT_FORMATTER);
            LocalDateTime to = LocalDateTime.parse(dateParts[1], DatetimeHelper.INPUT_FORMATTER);
            return new AddCommand(new Event(parts[0], from, to));
        } catch (DateTimeParseException e) {
            throw new BobException("""
                    Error: Cannot parse date
                    Date Format: dd/MM/yy HH:mm
                    """);
        }
    }
}
