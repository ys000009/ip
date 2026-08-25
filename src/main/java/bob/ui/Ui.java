package bob.ui;

import java.util.Scanner;

import bob.task.Task;
import bob.task.TaskList;

/**
 * Handles interactions with the user, such as reading input and displaying
 * messages.
 */
public class Ui {
    private static final String HORIZONTAL_LINE = "_".repeat(30);
    private final Scanner scanner;

    /**
     * Constructs a new Ui instance with standard input scanner.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome greeting to the user.
     */
    public void showWelcome() {
        showDividerLine();
        System.out.println("Hello! I'm Bob.");
        System.out.println("What can I do for you?");
        showDividerLine();
    }

    /**
     * Displays the goodbye message to the user.
     */
    public void showGoodbye() {
        System.out.println("Goodbye.");
    }

    /**
     * Prints a horizontal divider line.
     */
    public void showDividerLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Reads a line of user input.
     *
     * @return the command string entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Checks if there is another line of input available.
     *
     * @return true if there is input available, false otherwise
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Displays the full list of tasks.
     *
     * @param tasks the task list to display
     */
    public void showTaskList(TaskList tasks) {
        System.out.println("Tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format(
                    "%d: %s",
                    i + 1,
                    tasks.get(i).toString()));
        }
    }

    /**
     * Displays the message after adding a task.
     *
     * @param task       the added task
     * @param totalCount the total number of tasks after addition
     */
    public void showTaskAdded(Task task, int totalCount) {
        System.out.println("Task added:");
        System.out.println(task.toString());
        System.out.println(String.format(
                "%d %s in list",
                totalCount,
                totalCount < 2 ? "item" : "items"));
    }

    /**
     * Displays the message after deleting a task.
     *
     * @param task       the deleted task
     * @param totalCount the total number of tasks after deletion
     */
    public void showTaskDeleted(Task task, int totalCount) {
        System.out.println("Removed: ");
        System.out.println(task.toString());
        System.out.println(String.format(
                "%d %s in list",
                totalCount,
                totalCount < 2 ? "item" : "items"));
    }

    /**
     * Displays the message after marking or unmarking a task.
     *
     * @param task   the task that was marked or unmarked
     * @param isDone true if marked as done, false if marked as not done
     */
    public void showTaskMarked(Task task, boolean isDone) {
        if (isDone) {
            System.out.println("Marked as done:");
        } else {
            System.out.println("Marked as not done:");
        }
        System.out.println(" " + task.toString());
    }

    /**
     * Displays a message to the user.
     *
     * @param message the message to display
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Closes the underlying scanner resource.
     */
    public void close() {
        scanner.close();
    }
}
