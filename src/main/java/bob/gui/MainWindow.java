package bob.gui;

import java.util.Objects;

import bob.Bob;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI view.
 */
public final class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Bob bob;

    private final Image userImage = new Image(Objects.requireNonNull(
            this.getClass().getResourceAsStream("/images/DaUser.png")));
    private final Image bobImage = new Image(Objects.requireNonNull(
            this.getClass().getResourceAsStream("/images/DaBob.png")));

    /**
     * Initializes the controller and binds scroll pane height.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Bob instance and displays the initial greeting.
     *
     * @param b the Bob instance to interact with
     */
    public void setBob(Bob b) {
        bob = b;
        dialogContainer.getChildren().add(
                DialogBox.getBobDialog(bob.getGreeting(), bobImage));
    }

    /**
     * Handles the user input event, generating Bob's response and updating the dialog container.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.trim().isEmpty()) {
            return;
        }

        String response = bob.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBobDialog(response, bobImage));
        userInput.clear();
    }
}
