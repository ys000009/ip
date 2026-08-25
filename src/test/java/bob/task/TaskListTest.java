package bob.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link TaskList}.
 */
public class TaskListTest {

    @Test
    public void defaultConstructor_createsEmptyList() {
        TaskList tasks = new TaskList();
        assertTrue(tasks.isEmpty());
        assertEquals(0, tasks.size());
    }

    @Test
    public void parameterizedConstructor_initializesWithTasks() {
        ArrayList<Task> list = new ArrayList<>();
        list.add(new ToDo("task 1"));
        list.add(new ToDo("task 2"));

        TaskList tasks = new TaskList(list);
        assertEquals(2, tasks.size());
        assertFalse(tasks.isEmpty());
        assertEquals("task 1", tasks.get(0).name);
    }

    @Test
    public void addAndGet_validTasks_storesAndRetrievesCorrectly() {
        TaskList tasks = new TaskList();
        Task todo = new ToDo("sample task");

        tasks.add(todo);

        assertEquals(1, tasks.size());
        assertEquals(todo, tasks.get(0));
    }

    @Test
    public void remove_validIndex_removesAndReturnsTask() {
        TaskList tasks = new TaskList();
        Task task1 = new ToDo("task 1");
        Task task2 = new ToDo("task 2");
        tasks.add(task1);
        tasks.add(task2);

        Task removed = tasks.remove(0);

        assertEquals(task1, removed);
        assertEquals(1, tasks.size());
        assertEquals(task2, tasks.get(0));
    }

    @Test
    public void getAndRemove_invalidIndex_throwsIndexOutOfBoundsException() {
        TaskList tasks = new TaskList();
        tasks.add(new ToDo("task 1"));

        assertThrows(IndexOutOfBoundsException.class, () -> tasks.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> tasks.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> tasks.remove(1));
        assertThrows(IndexOutOfBoundsException.class, () -> tasks.remove(-1));
    }

    @Test
    public void iterator_iteratesAllElements() {
        TaskList tasks = new TaskList();
        tasks.add(new ToDo("item 1"));
        tasks.add(new ToDo("item 2"));

        Iterator<Task> it = tasks.iterator();
        assertTrue(it.hasNext());
        assertEquals("item 1", it.next().name);
        assertTrue(it.hasNext());
        assertEquals("item 2", it.next().name);
        assertFalse(it.hasNext());
    }
}
