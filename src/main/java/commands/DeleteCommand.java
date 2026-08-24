package commands;

import exceptions.BobException;
import tasks.Task;

public class DeleteCommand extends Command {
    public boolean processInput(String input) throws BobException {
        String[] parts = input.split(" ", 2);
        if (parts[0].equals("delete")) {
            int taskId;
            try {
                taskId = Integer.parseInt(parts[1]);
                Task task = this.taskList.remove(taskId - 1);
                System.out.println("Removed: ");
                System.out.println(task.toString());
                System.out.println(String.format(
                    "%d %s in list",
                    this.taskList.size(),
                    this.taskList.size() < 2 ? "item" : "items"
                ));
            } catch (NumberFormatException e) {
                throw new BobException("Error: Argument must be an integer");
            } catch (IndexOutOfBoundsException e) {
                throw new BobException(
                    """
                    Error: taskId out of bounds
                    """);
            }
            return true;
        }
        return false;
    }
}
