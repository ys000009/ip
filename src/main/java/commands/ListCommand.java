package commands;

import exceptions.BobException;
import storage.TaskStorage;
import tasks.TaskList;
import ui.Ui;

/**
 * Command to display all tasks in the task list.
 */
public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, TaskStorage storage) throws BobException {
        ui.showTaskList(tasks);
    }
}

