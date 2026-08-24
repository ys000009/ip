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
import tasks.Task;

public class Bob {
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static String horiLines = "_".repeat(30);
    private static Command[] commands = {
        new MarkCommand(tasks),
        new ExitCommand(tasks),
        new ListCommand(tasks),
        new AddCommand(tasks),
        new DeleteCommand(tasks)
    };

    public static void main(String[] args) {
        System.out.println(horiLines);
        System.out.println("Hello! I'm Bob.");
        System.out.println("What can I do for you?");
        System.out.println(horiLines);

        Scanner sc = new Scanner(System.in);
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

