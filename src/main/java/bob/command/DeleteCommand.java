package bob.command;

import bob.exception.BobException;
import bob.storage.TaskStorage;
import bob.task.Task;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Command to delete a task from the task list.
 */
public class DeleteCommand extends Command {
    private final int taskId;

    /**
     * Constructs a DeleteCommand with the specified 1-based task ID.
     *
     * @param taskId the 1-based index of the task to delete
     */
    public DeleteCommand(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, TaskStorage storage) throws BobException {
        try {
            Task task = tasks.remove(taskId - 1);
            storage.save(tasks);
            ui.showTaskDeleted(task, tasks.size());
        } catch (IndexOutOfBoundsException e) {
            throw new BobException("Error: taskId out of bounds");
        }
    }
}
