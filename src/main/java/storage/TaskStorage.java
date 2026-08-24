package storage;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import exceptions.BobException;
import tasks.Task;



public class TaskStorage implements IStorage<ArrayList<Task>> {

    private static final String FILE_PATH = "./data/tasks.txt";

    @Override
    public ArrayList<Task> load() throws BobException {
        // Read file and reconstruct Tasks
    }

    @Override
    public void save(ArrayList<Task> tasks) throws BobException {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            ArrayList<String> lines = new ArrayList<>();

            for (Task task : tasks) {
                lines.add(task.export());
            }

            Files.write(path, lines);

        } catch (IOException e) {
            throw new BobException("Unable to save tasks.");
        }
    }
}