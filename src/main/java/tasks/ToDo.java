package tasks;

public class ToDo extends Task {
    public ToDo(String name) {
        super(name);
    }

    public String getEntryString() {
        return "[T]" + super.toString();
    }
}
