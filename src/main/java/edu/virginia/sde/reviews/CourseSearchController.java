package edu.virginia.sde.reviews;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class CourseSearchController {

    @FXML
    private TableView<Course> coursesTable;

    @FXML
    private TableColumn<Course, String> subjectColumn;

    @FXML
    private TableColumn<Course, Integer> numberColumn;

    @FXML
    private TableColumn<Course, String> titleColumn;

    @FXML
    private TableColumn<Course, Double> averageRatingColumn;

    @FXML
    private TextField searchSubjectField;

    @FXML
    private TextField searchNumberField;

    @FXML
    private TextField searchTitleField;

    @FXML
    private Button searchButton;

    @FXML
    private Button addCourseButton;

    @FXML
    private Button viewMyReviewsButton;

    @FXML
    private Button logoutButton;

    // ObservableList to hold courses data
    private ObservableList<Course> courses = FXCollections.observableArrayList();

    // Initialize method to set up the table columns
    @FXML
    public void initialize() {
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        averageRatingColumn.setCellValueFactory(new PropertyValueFactory<>("averageRating"));

        // Populate the table with data
        coursesTable.setItems(courses);
        loadCourses();
        // Add click event handler
        coursesTable.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2 && (!coursesTable.getSelectionModel().isEmpty())) {
                Course selectedCourse = coursesTable.getSelectionModel().getSelectedItem();
                handleCourseSelection(selectedCourse);
            }
        });
    }

    private void handleCourseSelection(Course selectedCourse) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseReview.fxml"));
            Scene scene = new Scene(loader.load());
            CourseReviewController controller = loader.getController();
            controller.setCurrentCourse(selectedCourse); // Set the course in the review controller

            Stage stage = (Stage) coursesTable.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // Existing changeScene method - This method can remain as is for other uses
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


    private void loadCourses() {
        courses.setAll(DatabaseHelper.getCourses());
    }


    // Method to handle search button action
    @FXML
    private void handleSearchButtonAction(ActionEvent event) {
        String subject = searchSubjectField.getText().toUpperCase(); // Convert to uppercase for consistency
        String number = searchNumberField.getText();
        String title = searchTitleField.getText().toLowerCase(); // Lowercase for case-insensitive comparison
        courses.setAll(DatabaseHelper.searchCourses(subject, number, title));
    }



    // Method to handle add course button action
    @FXML
    private void handleAddCourseButtonAction(ActionEvent event) {

        // Assume you have TextFields in your scene for subject, number, and title
        int id = Course.GlobalID;
        Course.GlobalID +=1;
        String subject = searchSubjectField.getText().toUpperCase(); // Example TextField for subject
        String number = searchNumberField.getText(); // Example TextField for number
        String title = searchTitleField.getText(); // Example TextField for title
        if (title.isEmpty()) {
            showAlert("Title too short", "Title must be at least 1 character");
            return;
        }

        if (!validateCourseInput(subject, Integer.parseInt(number), title)) {
            // Show error message
            return;
        }

        DatabaseHelper.addCourse(id, subject, Integer.parseInt(number), title);
        loadCourses();
    }

    private boolean validateCourseInput(String subject, int number, String title) {

        return subject.matches("[A-Z]{2,4}") && title.length() <= 50 && String.valueOf(number).matches("\\d{4}");
    }



    public void handleLogoutButtonAction(ActionEvent actionEvent) {
        // Change to the login scene
        changeScene(actionEvent, "Login.fxml");
    }



    public void handleViewMyReviewsButtonAction(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("MyReviews.fxml"));
            Parent userReviewsRoot = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Scene scene = new Scene(userReviewsRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


}
