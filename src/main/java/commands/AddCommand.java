package commands;

import java.util.ArrayList;

import exceptions.BobException;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.ToDo;


public class AddCommand extends Command {
    public AddCommand(ArrayList<Task> taskList) {
        super(taskList);
    }

    public boolean processInput(String input) throws BobException {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        if (command.equals("todo") || command.equals("deadline") || command.equals("event")) {
            String arg;
            try {
                arg = parts[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new BobException(command + " needs a description");
            }

            switch (command) {
                case "todo":
                    ToDo todo = new ToDo(arg);
                    this.taskList.add(todo); 
                    this.printAddition(todo);
                    break;
                
                case "deadline":
                    String[] deadlineParts = arg.split(" /by ");
                    Deadline deadline;
                    try {
                        deadline = new Deadline(deadlineParts[0], deadlineParts[1]);

                    } catch (ArrayIndexOutOfBoundsException e) {
                        throw new BobException(
                            """
                            Error: No deadline set for deadline task
                            Usage: deadline ___ \\by ___
                            """
                        );

                    }
                    this.taskList.add(deadline); 
                    this.printAddition(deadline);
                    break;
                
                case "event":
                    String[] eventParts = arg.split(" /from ", 2);
                    Event event;
                    try {
                        String[] dateParts = eventParts[1].split(" /to ", 2);
                        event = new Event(eventParts[0], dateParts[0], dateParts[1]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        throw new BobException(
                            """
                            Error: Missing either /from or /to
                            Usage: event ___ /from ___ /to ___
                            """
                        );
                    }
                    this.taskList.add(event); 
                    this.printAddition(event);
                    break;
            }
            return true;
        }

        return false;
    }

    private void printAddition(Task t) {
        System.out.println("Task added:");
        System.out.println(t.toString());
        System.out.println(String.format(
            "%d %s in list",
            this.taskList.size(),
            this.taskList.size() < 2 ? "item" : "items"
        ));
    }
}
