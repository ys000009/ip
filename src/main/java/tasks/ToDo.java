package tasks;

public class ToDo extends Task {
    public ToDo(String name) {
        super(name);
    }

    public String toString() {
        return "[T]" + super.toString();
    }

    public String export() {
        return String.format(
            "[T] | %s | %s",
            this.isDone ? 1 : 0,
            this.name
        );
    }
}
