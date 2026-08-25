package commands;

import exceptions.BobException;
import exceptions.ExitException;
import tasks.TaskList;

public abstract class Command {
    protected TaskList taskList;

    public Command setTaskList(TaskList taskList) {
        this.taskList = taskList;
        return this;
    }

    public abstract boolean processInput(String input) throws BobException, ExitException;
}
