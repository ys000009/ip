package bkxss;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** JavaFX user interface for the Bkxss task chatbot. */
public class BkxssGui extends Application {
    private static final double AVATAR_SIZE = 42.0;
    private static final String USER_AVATAR_PATH = "/images/cat-avatar.png";
    private static final String BOT_AVATAR_PATH = "/images/dog-avatar.png";
    private final ArrayList<Task> tasks = new ArrayList<>();
    private final Storage storage = new Storage("data/bkxss.txt");
    private Image userAvatar;
    private Image botAvatar;
    private VBox conversation;
    private ScrollPane conversationScroll;
    private TextField commandInput;

    /** Builds and displays the chatbot window. */
    @Override
    public void start(Stage stage) {
        tasks.addAll(storage.load());
        userAvatar = loadImage(USER_AVATAR_PATH);
        botAvatar = loadImage(BOT_AVATAR_PATH);
        conversation = new VBox(16);
        conversation.getStyleClass().add("conversation");
        conversationScroll = new ScrollPane(conversation);
        conversationScroll.setFitToWidth(true);
        conversationScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        conversationScroll.getStyleClass().add("conversation-scroll");

        Label title = new Label("Bkxss");
        title.getStyleClass().add("app-title");
        Label subtitle = new Label("Your task butler, at your service!");
        subtitle.getStyleClass().add("muted-text");
        VBox header = new VBox(3, title, subtitle);
        header.getStyleClass().add("header");

        BorderPane root = new BorderPane(conversationScroll, header, null, createComposer(), null);
        root.getStyleClass().add("app-root");
        Scene scene = new Scene(root, 620, 560);
        scene.getStylesheets().add(getClass().getResource("/styles/bkxss.css").toExternalForm());
        addBotMessage(new CommandResult("Hello hello ~ This is Bkxss here ;)\n"
                + "What can I do for you?\n\nTry: todo borrow book\nUse list to see your tasks.", false));
        stage.setTitle("Bkxss · Task Assistant");
        stage.setMinWidth(360);
        stage.setMinHeight(360);
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
        commandInput.requestFocus();
    }

    /** Creates a keyboard-friendly input area with a persistent command hint. */
    private VBox createComposer() {
        commandInput = new TextField();
        commandInput.setPromptText("e.g. todo borrow book");
        commandInput.setAccessibleText("Command");
        commandInput.setMinWidth(0);
        commandInput.setOnAction(event -> sendCommand());
        Button sendButton = new Button("Send");
        sendButton.setMinWidth(Region.USE_PREF_SIZE);
        sendButton.setOnAction(event -> sendCommand());
        sendButton.disableProperty().bind(commandInput.disabledProperty().or(
                Bindings.createBooleanBinding(() -> commandInput.getText().isBlank(), commandInput.textProperty())));

        HBox inputBar = new HBox(10, commandInput, sendButton);
        inputBar.setAlignment(Pos.CENTER);
        HBox.setHgrow(commandInput, Priority.ALWAYS);
        Label hint = new Label("Enter to send · list to view tasks · bye to finish");
        hint.setWrapText(true);
        hint.getStyleClass().add("input-hint");
        VBox composer = new VBox(8, inputBar, hint);
        composer.getStyleClass().add("composer");
        return composer;
    }

    /** Sends a command, restores keyboard focus, and scrolls to the new reply after layout. */
    private void sendCommand() {
        String command = commandInput.getText().trim();
        if (commandInput.isDisabled() || command.isBlank()) {
            return;
        }
        addUserMessage(command);
        addBotMessage(Bkxss.processCommandResult(command, tasks, storage));
        commandInput.clear();
        if (command.equals("bye")) {
            commandInput.setDisable(true);
            commandInput.setPromptText("Session finished. Close the window to exit.");
        } else {
            commandInput.requestFocus();
        }
        Platform.runLater(() -> {
            conversationScroll.applyCss();
            conversationScroll.layout();
            conversationScroll.setVvalue(1.0);
        });
    }

    /** Adds a compact command bubble on the right, leaving most space for bot replies. */
    private void addUserMessage(String message) {
        Label command = createMessageLabel(message);
        command.getStyleClass().add("user-message");
        command.setAccessibleText("You: " + message);
        command.maxWidthProperty().bind(conversation.widthProperty().subtract(88).multiply(0.85));
        ImageView avatar = createAvatar(userAvatar, "User avatar: cat");
        HBox row = new HBox(10, command, avatar);
        row.setAlignment(Pos.CENTER_RIGHT);
        conversation.getChildren().add(row);
    }

    /** Adds a full-width response card; errors have both a text heading and a distinct color. */
    private void addBotMessage(CommandResult result) {
        Label heading = new Label(result.isError() ? "CHECK YOUR COMMAND" : "BKXSS");
        heading.getStyleClass().add("message-heading");
        Label message = createMessageLabel(result.message());
        message.setMaxWidth(Double.MAX_VALUE);
        VBox card = new VBox(6, heading, message);
        card.getStyleClass().add("bot-message");
        card.setMaxWidth(Double.MAX_VALUE);
        if (result.isError()) {
            card.getStyleClass().add("error-message");
        }
        ImageView avatar = createAvatar(botAvatar, "Bkxss avatar: dog");
        HBox row = new HBox(10, avatar, card);
        row.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(card, Priority.ALWAYS);
        conversation.getChildren().add(row);
    }

    /** Allows long replies to wrap and grow vertically even in a narrow window. */
    private Label createMessageLabel(String message) {
        Label label = new Label(message);
        label.setWrapText(true);
        label.setMinWidth(0);
        label.setMinHeight(Region.USE_PREF_SIZE);
        label.getStyleClass().add("message-text");
        return label;
    }

    /** Loads an image bundled with the application. */
    private Image loadImage(String resourcePath) {
        return new Image(getClass().getResource(resourcePath).toExternalForm());
    }

    /** Creates a small avatar view from a transparent circular image. */
    private ImageView createAvatar(Image image, String accessibleText) {
        ImageView avatar = new ImageView(image);
        avatar.setFitWidth(AVATAR_SIZE);
        avatar.setFitHeight(AVATAR_SIZE);
        avatar.setPreserveRatio(true);
        avatar.setSmooth(true);
        avatar.setAccessibleText(accessibleText);
        avatar.getStyleClass().add("avatar");
        return avatar;
    }
}
