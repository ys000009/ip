import commands.AddCommand;
import commands.Command;
import commands.DeleteCommand;
import commands.ExitCommand;
import commands.ListCommand;
import commands.MarkCommand;
import exceptions.BobException;
import exceptions.ExitException;
import storage.TaskStorage;
import tasks.TaskList;
import ui.Ui;

public class Bob {
    private static TaskStorage storage = new TaskStorage();
    private static Ui ui = new Ui();
    private static Command[] commands = {
            new MarkCommand(),
            new ExitCommand(),
            new ListCommand(),
            new AddCommand(),
            new DeleteCommand()
    };

    public static void main(String[] args) {
        ui.showWelcome();

        TaskList tasks;
        try {
            tasks = storage.load();
        } catch (BobException e) {
            ui.showError(e.getMessage());
            return;
        }

        for (Command c : commands) {
            c.setTaskList(tasks);
        }

        while (ui.hasNextCommand()) {
            String nextLine = ui.readCommand();
            ui.showDividerLine();
            boolean processed = false;
            try {
                for (Command c : commands) {
                    processed = processed || c.processInput(nextLine);
                }

                // save tasks after every command
                storage.save(tasks);

                if (!processed) {
                    ui.showMessage("What's that?");
                }

            } catch (ExitException e) {
                ui.showMessage(e.getMessage());
                break;
            } catch (BobException e) {
                ui.showError(e.getMessage());
            }

            ui.showDividerLine();
        }
    }
}
