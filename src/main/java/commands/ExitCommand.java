package commands;

import exceptions.ExitException;

public class ExitCommand extends Command {
    public boolean processInput(String input) throws ExitException {
        if (input.equals("bye")) {
            throw new ExitException("Goodbye.");
        }
        return false;
    }
}
