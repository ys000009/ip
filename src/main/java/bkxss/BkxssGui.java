package bkxss;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/** JavaFX user interface for the Bkxss task chatbot. */
public class BkxssGui extends Application {
    private final ArrayList<Task> tasks = new ArrayList<>();
    private final Storage storage = new Storage("data/bkxss.txt");
    private TextArea conversation;
    private TextField commandInput;

    /** Builds and displays the chatbot window. */
    @Override
    public void start(Stage stage) {
        tasks.addAll(storage.load());
        conversation = new TextArea("Bkxss: Hello hello ~ This is Bkxss here ;)\n"
                + "Bkxss: What can I do for you?\n");
        conversation.setEditable(false);
        conversation.setWrapText(true);
        commandInput = new TextField();
        commandInput.setPromptText("Enter a command, e.g. todo borrow book");
        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> sendCommand());
        commandInput.setOnAction(event -> sendCommand());

        HBox inputBar = new HBox(8, commandInput, sendButton);
        inputBar.setPadding(new Insets(10));
        HBox.setHgrow(commandInput, javafx.scene.layout.Priority.ALWAYS);
        BorderPane root = new BorderPane(conversation, new Label("  Bkxss Task Assistant"), null, inputBar, null);
        BorderPane.setMargin(conversation, new Insets(10));
        stage.setTitle("Bkxss");
        stage.setScene(new Scene(root, 620, 420));
        stage.show();
    }

    /** Sends the entered command to the existing chatbot logic. */
    private void sendCommand() {
        String command = commandInput.getText().trim();
        if (command.isBlank()) {
            return;
        }
        conversation.appendText("\nYou: " + command + "\n\n");
        String response = Bkxss.processCommand(command, tasks, storage);
        conversation.appendText("Bkxss: " + response + "\n");
        commandInput.clear();
        if (command.equals("bye")) {
            commandInput.setDisable(true);
        }
    }
}
