package tasks;

public class Deadline extends Task {
    private String deadline;

    public Deadline(String name, String deadline) {
        super(name);
        this.deadline = deadline;
    }

    public String toString() {
        return String.format(
            "[D]%s (by: %s)",
            super.toString(),
            this.deadline
        );
    }
}
