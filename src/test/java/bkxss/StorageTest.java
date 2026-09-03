package bkxss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/** Tests persistence of valid tasks and safe handling of invalid storage data. */
class StorageTest {
    @Test
    void storage_saveAndLoad_mixedTasks_preservesTypesValuesAndStatus() throws Exception {
        Path dataFile = Files.createTempDirectory("bkxss-storage-test").resolve("tasks.txt");
        Storage storage = new Storage(dataFile.toString());
        Todo todo = new Todo("read book");
        Deadline deadline = new Deadline("return book", LocalDateTime.of(2019, 12, 2, 18, 0));
        Event event = new Event("project meeting", "Mon 2pm", "4pm");
        todo.markAsDone();
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(todo);
        tasks.add(deadline);
        tasks.add(event);

        storage.save(tasks);
        ArrayList<Task> loaded = storage.load();

        assertEquals(3, loaded.size());
        assertTrue(loaded.get(0) instanceof Todo);
        assertEquals("[T][X] read book", loaded.get(0).toString());
        assertEquals("[D][ ] return book (by: Dec 02 2019 18:00)", loaded.get(1).toString());
        assertEquals("[E][ ] project meeting (from: Mon 2pm to: 4pm)", loaded.get(2).toString());
        assertFalse(loaded.get(1).isDone());
    }

    @Test
    void storage_load_missingFile_returnsEmptyList() throws Exception {
        Path missingFile = Files.createTempDirectory("bkxss-storage-test").resolve("missing.txt");

        assertTrue(new Storage(missingFile.toString()).load().isEmpty());
    }

    @Test
    void storage_load_invalidLines_ignoresInvalidEntriesAndKeepsValidEntries() throws Exception {
        Path dataFile = Files.createTempDirectory("bkxss-storage-test").resolve("tasks.txt");
        Files.writeString(dataFile, "T | 0 | valid task\ninvalid line\nE | 0 | meeting | missing separator\n");

        ArrayList<Task> loaded = new Storage(dataFile.toString()).load();

        assertEquals(1, loaded.size());
        assertEquals("[T][ ] valid task", loaded.get(0).toString());
    }
}
