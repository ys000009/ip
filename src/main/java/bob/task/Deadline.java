package bob.task;

import java.time.LocalDateTime;

import bob.util.DatetimeHelper;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
    private LocalDateTime deadline;

    /**
     * Constructs a Deadline task with the specified description and due date/time.
     *
     * @param name     the description of the deadline task
     * @param deadline the due date/time
     */
    public Deadline(String name, LocalDateTime deadline) {
        super(name);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return String.format(
                "[D]%s (by: %s)", super.toString(), this.deadline.format(DatetimeHelper.OUTPUT_FORMATTER));
    }

    @Override
    public String export() {
        return String.format(
                "D | %s | %s | %s",
                this.isDone ? 1 : 0, this.name, this.deadline.format(DatetimeHelper.ISO_FORMATTER));
    }
}
