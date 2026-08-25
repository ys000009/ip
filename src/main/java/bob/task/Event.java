package bob.task;

import java.time.LocalDateTime;

import bob.util.DatetimeHelper;

/**
 * Represents an event task with a start time and an end time.
 */
public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;

    /**
     * Constructs an Event task with the specified description, start time, and end
     * time.
     *
     * @param name the description of the event task
     * @param from the start date/time
     * @param to   the end date/time
     */
    public Event(String name, LocalDateTime from, LocalDateTime to) {
        super(name);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the string representation of the event task, including its status,
     * description, and formatted start and end date/times.
     *
     * @return formatted string representation of this event task
     */
    @Override
    public String toString() {
        return String.format(
                "[E]%s (from: %s to: %s)",
                super.toString(),
                this.from.format(DatetimeHelper.OUTPUT_FORMATTER),
                this.to.format(DatetimeHelper.OUTPUT_FORMATTER));
    }

    /**
     * Formats the event task as a string suitable for persistent storage export.
     *
     * @return pipe-delimited string representation of this event task
     */
    @Override
    public String export() {
        return String.format(
                "E | %s | %s | %s | %s",
                this.isDone ? 1 : 0,
                this.name,
                this.from.format(DatetimeHelper.ISO_FORMATTER),
                this.to.format(DatetimeHelper.ISO_FORMATTER));
    }
}
