package bob.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

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
        assertEquals("[T][ ] item 1", it.next().toString());
        assertTrue(it.hasNext());
        assertEquals("[T][ ] item 2", it.next().toString());
        assertFalse(it.hasNext());
    }

    @Test
    public void find_matchingAndNonMatchingKeyword_returnsFilteredTaskList() {
        TaskList tasks = new TaskList();
        tasks.add(new ToDo("read book"));
        tasks.add(new ToDo("return book"));
        tasks.add(new ToDo("buy groceries"));

        TaskList matching = tasks.find("book");
        assertEquals(2, matching.size());
        assertEquals("[T][ ] read book", matching.get(0).toString());
        assertEquals("[T][ ] return book", matching.get(1).toString());

        TaskList caseInsensitive = tasks.find("BOOK");
        assertEquals(2, caseInsensitive.size());

        TaskList none = tasks.find("swimming");
        assertEquals(0, none.size());
        assertTrue(none.isEmpty());

        TaskList emptyKeyword = tasks.find("");
        assertEquals(0, emptyKeyword.size());
    }
}
