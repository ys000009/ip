package bob.task;

/**
 * Represents a todo task without any date or time attached.
 */
public class ToDo extends Task {

    /**
     * Constructs a ToDo task with the specified description.
     *
     * @param name the description of the todo task
     */
    public ToDo(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String export() {
        return String.format(
                "T | %s | %s",
                this.isDone ? 1 : 0,
                this.name);
    }
}
