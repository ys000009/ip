package ui;

import java.util.Scanner;

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
