package commands;

public class ListCommand extends Command {
    public boolean processInput(String input) {
        if (input.equals("list")) {
            System.out.println("Tasks:");
            for (int i = 0; i < this.taskList.size(); i++) {
                System.out.println(String.format(
                    "%d: %s",
                    i + 1,
                    this.taskList.get(i).toString()
                ));
            }
            return true;
        }
        return false;
    }
}
