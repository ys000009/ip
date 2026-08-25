package bob.command;

import bob.exception.BobException;
import bob.storage.TaskStorage;
import bob.task.Task;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Command to mark or unmark a task in the task list.
 */
public class MarkCommand extends Command {
    private final int taskId;
    private final boolean isDone;

    /**
     * Constructs a MarkCommand with the specified 1-based task ID and target
     * completion state.
     *
     * @param taskId the 1-based index of the task to mark/unmark
     * @param isDone true to mark the task as done, false to mark as not done
     */
    public MarkCommand(int taskId, boolean isDone) {
        this.taskId = taskId;
        this.isDone = isDone;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, TaskStorage storage) throws BobException {
        try {
            Task task = tasks.get(taskId - 1);
            if (isDone) {
                task.mark();
            } else {
                task.unmark();
            }
            storage.save(tasks);
            ui.showTaskMarked(task, isDone);
        } catch (IndexOutOfBoundsException e) {
            throw new BobException("Error: taskId out of bounds");
        }
    }
}
