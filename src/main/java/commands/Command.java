package commands;

import java.util.ArrayList;

import exceptions.BobException;
import exceptions.ExitException;
import tasks.Task;

public abstract class Command {
    protected ArrayList<Task> taskList;

    public Command setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
        return this;
    }

    public abstract boolean processInput(String input) throws BobException, ExitException;
}
