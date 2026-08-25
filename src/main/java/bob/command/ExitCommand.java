package bob.command;

import bob.exception.BobException;
import bob.storage.TaskStorage;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Command to exit the application.
 */
public class ExitCommand extends Command {

    /**
     * Constructs an ExitCommand.
     */
    public ExitCommand() {
    }

    @Override
    public void execute(TaskList tasks, Ui ui, TaskStorage storage) throws BobException {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
