package bob.task;

/**
 * Represents a general task in the task list.
 */
public abstract class Task {
    protected String name;
    protected boolean isDone;

    /**
     * Constructs a Task with the specified description.
     *
     * @param name the description of the task
     */
    public Task(String name) {
        this.name = name;
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "[" + (this.isDone ? "X" : " ") + "] " + this.name;
    }

    /**
     * Marks the task as completed.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Marks the task as not completed.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Formats the task as a string suitable for persistent storage export.
     *
     * @return the exported string representation of the task
     */
    public abstract String export();
}
