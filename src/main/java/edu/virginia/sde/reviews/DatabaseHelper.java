package edu.virginia.sde.reviews;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private static final String DATABASE_URL = "jdbc:sqlite:Database";

    // Establishes a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    // Validates user credentials
    public static boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            // Check if user exists
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void initializeDatabase() {
        // SQL statement for creating the users table
        String createUsersTableSql = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL);";

        // SQL statement for creating the courses table

        String createCoursesTableSql = "CREATE TABLE IF NOT EXISTS courses ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "subject TEXT NOT NULL, "
                + "number INTEGER NOT NULL, "
                + "title TEXT NOT NULL, "
                + "averageRating REAL);";

        String createReviewsTableSql = "CREATE TABLE IF NOT EXISTS reviews ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "review_id TEXT NOT NULL,"
                + "course_id INTEGER NOT NULL, "
                + "user_id TEXT NOT NULL, "
                + "rating INTEGER NOT NULL, "
                + "comment TEXT, "
                + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY (course_id) REFERENCES courses(id), "
                + "FOREIGN KEY (user_id) REFERENCES users(username));";


        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Execute the SQL statements to create new tables
            stmt.execute(createUsersTableSql);
            stmt.execute(createCoursesTableSql);
            stmt.execute(createReviewsTableSql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean deleteReview(Review review) {
        String sql = "DELETE FROM reviews WHERE review_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, review.getId());  // Using the generated ID

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Adds a new user to the database
    public static boolean addUser(String username, String password) {
        System.out.println(username);
        System.out.println(password);
        String checkUserSql = "SELECT * FROM users WHERE username = ?";
        String insertSql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkUserSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            int a = 1;
            //int a = insertStmt.executeUpdate();
            System.out.println("Affected rows: " + a);

            //ERROR IS RIGHT ABOVE HERE

            // Check if username already exists
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                // Username already exists
                return false;
            }

            // Insert new user
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            int affectedRows = insertStmt.executeUpdate();

            // Check if the insertion was successful
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void editReview(Review review) {
    }

    public static Course getCourseById(int courseId) {
        String sql = "SELECT * FROM courses WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Course(
                        rs.getInt("id"),
                        rs.getString("subject"),
                        rs.getInt("number"),
                        rs.getString("title"),
                        rs.getDouble("averageRating")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses"; // Adjust SQL based on your schema

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Course course = new Course(
                        rs.getInt("id"),
                        rs.getString("subject"),
                        rs.getInt("number"),
                        rs.getString("title"),
                        rs.getDouble("averageRating") // Adjust these according to your Course class
                );
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
    public static boolean addCourse(int id, String subject, int number, String title) {
        String sql = "INSERT INTO courses (id, subject, number, title) VALUES (?, ?, ?, ?)"; // Adjust SQL and parameters

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, subject);
            pstmt.setInt(3, number);
            pstmt.setString(4, title);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<Course> searchCourses(String subject, String number, String title) {
        List<Course> courses = new ArrayList<>();

        String sql = "SELECT * FROM courses WHERE " +
                "LOWER(subject) LIKE ? AND " +
                "CAST(number AS TEXT) LIKE ? AND " +
                "LOWER(title) LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameters for the query
            pstmt.setString(1, "%" + subject.toLowerCase() + "%");
            pstmt.setString(2, "%" + number + "%");
            pstmt.setString(3, "%" + title.toLowerCase() + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Course course = new Course(
                        rs.getInt("id"),
                        rs.getString("subject"),
                        rs.getInt("number"),
                        rs.getString("title"),
                        rs.getDouble("averageRating") // Adjust these according to your Course class
                );
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
    public static List<Review> getReviewsForCourse(Course course) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE course_id = ?"; // Ensure your table and column names match

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, course.getId());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Review review = new Review(
                        rs.getInt("course_id"),
                        rs.getString("user_id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getTimestamp("timestamp")
                );
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public static double calculateAverageRating(Course course) {
        String sql = "SELECT AVG(rating) AS averageRating FROM reviews WHERE course_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, course.getId());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("averageRating");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }




    public static boolean hasUserReviewedCourse(User user, Course course) {
        String sql = "SELECT * FROM reviews WHERE user_id = ? AND course_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setInt(2, course.getId());

            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static List<Review> getReviewsByUser(User user) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Review review = new Review(
                        rs.getInt("course_id"),
                        rs.getString("user_id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getTimestamp("timestamp")
                );
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }


    public static boolean addReview(Course course, User user, int rating, String comment, Timestamp timestamp) {
        if (hasUserReviewedCourse(user, course)) {
            // User has already reviewed the course, so do not allow another review
            return false;
        }
        String sql = "INSERT INTO reviews (review_id, course_id, user_id, rating, comment, timestamp) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getId() + user.getUsername());
            pstmt.setInt(2, course.getId());
            pstmt.setString(3, user.getUsername());
            pstmt.setInt(4, rating);
            pstmt.setString(5, comment);
            pstmt.setTimestamp(6, timestamp);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean updateReview(Course course, User user, int rating, String comment, Timestamp timestamp) {
        String sql = "UPDATE reviews SET rating = ?, comment = ?, timestamp = ? WHERE course_id = ? AND user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, rating);
            pstmt.setString(2, comment);
            pstmt.setTimestamp(3, timestamp);
            pstmt.setInt(4, course.getId());
            pstmt.setString(5, user.getUsername());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




}
