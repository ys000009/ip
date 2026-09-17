package bkxss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests persistence of valid tasks and safe handling of invalid storage data. */
class StorageTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    void storage_load_corruptRecords_recoversLaterTasksAndProtectsOriginal() throws Exception {
        Path dataFile = temporaryDirectory.resolve("tasks.txt");
        String contents = "T | 0 | first\n"
                + "D | 0 | impossible date | Feb 30 2026 09:00\n"
                + "T | 2 | invalid status\n"
                + "T | 0 | \n"
                + "E | 0 | invalid date | 2026-02-30 0900 to 2026-03-01 1000\n"
                + "E | 0 | reversed | 2026-09-12 1000 to 2026-09-12 0900\n"
                + "E | 0 | equal | 2026-09-12 0900 to 2026-09-12 0900\n"
                + "E | 0 | missing end | Mon 2pm to \n"
                + "T | 1 | first\n"
                + "T | 0 | extra | unexpected\n"
                + "T | 1 | last\n";
        Files.writeString(dataFile, contents);
        Storage storage = new Storage(dataFile.toString());

        ArrayList<Task> loaded = storage.load();

        assertEquals(2, loaded.size());
        assertEquals("[T][ ] first", loaded.getFirst().toString());
        assertEquals("[T][X] last", loaded.getLast().toString());
        assertThrows(BkxssException.class, () -> storage.save(loaded));
        assertTrue(Bkxss.processCommandResult("delete 1", loaded, storage).isError());
        assertEquals(2, loaded.size());
        assertEquals(contents, Files.readString(dataFile));
        Files.writeString(dataFile, "T | 0 | repaired\n");
        ArrayList<Task> repairedTasks = storage.load();
        storage.save(repairedTasks);
        assertEquals(1, storage.load().size());
    }

    @Test
    void storage_load_invalidUtf8OrDirectory_blocksChanges() throws Exception {
        Path invalidEncoding = temporaryDirectory.resolve("invalid.txt");
        Files.write(invalidEncoding, new byte[]{(byte) 0xc3, (byte) 0x28});

        for (Path path : List.of(invalidEncoding, temporaryDirectory)) {
            Storage storage = new Storage(path.toString());
            assertTrue(storage.load().isEmpty());
            assertThrows(BkxssException.class, () -> storage.save(new ArrayList<>()));
        }
        assertEquals(2, Files.size(invalidEncoding));
    }

    @Test
    void storage_save_invalidRecord_doesNotTruncateExistingFileOrLeaveTemporaryFiles() throws Exception {
        Path dataFile = temporaryDirectory.resolve("tasks.txt");
        String contents = "T | 0 | original\n";
        Files.writeString(dataFile, contents);
        Storage storage = new Storage(dataFile.toString());
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("bad | record"));

        assertThrows(BkxssException.class, () -> storage.save(tasks));
        assertEquals(contents, Files.readString(dataFile));
        tasks.clear();
        tasks.add(new Todo("replacement"));
        storage.save(tasks);
        assertEquals("[T][ ] replacement", storage.load().getFirst().toString());
        try (var paths = Files.list(temporaryDirectory)) {
            assertEquals(1, paths.count());
        }
    }

    @Test
    void storage_save_readOnlyFile_preservesSavedTasks() throws Exception {
        Path dataFile = temporaryDirectory.resolve("tasks.txt");
        String contents = "T | 0 | original\n";
        Files.writeString(dataFile, contents);
        var originalPermissions = Files.getPosixFilePermissions(dataFile);
        Files.setPosixFilePermissions(dataFile, java.nio.file.attribute.PosixFilePermissions.fromString("r--r--r--"));
        try {
            Storage storage = new Storage(dataFile.toString());
            assertEquals(1, storage.load().size());
            assertThrows(BkxssException.class, () -> storage.save(new ArrayList<>()));
            assertEquals(contents, Files.readString(dataFile));
        } finally {
            Files.setPosixFilePermissions(dataFile, originalPermissions);
        }
    }

    @Test
    void storage_save_bareRelativeFilename_doesNotRequireExplicitParent() throws Exception {
        Path dataFile = Files.createTempFile(Path.of("."), "bkxss-relative-", ".txt");
        try {
            Storage storage = new Storage(dataFile.getFileName().toString());
            storage.save(new ArrayList<>(List.of(new Todo("relative path"))));
            assertEquals(1, storage.load().size());
        } finally {
            Files.deleteIfExists(dataFile);
        }
    }

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

    @Test
    void storage_saveAndLoad_datedEvent_preservesScheduleTimes() throws Exception {
        Path dataFile = Files.createTempDirectory("bkxss-storage-test").resolve("tasks.txt");
        Storage storage = new Storage(dataFile.toString());
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Event("lecture", "2026-09-12 0900", "2026-09-12 1100"));

        storage.save(tasks);
        Event loadedEvent = (Event) storage.load().get(0);

        assertTrue(loadedEvent.hasScheduledTimes());
        assertEquals(LocalDateTime.of(2026, 9, 12, 9, 0), loadedEvent.getFromDateTime().orElseThrow());
        assertEquals(LocalDateTime.of(2026, 9, 12, 11, 0), loadedEvent.getToDateTime().orElseThrow());
    }
}
