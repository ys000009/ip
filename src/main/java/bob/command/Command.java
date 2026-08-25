package bob.command;

import bob.exception.BobException;
import bob.storage.TaskStorage;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Represents an executable command in the application.
 */
public abstract class Command {

    /**
     * Constructs a Command.
     */
    public Command() {
    }

    /**
     * Executes the command with the given task list, user interface, and storage.
     *
     * @param tasks   the list of tasks
     * @param ui      the user interface handler
     * @param storage the storage handler
     * @throws BobException if an error occurs during execution
     */
    public abstract void execute(TaskList tasks, Ui ui, TaskStorage storage) throws BobException;

    /**
     * Indicates whether this command causes the application to exit.
     *
     * @return true if the command causes an exit, false otherwise
     */
    public boolean isExit() {
        return false;
    }
}
