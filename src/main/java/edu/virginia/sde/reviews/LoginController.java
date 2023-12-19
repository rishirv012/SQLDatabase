package edu.virginia.sde.reviews;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;
    public static User currentUser;
    @FXML
    private Button loginButton;

    @FXML
    private Button createAccountButton;

    // Method to handle login button action
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (DatabaseHelper.validateUser(username, password)) {
            // User is valid, proceed to the Course Search Screen
            currentUser = new User(username, password);
            changeScene(event, "CourseSearch.fxml");
        } else {
            // User is invalid, show error message
            messageLabel.setText("Invalid username or password.");
        }
    }

    // Method to handle create account button action
    @FXML
    private void handleCreateAccountButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        System.out.println(username);

        String password = passwordField.getText();
        System.out.println(password);

        if (username.isEmpty() || password.length() < 8) {
            messageLabel.setText("Username cannot be empty and password must be at least 8 characters.");
            return;
        }
        if (DatabaseHelper.addUser(username, password)) {
            // Account created successfully
            messageLabel.setText("Account created. Please log in.");
            return;
        } else {
            // Account creation failed (possibly due to existing username)
            messageLabel.setText("Account creation failed. Username may already exist.");
        }
    }

    // Utility method to change scenes
    private void changeScene(ActionEvent event, String fxmlFile) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource(fxmlFile)));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}
