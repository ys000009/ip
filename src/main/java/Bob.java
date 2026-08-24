import java.util.Scanner;
import java.util.ArrayList;

import commands.AddCommand;
import commands.Command;
import commands.DeleteCommand;
import commands.ExitCommand;
import commands.ListCommand;
import commands.MarkCommand;
import exceptions.BobException;
import exceptions.ExitException;
import storage.TaskStorage;
import tasks.Task;

public class Bob {
    private static TaskStorage storage = new TaskStorage();
    private static String horiLines = "_".repeat(30);
    private static Command[] commands = {
        new MarkCommand(),
        new ExitCommand(),
        new ListCommand(),
        new AddCommand(),
        new DeleteCommand()
    };

    public static void main(String[] args) {
        System.out.println(horiLines);
        System.out.println("Hello! I'm Bob.");
        System.out.println("What can I do for you?");
        System.out.println(horiLines);

        Scanner sc = new Scanner(System.in);

        ArrayList<Task> tasks;
        try {
            tasks = storage.load();
        } catch (BobException e) {
            System.out.println(e.getMessage());
            return;
        }

        for (Command c: commands) {
            c.setTaskList(tasks);
        }
        
        while (sc.hasNextLine()) {
            String nextLine = sc.nextLine();
            System.out.println(horiLines);
            boolean processed = false;
            try 
            {
                for (Command c : commands) {
                    processed = processed || c.processInput(nextLine);
                }

                if (!processed) {
                    System.out.println("What's that?");
                }
            
            } catch (ExitException e) {
                System.out.println(e.getMessage());
                break;
            } catch (BobException e) {
                System.out.println(e.getMessage());
            }
            

            System.out.println(horiLines);
        }
    }
}

