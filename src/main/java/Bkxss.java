import java.util.Scanner;

/**
 * Starts the Bkxss chatbot and displays its initial greeting.
 */
public class Bkxss {
    /**
     * Greets the user, stores task descriptions, lists stored tasks, and exits when the user enters {@code bye}.
     *
     * @param args command-line arguments, which are not used by this application
     */
    public static void main(String[] args) {
        String botPrefix = "     ";
        String divider = "    ____________________________________________________________";
        String[] tasks = new String[100];
        int numberOfTasks = 0;
        String banner = "____  _                   \n"
                + "| __ )| | ____  _____ ___ \n"
                + "|  _ \\| |/ /\\ \\/ / __/ __|\n"
                + "| |_) |   <  >  <\\__ \\__ \\\n"
                + "|____/|_|\\_\\/_/\\_\\___/___/\n";

        System.out.println(divider);
        System.out.print(botPrefix + banner.replace("\n", "\n" + botPrefix).stripTrailing());
        System.out.println();
        System.out.println(botPrefix + "Hello hello ~ This is Bkxss here ;)");
        System.out.println(botPrefix + "What can I do for you?");
        System.out.println(divider);

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();

                System.out.println(divider);
                if (command.equals("bye")) {
                    System.out.println(botPrefix + "Bye. Hope to see you again soon!");
                    System.out.println(divider);
                    return;
                }

                if (command.equals("list")) {
                    for (int i = 0; i < numberOfTasks; i++) {
                        System.out.println(botPrefix + (i + 1) + ". " + tasks[i]);
                    }
                } else {
                    tasks[numberOfTasks] = command;
                    numberOfTasks++;
                    System.out.println(botPrefix + "added: " + command);
                }
                System.out.println(divider);
            }
        }
    }
}
