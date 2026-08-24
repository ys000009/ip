package tasks;

import java.time.LocalDateTime;

import util.DatetimeHelper;

public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;

    public Event(String name, LocalDateTime from, LocalDateTime to) {
        super(name);
        this.from = from;
        this.to = to;
    }

    public String toString() {
        return String.format(
                "[E]%s (from: %s to: %s)",
                super.toString(),
                this.from.format(DatetimeHelper.OUTPUT_FORMATTER),
                this.to.format(DatetimeHelper.OUTPUT_FORMATTER));
    }

    public String export() {
        return String.format(
                "E | %s | %s | %s | %s",
                this.isDone ? 1 : 0,
                this.name,
                this.from.format(DatetimeHelper.ISO_FORMATTER),
                this.to.format(DatetimeHelper.ISO_FORMATTER));
    }
}
