import java.util.Scanner;

/**
 * Starts the Bkxss chatbot and displays its initial greeting.
 */
public class Bkxss {
    private static final String BOT_PREFIX = "     ";
    private static final String DIVIDER = "    ____________________________________________________________";
    private static final int MAX_TASKS = 100;

    /**
     * Greets the user, stores task descriptions, lists stored tasks, and exits when the user enters {@code bye}.
     *
     * @param args command-line arguments, which are not used by this application
     */
    public static void main(String[] args) {
        Task[] tasks = new Task[MAX_TASKS];
        int numberOfTasks = 0;
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
                    numberOfTasks = handleCommand(command, tasks, numberOfTasks);
                } catch (BkxssException exception) {
                    System.out.println(BOT_PREFIX + "OhNo!! ERROR :( --> " + exception.getMessage());
                }
                System.out.println(DIVIDER);
            }
        }
    }

    /**
     * Processes one command and returns the resulting number of stored tasks.
     *
     * @param command command supplied by the user
     * @param tasks task storage
     * @param numberOfTasks current number of stored tasks
     * @return updated number of stored tasks
     * @throws BkxssException if the command is invalid
     */
    private static int handleCommand(String command, Task[] tasks, int numberOfTasks) throws BkxssException {
        if (command.equals("list")) {
            System.out.println(BOT_PREFIX + "Here are the tasks in your list:");
            for (int i = 0; i < numberOfTasks; i++) {
                System.out.println(BOT_PREFIX + (i + 1) + "." + tasks[i]);
            }
            return numberOfTasks;
        }
        if (command.startsWith("list") && command.substring(4).isBlank()) {
            throw new BkxssException("omg! you've entered an empty space at the end of the \"list\" accidentally");
        }
        if (command.equals("todo") || command.startsWith("todo ")) {
            return addTask(new Todo(requireDescription(command.substring(4), "todo")), tasks, numberOfTasks);
        }
        if (command.equals("deadline") || command.startsWith("deadline ")) {
            String[] parts = command.substring(8).trim().split(" /by ", 2);
            if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
                throw new BkxssException("a deadline needs a description and a due date. Use: deadline DESCRIPTION /by DATE");
            }
            return addTask(new Deadline(parts[0], parts[1]), tasks, numberOfTasks);
        }
        if (command.equals("event") || command.startsWith("event ")) {
            String[] parts = command.substring(5).trim().split(" /from | /to ", 3);
            if (parts.length != 3 || parts[0].isBlank() || parts[1].isBlank() || parts[2].isBlank()) {
                throw new BkxssException("an event needs a description, start, and end time. Use: event DESCRIPTION /from START /to END");
            }
            return addTask(new Event(parts[0], parts[1], parts[2]), tasks, numberOfTasks);
        }
        if (command.equals("mark") || command.startsWith("mark ")) {
            Task task = getTask(command.substring(4), tasks, numberOfTasks);
            if (task.isDone()) {
                throw new BkxssException("this task is already marked as done!");
            }
            task.markAsDone();
            System.out.println(BOT_PREFIX + "Nice! I've marked this task as done:");
            System.out.println(BOT_PREFIX + "  " + task);
            return numberOfTasks;
        }
        if (command.equals("unmark") || command.startsWith("unmark ")) {
            Task task = getTask(command.substring(6), tasks, numberOfTasks);
            if (!task.isDone()) {
                throw new BkxssException("this task is already unmarked!");
            }
            task.markAsNotDone();
            System.out.println(BOT_PREFIX + "OK, I've marked this task as not done yet:");
            System.out.println(BOT_PREFIX + "  " + task);
            return numberOfTasks;
        }
        throw new BkxssException("I'm sorry, but I don't know what that means :-(");
    }

    /** Adds a task after confirming that storage is available. */
    private static int addTask(Task task, Task[] tasks, int numberOfTasks) throws BkxssException {
        if (numberOfTasks == MAX_TASKS) {
            throw new BkxssException("the task list is full. Please remove a task before adding another one.");
        }
        tasks[numberOfTasks] = task;
        System.out.println(BOT_PREFIX + "Got it. I've added this task:");
        System.out.println(BOT_PREFIX + task);
        System.out.println(BOT_PREFIX + "Now you have " + (numberOfTasks + 1) + " tasks in the list.");
        return numberOfTasks + 1;
    }

    /** Returns a non-empty task description for the specified command type. */
    private static String requireDescription(String description, String commandName) throws BkxssException {
        if (description.isBlank()) {
            throw new BkxssException("The description of a " + commandName + " cannot be empty.");
        }
        return description.trim();
    }

    /** Returns the requested task after validating that its number is in the task list. */
    private static Task getTask(String numberText, Task[] tasks, int numberOfTasks) throws BkxssException {
        try {
            int taskNumber = Integer.parseInt(numberText.trim());
            if (taskNumber < 1 || taskNumber > numberOfTasks) {
                throw new BkxssException("there is no task numbered " + taskNumber + ".");
            }
            return tasks[taskNumber - 1];
        } catch (NumberFormatException exception) {
            throw new BkxssException("please provide a task number. Use: mark NUMBER or unmark NUMBER");
        }
    }
}