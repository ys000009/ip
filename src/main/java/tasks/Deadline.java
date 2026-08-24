package tasks;

import java.time.LocalDateTime;
import util.DatetimeHelper;

public class Deadline extends Task {
    private LocalDateTime deadline;

    public Deadline(String name, LocalDateTime deadline) {
        super(name);
        this.deadline = deadline;
    }

    public String toString() {
        return String.format(
                "[D]%s (by: %s)", super.toString(), this.deadline.format(DatetimeHelper.OUTPUT_FORMATTER));
    }

    public String export() {
        return String.format(
                "D | %s | %s | %s",
                this.isDone ? 1 : 0, this.name, this.deadline.format(DatetimeHelper.ISO_FORMATTER));
    }
}
