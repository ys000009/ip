package storage;

import exceptions.BobException;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.ToDo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TaskStorage implements IStorage<ArrayList<Task>> {

    private static final String FILE_PATH = "./data/tasks.txt";

    private final Path path;

    public TaskStorage() {
        this.path = Paths.get(FILE_PATH);
    }

    @Override
    public ArrayList<Task> load() throws BobException {
        ArrayList<Task> tasks = new ArrayList<>();

        // No file on first startup -> return empty task list.
        if (!Files.exists(path)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                if (line.isBlank()) {
                    continue;
                }

                tasks.add(parseTask(line));
            }

        } catch (IOException e) {
            throw new BobException("I/O Error: Unable to load tasks from storage");
        }

        return tasks;
    }

    @Override
    public void save(ArrayList<Task> tasks) throws BobException {
        try {
            // Create ./data/ if it does not exist.
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            ArrayList<String> lines = new ArrayList<>();

            for (Task task : tasks) {
                lines.add(task.export());
            }

            Files.write(path, lines);

        } catch (IOException e) {
            throw new BobException("I/O Error: Unable to save tasks");
        }
    }

    private Task parseTask(String line) throws BobException {
        String[] parts = line.split(" \\| ");

        if (parts.length < 3) {
            throw new BobException("Error: Invalid task export format: " + line);
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String name = parts[2];

        Task task;

        switch (type) {
            case "T":
                task = new ToDo(name);
                break;

            case "D":
                if (parts.length != 4) {
                    throw new BobException("Error: Corrupted deadline format: " + line);
                }

                String deadline = parts[3];
                task = new Deadline(name, deadline);
                break;

            case "E":
                if (parts.length != 5) {
                    throw new BobException("Error: Corrupted event format: " + line);
                }

                String from = parts[3];
                String to = parts[4];

                task = new Event(name, from, to);
                break;

            default:
                throw new BobException("Error: Unknown task type of " + type);
            }

            if (isDone) {
                task.mark();
            }

            return task;
        }
}