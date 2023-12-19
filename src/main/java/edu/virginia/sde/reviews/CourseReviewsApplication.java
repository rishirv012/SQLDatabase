package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CourseReviewsApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseHelper.initializeDatabase();
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml")); // Ensure Login.fxml is in the correct directory
        primaryStage.setTitle("Course Review Application");
        primaryStage.setScene(new Scene(root, 1280, 720)); // Set scene size to 1280x720
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
