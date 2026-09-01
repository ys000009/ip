package bob;

import bob.gui.Main;
import javafx.application.Application;

/**
 * A launcher class to workaround classpath and JavaFX module issues.
 */
public final class Launcher {

    private Launcher() {
    }

    /**
     * Launches the JavaFX graphical application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
