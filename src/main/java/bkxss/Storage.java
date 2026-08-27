package bkxss;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Loads tasks from and saves tasks to the application's data file.
 */
public class Storage {
    private static final String BOT_PREFIX = "     ";
    private final Path dataFile;

    /** Creates storage backed by the given file path. */
    public Storage(String filePath) {
        this.dataFile = Path.of(filePath);
    }

    /** Loads all valid tasks from the data file, or an empty list if it is unavailable. */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(dataFile)) {
            return tasks;
        }
        try {
            for (String line : Files.readAllLines(dataFile, StandardCharsets.UTF_8)) {
                String[] fields = line.split("\\s*\\|\\s*", -1);
                if (fields.length < 3) {
                    continue;
                }
                Task task = parseTask(fields);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException | IllegalArgumentException exception) {
            System.out.println(BOT_PREFIX + "I couldn't load the saved tasks, so I'll start with an empty list.");
        }
        return tasks;
    }

    /** Saves all tasks, creating the parent directory when necessary. */
    public void save(ArrayList<Task> tasks) {
        try {
            Files.createDirectories(dataFile.getParent());
            ArrayList<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                if (task instanceof Todo) {
                    lines.add("T | " + status(task) + " | " + task.description);
                } else if (task instanceof Deadline deadline) {
                    lines.add("D | " + status(task) + " | " + task.description + " | "
                            + deadline.getFormattedBy());
                } else if (task instanceof Event event) {
                    lines.add("E | " + status(task) + " | " + task.description + " | "
                            + event.getFrom() + " to " + event.getTo());
                }
            }
            Files.write(dataFile, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.out.println(BOT_PREFIX + "I couldn't save your tasks: " + exception.getMessage());
        }
    }

    private static Task parseTask(String[] fields) {
        Task task;
        if (fields[0].equals("T") && fields.length == 3) {
            task = new Todo(fields[2]);
        } else if (fields[0].equals("D") && fields.length == 4) {
            task = new Deadline(fields[2], Deadline.parseFormattedBy(fields[3]));
        } else if (fields[0].equals("E") && fields.length == 4) {
            String[] times = fields[3].split(" to ", 2);
            if (times.length != 2) {
                return null;
            }
            task = new Event(fields[2], times[0], times[1]);
        } else {
            return null;
        }
        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    private static String status(Task task) {
        return task.isDone() ? "1" : "0";
    }
}
