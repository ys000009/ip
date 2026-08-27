/**
 * A task that must be completed by a specified time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description text that describes the task
     * @param by deadline supplied by the user
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /** Returns the deadline text for persistence. */
    public String getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + description + " (by: " + by + ")";
    }
}
