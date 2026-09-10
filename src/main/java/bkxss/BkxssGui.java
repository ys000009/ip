package bkxss;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/** JavaFX user interface for the Bkxss task chatbot. */
public class BkxssGui extends Application {
    private final ArrayList<Task> tasks = new ArrayList<>();
    private final Storage storage = new Storage("data/bkxss.txt");
    private VBox conversation;
    private TextField commandInput;

    /** Builds and displays the chatbot window. */
    @Override
    public void start(Stage stage) {
        tasks.addAll(storage.load());
        conversation = new VBox(10);
        conversation.setPadding(new Insets(12));
        conversation.setStyle("-fx-background-color: transparent;");
        ScrollPane conversationScroll = new ScrollPane(conversation);
        conversationScroll.setFitToWidth(true);
        conversationScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        conversationScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        addBotMessage("Hello hello ~ This is Bkxss here ;)");
        addBotMessage("What can I do for you?");
        commandInput = new TextField();
        commandInput.setPromptText("Enter a command, e.g. todo borrow book");
        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> sendCommand());
        commandInput.setOnAction(event -> sendCommand());

        HBox inputBar = new HBox(8, commandInput, sendButton);
        inputBar.setPadding(new Insets(10));
        HBox.setHgrow(commandInput, Priority.ALWAYS);
        Label title = new Label("  Bkxss Task Assistant");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px;");
        BorderPane root = new BorderPane(conversationScroll, title, null, inputBar, null);
        Image backgroundImage = new Image(
                getClass().getResource("/images/chatbot_background.png").toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        root.setBackground(new Background(background));
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
        addUserMessage(command);
        String response = Bkxss.processCommand(command, tasks, storage);
        addBotMessage(response);
        commandInput.clear();
        if (command.equals("bye")) {
            commandInput.setDisable(true);
        }
    }

    /** Adds a user message aligned to the right side of the conversation. */
    private void addUserMessage(String message) {
        Label avatar = new Label("🥸");
        Label messageBubble = createMessageBubble(message, "#87ceeb");
        HBox messageRow = new HBox(8, messageBubble, avatar);
        messageRow.setAlignment(Pos.CENTER_RIGHT);
        conversation.getChildren().add(messageRow);
    }

    /** Adds a chatbot message aligned to the left side of the conversation. */
    private void addBotMessage(String message) {
        Label avatar = new Label("🤖");
        Label messageBubble = createMessageBubble(message, "white");
        HBox messageRow = new HBox(8, avatar, messageBubble);
        messageRow.setAlignment(Pos.CENTER_LEFT);
        conversation.getChildren().add(messageRow);
    }

    /** Creates a compact, wrapped message bubble with the requested background colour. */
    private Label createMessageBubble(String message, String backgroundColor) {
        Label messageBubble = new Label(message);
        messageBubble.setWrapText(true);
        messageBubble.setMaxWidth(460);
        messageBubble.setStyle("-fx-background-color: " + backgroundColor + ";"
                + "-fx-background-radius: 10; -fx-padding: 8 12 8 12;"
                + "-fx-text-fill: #1f2933;");
        return messageBubble;
    }
}
