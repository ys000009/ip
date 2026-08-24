package commands;

import exceptions.BobException;

public class MarkCommand extends Command {
    public boolean processInput(String input) throws BobException {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        int taskId;
        switch (command) {
            case "mark":
                try {
                    taskId = Integer.parseInt(parts[1]);
                    this.taskList.get(taskId - 1).mark();
                } catch (NumberFormatException e) {
                    throw new BobException("Error: Argument must be an integer");
                } catch (IndexOutOfBoundsException e) {
                    throw new BobException("Error: taskId out of bounds");
                }
                System.out.println("Marked as done:");
                System.out.println(" " + this.taskList.get(taskId - 1).toString());
                return true;
            
            case "unmark":
                try {
                    taskId = Integer.parseInt(parts[1]);
                    this.taskList.get(taskId - 1).unmark();
                } catch (NumberFormatException e) {
                    throw new BobException("Error: Argument must be an integer");
                } catch (IndexOutOfBoundsException e) {
                    throw new BobException("Error: taskId out of bounds");
                }
                System.out.println("Marked as not done:");
                System.out.println(" " + this.taskList.get(taskId - 1).toString());
                return true;
        }
        return false;
    }
}
