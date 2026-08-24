package tasks;

abstract public class Task {
    private String name;
    private boolean isDone;

    public Task(String name) {
        this.name = name;
        this.isDone = false;
    }

    public String toString() {
        return "[" + (this.isDone ? "X" : " ") + "] " + this.name;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    public abstract String export();
}
