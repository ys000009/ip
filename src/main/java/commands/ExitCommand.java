package commands;

import exceptions.BobException;
import storage.TaskStorage;
import tasks.TaskList;
import ui.Ui;

/**
 * Command to exit the application.
 */
public class ExitCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, TaskStorage storage) throws BobException {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}

