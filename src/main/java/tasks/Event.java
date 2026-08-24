package tasks;

public class Event extends Task {
    private String from;
    private String to;

    public Event(String name, String from, String to) {
        super(name);
        this.from = from;
        this.to = to;
    }

    public String toString() {
        return String.format(
            "[D]%s (from: %s to: %s)",
            super.toString(),
            this.from, 
            this.to
        );
    }

    public String export() {
        return String.format(
            "D | %s | %s | %s | %s",
            this.isDone ? 1 : 0,
            this.name,
            this.from,
            this.to
        );
    }
}
