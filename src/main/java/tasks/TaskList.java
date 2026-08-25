package tasks;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a list of tasks and provides operations to manipulate the tasks.
 */
public class TaskList implements Iterable<Task> {
    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList containing the tasks from the specified list.
     *
     * @param tasks the initial list of tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
    }

    /**
     * Adds a task to the list.
     *
     * @param task the task to be added
     */
    public void add(Task task) {
        this.tasks.add(task);
    }

    /**
     * Retrieves the task at the specified index.
     *
     * @param index the index of the task to return
     * @return the task at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Task get(int index) {
        return this.tasks.get(index);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index the index of the task to be removed
     * @return the removed task
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Task remove(int index) {
        return this.tasks.remove(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the number of tasks
     */
    public int size() {
        return this.tasks.size();
    }

    /**
     * Checks if the task list is empty.
     *
     * @return true if the list contains no tasks, false otherwise
     */
    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }

    @Override
    public Iterator<Task> iterator() {
        return this.tasks.iterator();
    }
}
