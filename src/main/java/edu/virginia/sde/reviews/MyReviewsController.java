package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MyReviewsController {

    @FXML
    private ListView<Review> myReviewsListView;

    @FXML
    private Button backButton;

    private User currentUser; // The currently logged-in user

    // ObservableList to hold the user's reviews
    private ObservableList<Review> myReviews = FXCollections.observableArrayList();

    // Initialize method

    @FXML
    public void initialize() {
        System.out.println("__________");
        setCurrentUser(LoginController.currentUser);
        myReviewsListView.setItems(myReviews);
        myReviewsListView.setCellFactory(listView -> new ListCell<Review>() {
            private final Button deleteButton = new Button("Delete");
            private final HBox content = new HBox();
            private final Label text = new Label();

            {
                content.setSpacing(10);
                content.getChildren().addAll(text, deleteButton);
            }

            @Override
            protected void updateItem(Review review, boolean empty) {
                super.updateItem(review, empty);

                if (empty || review == null) {
                    setGraphic(null);
                } else {
                    Course course = DatabaseHelper.getCourseById(review.getCourseId()); // Retrieve course details
                    String courseMnemonic = course.getSubject() + " " + course.getNumber(); // Create course mnemonic
                    text.setText("Course: " + courseMnemonic + ", Rating: " + review.getRating());
                    deleteButton.setOnAction(event -> deleteReviewFromDatabase(review));
                    setGraphic(content);
                }
            }


            private void deleteReviewFromDatabase(Review review) {
                System.out.println("__________");

                if (DatabaseHelper.deleteReview(review)) {
                    System.out.println("222");
                    myReviews.remove(review); // Remove the review from the ListView

                } else {
                    System.out.println("111");
                    // Handle failure to delete review
                }
            }
        });
        myReviewsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleReviewSelection(newValue);
            }
        });

        loadUserReviews();
    }




    private void openCourseReviewScreen(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseReview.fxml"));
            Parent root = loader.load();

            // Get the controller and set the course
            CourseReviewController controller = loader.getController();
            controller.setCurrentCourse(course); // You need to implement this method in CourseReviewController

            // Show the new scene
            Scene scene = new Scene(root);
            Stage stage = (Stage) myReviewsListView.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }


    private void handleReviewSelection(Review selectedReview) {
        // Assuming you have a method to get a Course object by its ID
        Course course = DatabaseHelper.getCourseById(selectedReview.getCourseId());
        if (course != null) {
            openCourseReviewScreen(course);
        }
    }

    private void loadUserReviews() {
        myReviews.clear();
        if (currentUser != null) {
            myReviews.addAll(DatabaseHelper.getReviewsByUser(currentUser));
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        changeScene(event, "CourseSearch.fxml");
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadUserReviews(); // Load reviews when the current user is set
    }

    private void changeScene(ActionEvent event, String fxmlFile) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }
}
