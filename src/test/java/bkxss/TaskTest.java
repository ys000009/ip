package bkxss;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Tests the common completion behavior inherited by every task type. */
class TaskTest {
    @Test
    void task_newTask_isIncompleteWithBlankStatusIcon() {
        Todo todo = new Todo("read book");

        assertFalse(todo.isDone());
        assertEquals(" ", todo.getStatusIcon());
    }

    @Test
    void task_markAsDone_changesStatusToCompleted() {
        Todo todo = new Todo("read book");

        todo.markAsDone();

        assertTrue(todo.isDone());
        assertEquals("X", todo.getStatusIcon());
    }

    @Test
    void task_markAsNotDone_afterCompletion_changesStatusToIncomplete() {
        Todo todo = new Todo("read book");
        todo.markAsDone();

        todo.markAsNotDone();

        assertFalse(todo.isDone());
        assertEquals(" ", todo.getStatusIcon());
    }

    @Test
    void task_matchesKeyword_isCaseInsensitiveAndSearchesDescriptionOnly() {
        Todo todo = new Todo("Read a book");

        assertTrue(todo.matchesKeyword("BOOK"));
        assertFalse(todo.matchesKeyword("movie"));
    }
}
