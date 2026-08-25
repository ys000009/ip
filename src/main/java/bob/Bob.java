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
     * Runs the main command loop of the application.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit && ui.hasNextCommand()) {
            String fullCommand = ui.readCommand();
            ui.showDividerLine();
            try {
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (BobException e) {
                ui.showError(e.getMessage());
            }
            ui.showDividerLine();
        }
    }

    /**
     * Starts the Bob task manager application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        new Bob().run();
    }
}
