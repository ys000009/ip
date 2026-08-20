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
        Task[] tasks = new Task[100];
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
                    System.out.println(botPrefix + "Here are the tasks in your list:");
                    for (int i = 0; i < numberOfTasks; i++) {
                        System.out.println(botPrefix + (i + 1) + "." + tasks[i]);
                    }
                } else if (command.startsWith("mark ")) {
                    int taskNumber = Integer.parseInt(command.substring(5));
                    Task task = tasks[taskNumber - 1];
                    task.markAsDone();
                    System.out.println(botPrefix + "Nice! I've marked this task as done:");
                    System.out.println(botPrefix + "  " + task);
                } else if (command.startsWith("unmark ")) {
                    int taskNumber = Integer.parseInt(command.substring(7));
                    Task task = tasks[taskNumber - 1];
                    task.markAsNotDone();
                    System.out.println(botPrefix + "OK, I've marked this task as not done yet:");
                    System.out.println(botPrefix + "  " + task);
                } else {
                    tasks[numberOfTasks] = new Task(command);
                    numberOfTasks++;
                    System.out.println(botPrefix + "added: " + command);
                }
                System.out.println(divider);
            }
        }
    }
}
