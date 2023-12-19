package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;

public class CourseReviewController {

    @FXML
    private ListView<Review> reviewsListView;

    @FXML
    private TextField ratingField;

    @FXML
    private TextArea commentArea;

    @FXML
    private Label courseDetailsLabel;

    @FXML
    private Label averageRatingLabel;

    @FXML
    private Button submitReviewButton;

    @FXML
    private Button backButton;

    private Course currentCourse; // The course for which reviews are being shown
    private User currentUser;  // The currently logged-in user

    // ObservableList to hold reviews data
    private ObservableList<Review> reviews = FXCollections.observableArrayList();

    // Method to initialize the controller
    @FXML
    public void initialize() {

        setCurrentUser(LoginController.currentUser);
        reviewsListView.setItems(reviews);

        //loadReviews();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setCurrentCourse(Course course) {
        this.currentCourse = course;
        updateCourseDetails();
        loadReviews();
        updateAverageRating();
    }

    private void updateCourseDetails() {

        courseDetailsLabel.setText(currentCourse.getId() + " " + currentCourse.getSubject() + " "
                + currentCourse.getNumber() + ": " + currentCourse.getTitle());

        // Update average rating
        double averageRating = currentCourse.getAverageRating();
        averageRatingLabel.setText(String.format("%.2f", averageRating));
        updateAverageRating();
    }

    private void loadReviews() {
        reviews.clear();
        reviews.addAll(DatabaseHelper.getReviewsForCourse(currentCourse));
    }

    @FXML
    private void handleSubmitReviewButtonAction(ActionEvent event) {
        if (DatabaseHelper.hasUserReviewedCourse(currentUser, currentCourse)) {
            showAlert("Review Already Submitted", "You have already reviewed this course.");
            return;
        }
        try {
            int rating = Integer.parseInt(ratingField.getText());
            String comment = commentArea.getText();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            if (DatabaseHelper.addReview(currentCourse, currentUser, rating, comment, timestamp)) {
                // Refresh the reviews list and update average rating
                loadReviews();
                updateAverageRating();
            } else {
                // Handle the case when the review could not be added
            }
        } catch (NumberFormatException e) {
            // Handle invalid rating input
        }
    }

    private void updateAverageRating() {
        // Assume a method in DatabaseHelper to calculate the average rating
        double newAverageRating = DatabaseHelper.calculateAverageRating(currentCourse);
        currentCourse.setAverageRating(newAverageRating);
        averageRatingLabel.setText(String.format("%.2f", newAverageRating));
    }


    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            // Load the FXML file for the Course Search screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseSearch.fxml"));
            Parent courseSearchRoot = loader.load();

            // Get the current stage (window) from the event source (the Back button)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene to the Course Search screen
            Scene scene = new Scene(courseSearchRoot);
            stage.setScene(scene);

            // Optionally, set the title of the stage, if necessary
            // stage.setTitle("Course Search");

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, possibly by showing an error message to the user
        }
    }


    // Setter for currentUser
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }



}
