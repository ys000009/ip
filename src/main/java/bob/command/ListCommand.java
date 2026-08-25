package bob.command;

import bob.exception.BobException;
import bob.storage.TaskStorage;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Command to display all tasks in the task list.
 */
public class ListCommand extends Command {

    /**
     * Constructs a ListCommand.
     */
    public ListCommand() {
    }

    @Override
    public void execute(TaskList tasks, Ui ui, TaskStorage storage) throws BobException {
        ui.showTaskList(tasks);
    }
}
