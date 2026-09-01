package bob;

import bob.command.Command;
import bob.exception.BobException;
import bob.parser.Parser;
import bob.storage.TaskStorage;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Main entry point for the Bob task management application.
 */
public class Bob {
    private final TaskStorage storage;
    private TaskList tasks;
    private final Ui ui;

    private boolean isExit = false;

    /**
     * Constructs a new Bob application instance.
     */
    public Bob() {
        this.ui = new Ui();
        this.storage = new TaskStorage();
        try {
            this.tasks = this.storage.load();
        } catch (BobException e) {
            this.ui.showError(e.getMessage());
            this.tasks = new TaskList();
        }
    }

    /**
     * Runs the main command loop of the application in CLI mode.
     */
    public void run() {
        ui.showWelcome();
        boolean isRunning = true;

        while (isRunning && ui.hasNextCommand()) {
            String fullCommand = ui.readCommand();
            ui.showDividerLine();
            try {
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                this.isExit = c.isExit();
                isRunning = !this.isExit;
            } catch (BobException e) {
                ui.showError(e.getMessage());
            }
            ui.showDividerLine();
        }
    }

    /**
     * Generates a response for the user's chat message input in GUI mode.
     *
     * @param input the raw input command string entered by the user
     * @return the response string generated after command execution or error handling
     */
    public String getResponse(String input) {
        try {
            Command c = Parser.parse(input);
            c.execute(tasks, ui, storage);
            this.isExit = c.isExit();
            return ui.getLastResponse();
        } catch (BobException e) {
            ui.showError(e.getMessage());
            return ui.getLastResponse();
        }
    }

    /**
     * Checks whether the application has received an exit command.
     *
     * @return true if an exit command was executed, false otherwise
     */
    public boolean isExit() {
        return isExit;
    }

    /**
     * Returns the welcome greeting message for the user.
     *
     * @return the initial greeting string
     */
    public String getGreeting() {
        ui.showWelcome();
        return ui.getLastResponse();
    }

    /**
     * Initializes and launches the Bob application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new Bob().run();
    }
}
