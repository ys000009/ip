package commands;

import exceptions.BobException;
import storage.TaskStorage;
import tasks.Task;
import tasks.TaskList;
import ui.Ui;

/**
 * Command to add a task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Constructs an AddCommand with the specified task to add.
     *
     * @param task the task to be added
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, TaskStorage storage) throws BobException {
        tasks.add(this.task);
        storage.save(tasks);
        ui.showTaskAdded(this.task, tasks.size());
    }
}

