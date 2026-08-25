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

    /**
     * Returns the string representation of the todo task, including its status
     * and description.
     *
     * @return formatted string representation of this todo task
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Formats the todo task as a string suitable for persistent storage export.
     *
     * @return pipe-delimited string representation of this todo task
     */
    @Override
    public String export() {
        return String.format(
                "T | %s | %s",
                this.isDone ? 1 : 0,
                this.name);
    }
}
