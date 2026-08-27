import java.util.ArrayList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Starts the Bkxss chatbot and displays its initial greeting.
 */
public class Bkxss {
    private static final Path DATA_FILE = Path.of(".", "data", "bkxss.txt");
    private static final String BOT_PREFIX = "     ";
    private static final String DIVIDER = "    ____________________________________________________________";

    /**
     * Greets the user, stores task descriptions, lists stored tasks, and exits when the user enters {@code bye}.
     *
     * @param args command-line arguments, which are not used by this application
     */
    public static void main(String[] args) {
        ArrayList<Task> tasks = loadTasks();
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
                        saveTasks(tasks);
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
        if (command.equals("todo") || command.startsWith("todo ")) {
            addTask(new Todo(requireDescription(command.substring(4), "todo")), tasks);
            return true;
        }
        if (command.equals("deadline") || command.startsWith("deadline ")) {
            String[] parts = command.substring(8).trim().split(" /by ", 2);
            if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
                throw new BkxssException("a deadline needs a description and a due date. Use: deadline DESCRIPTION /by DATE");
            }
            addTask(new Deadline(parts[0], parts[1]), tasks);
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

    /** Loads saved tasks, treating a missing file as an empty task list. */
    private static ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(DATA_FILE)) {
            return tasks;
        }
        try {
            for (String line : Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8)) {
                String[] fields = line.split("\\s*\\|\\s*", -1);
                if (fields.length < 3) {
                    continue;
                }
                String type = fields[0];
                Task task;
                if (type.equals("T") && fields.length == 3) {
                    task = new Todo(fields[2]);
                } else if (type.equals("D") && fields.length == 4) {
                    task = new Deadline(fields[2], fields[3]);
                } else if (type.equals("E") && fields.length == 4) {
                    String[] times = fields[3].split(" to ", 2);
                    if (times.length != 2) {
                        continue;
                    }
                    task = new Event(fields[2], times[0], times[1]);
                } else {
                    continue;
                }
                if (fields[1].equals("1")) {
                    task.markAsDone();
                }
                tasks.add(task);
            }
        } catch (IOException | IllegalArgumentException exception) {
            System.out.println(BOT_PREFIX + "I couldn't load the saved tasks, so I'll start with an empty list.");
        }
        return tasks;
    }

    /** Saves all tasks, creating the data directory when necessary. */
    private static void saveTasks(ArrayList<Task> tasks) {
        try {
            Files.createDirectories(DATA_FILE.getParent());
            ArrayList<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                if (task instanceof Todo) {
                    lines.add("T | " + status(task) + " | " + task.description);
                } else if (task instanceof Deadline deadline) {
                    lines.add("D | " + status(task) + " | " + task.description + " | " + deadline.getBy());
                } else if (task instanceof Event event) {
                    lines.add("E | " + status(task) + " | " + task.description + " | " + event.getFrom() + " to " + event.getTo());
                }
            }
            Files.write(DATA_FILE, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.out.println(BOT_PREFIX + "I couldn't save your tasks: " + exception.getMessage());
        }
    }

    private static String status(Task task) { return task.isDone() ? "1" : "0"; }
}
