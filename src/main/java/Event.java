/**
 * A task that occurs during a specified time period.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an incomplete event task.
     *
     * @param description text that describes the event
     * @param from event start time supplied by the user
     * @param to event end time supplied by the user
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + description
                + " (from: " + from + " to: " + to + ")";
    }
}
