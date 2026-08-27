package bkxss;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

/**
 * Starts the Bkxss chatbot and displays its initial greeting.
 */
public class Bkxss {
    private static final DateTimeFormatter INPUT_DATE_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("uuuu-MM-dd HHmm")
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);
    private static final String BOT_PREFIX = "     ";
    private static final String DIVIDER = "    ____________________________________________________________";

    /**
     * Greets the user, stores task descriptions, lists stored tasks, and exits when the user enters {@code bye}.
     *
     * @param args command-line arguments, which are not used by this application
     */
    public static void main(String[] args) {
        Storage storage = new Storage("data/bkxss.txt");
        ArrayList<Task> tasks = storage.load();
        String banner = "____  _                   \n"
                + "| __ )| | ____  _____ ___ \n"
                + "|  _ \\| |/ /\\ \\/ / __/ __|\n"
                + "| |_) |   <  >  <\\__ \\__ \\\n"
                + "|____/|_|\\_\\/_/\\_\\___/___/\n";

        System.out.println(DIVIDER);
        System.out.print(BOT_PREFIX + banner.replace("\n", "\n" + BOT_PREFIX).stripTrailing());
        System.out.println();
        System.out.println(BOT_PREFIX + "Hello hello ~ This is Bkxss here ;)");
        System.out.println(BOT_PREFIX + "What can I do for you?");
        System.out.println(DIVIDER);

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();

                System.out.println(DIVIDER);
                if (command.equals("bye")) {
                    System.out.println(BOT_PREFIX + "Bye. Hope to see you again soon!");
                    System.out.println(DIVIDER);
                    return;
                }
                try {
                    boolean changed = handleCommand(command, tasks);
                    if (changed) {
                        storage.save(tasks);
                    }
                } catch (BkxssException exception) {
                    System.out.println(BOT_PREFIX + "OhNo!! ERROR :( --> " + exception.getMessage());
                }
                System.out.println(DIVIDER);
            }
        }
    }

    /**
     * Processes one command against the given task list.
     *
     * @param command command supplied by the user
     * @param tasks task storage
     * @throws BkxssException if the command is invalid
     */
    private static boolean handleCommand(String command, ArrayList<Task> tasks) throws BkxssException {
        if (command.equals("list")) {
            System.out.println(BOT_PREFIX + "Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(BOT_PREFIX + (i + 1) + "." + tasks.get(i));
            }
            return false;
        }
        if (command.startsWith("list") && command.substring(4).isBlank()) {
            throw new BkxssException("omg! you've entered an empty space at the end of the \"list\" accidentally");
        }
        if (command.equals("find") || command.startsWith("find ")) {
            String keyword = command.substring(4).trim();
            if (keyword.isBlank()) {
                throw new BkxssException("please provide a keyword to search for. Use: find KEYWORD");
            }
            System.out.println(BOT_PREFIX + "Here are the matching tasks in your list:");
            int matchNumber = 1;
            for (Task task : tasks) {
                if (task.matchesKeyword(keyword)) {
                    System.out.println(BOT_PREFIX + matchNumber + "." + task);
                    matchNumber++;
                }
            }
            return false;
        }
        if (command.equals("todo") || command.startsWith("todo ")) {
            addTask(new Todo(requireDescription(command.substring(4), "todo")), tasks);
            return true;
        }
        if (command.equals("deadline") || command.startsWith("deadline ")) {
            String[] parts = command.substring(8).trim().split(" /by ", 2);
            if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
                throw new BkxssException("a deadline needs a description and a due date. Use: deadline DESCRIPTION /by DATE");
            }
            addTask(new Deadline(parts[0], parseDeadline(parts[1])), tasks);
            return true;
        }
        if (command.equals("event") || command.startsWith("event ")) {
            String[] parts = command.substring(5).trim().split(" /from | /to ", 3);
            if (parts.length != 3 || parts[0].isBlank() || parts[1].isBlank() || parts[2].isBlank()) {
                throw new BkxssException("an event needs a description, start, and end time. Use: event DESCRIPTION /from START /to END");
            }
            addTask(new Event(parts[0], parts[1], parts[2]), tasks);
            return true;
        }
        if (command.equals("mark") || command.startsWith("mark ")) {
            Task task = getTask(command.substring(4), tasks);
            if (task.isDone()) {
                throw new BkxssException("this task is already marked as done!");
            }
            task.markAsDone();
            System.out.println(BOT_PREFIX + "Nice! I've marked this task as done:");
            System.out.println(BOT_PREFIX + "  " + task);
            return true;
        }
        if (command.equals("unmark") || command.startsWith("unmark ")) {
            Task task = getTask(command.substring(6), tasks);
            if (!task.isDone()) {
                throw new BkxssException("this task is already unmarked!");
            }
            task.markAsNotDone();
            System.out.println(BOT_PREFIX + "OK, I've marked this task as not done yet:");
            System.out.println(BOT_PREFIX + "  " + task);
            return true;
        }
        if (command.equals("delete") || command.startsWith("delete ")) {
            Task task = getTask(command.substring(6), tasks);
            tasks.remove(task);
            System.out.println(BOT_PREFIX + "Noted. I've removed this task:");
            System.out.println(BOT_PREFIX + "  " + task);
            System.out.println(BOT_PREFIX + "Now you have " + tasks.size() + " tasks in the list.");
            return true;
        }
        throw new BkxssException("I'm sorry, but I don't know what that means :-(");
    }

    /** Parses a deadline and gives the user a useful error for invalid dates. */
    private static LocalDateTime parseDeadline(String text) throws BkxssException {
        try {
            return LocalDateTime.parse(text.trim(), INPUT_DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new BkxssException("please provide a valid deadline in yyyy-MM-dd HHmm format, e.g. 2019-12-02 1800");
        }
    }

    /** Adds a task to the list and prints a confirmation. */
    private static void addTask(Task task, ArrayList<Task> tasks) {
        tasks.add(task);
        System.out.println(BOT_PREFIX + "Got it. I've added this task:");
        System.out.println(BOT_PREFIX + task);
        System.out.println(BOT_PREFIX + "Now you have " + tasks.size() + " tasks in the list.");
    }

    /** Returns a non-empty task description for the specified command type. */
    private static String requireDescription(String description, String commandName) throws BkxssException {
        if (description.isBlank()) {
            throw new BkxssException("The description of a " + commandName + " cannot be empty.");
        }
        return description.trim();
    }

    /** Returns the requested task after validating that its number is in the task list. */
    private static Task getTask(String numberText, ArrayList<Task> tasks) throws BkxssException {
        try {
            int taskNumber = Integer.parseInt(numberText.trim());
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                throw new BkxssException("there is no task numbered " + taskNumber + ".");
            }
            return tasks.get(taskNumber - 1);
        } catch (NumberFormatException exception) {
            throw new BkxssException("please provide a task number. Use: mark/unmark/delete NUMBER");
        }
    }

}
