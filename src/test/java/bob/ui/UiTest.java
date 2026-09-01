package bob.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.AbstractMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bob.task.Task;
import bob.task.TaskList;
import bob.task.ToDo;

/**
 * Unit tests for {@link Ui}.
 */
public class UiTest {
    private Ui ui;

    @BeforeEach
    public void setUp() {
        ui = new Ui();
    }

    @Test
    public void showWelcome_updatesLastResponse() {
        ui.showWelcome();
        assertEquals("Hello! I'm Bob.\nWhat can I do for you?", ui.getLastResponse());
    }

    @Test
    public void showGoodbye_updatesLastResponse() {
        ui.showGoodbye();
        assertEquals("Goodbye.", ui.getLastResponse());
    }

    @Test
    public void showTaskAdded_updatesLastResponse() {
        Task task = new ToDo("read book");
        ui.showTaskAdded(task, 1);
        assertEquals("Task added:\n[T][ ] read book\n1 item in list", ui.getLastResponse());

        ui.showTaskAdded(task, 2);
        assertEquals("Task added:\n[T][ ] read book\n2 items in list", ui.getLastResponse());
    }

    @Test
    public void showTaskDeleted_updatesLastResponse() {
        Task task = new ToDo("read book");
        ui.showTaskDeleted(task, 0);
        assertEquals("Removed: \n[T][ ] read book\n0 item in list", ui.getLastResponse());

        ui.showTaskDeleted(task, 3);
        assertEquals("Removed: \n[T][ ] read book\n3 items in list", ui.getLastResponse());
    }

    @Test
    public void showTaskMarked_updatesLastResponse() {
        Task task = new ToDo("read book");
        ui.showTaskMarked(task, true);
        assertEquals("Marked as done:\n [T][ ] read book", ui.getLastResponse());

        ui.showTaskMarked(task, false);
        assertEquals("Marked as not done:\n [T][ ] read book", ui.getLastResponse());
    }

    @Test
    public void showTaskList_updatesLastResponse() {
        TaskList tasks = new TaskList();
        tasks.add(new ToDo("task 1"));
        tasks.add(new ToDo("task 2"));
        ui.showTaskList(tasks);
        assertEquals("Tasks:\n1: [T][ ] task 1\n2: [T][ ] task 2", ui.getLastResponse());
    }

    @Test
    public void showMatchingTasks_updatesLastResponse() {
        Task task = new ToDo("task 1");
        ui.showMatchingTasks(List.of(new AbstractMap.SimpleEntry<>(1, task)));
        assertEquals("Here are the matching tasks in your list:\n1.[T][ ] task 1", ui.getLastResponse());
    }

    @Test
    public void showErrorAndMessage_updatesLastResponse() {
        ui.showError("Error occurred");
        assertEquals("Error occurred", ui.getLastResponse());

        ui.showMessage("Simple message");
        assertEquals("Simple message", ui.getLastResponse());
    }
}
