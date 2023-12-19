package edu.virginia.sde.reviews;

import java.sql.Timestamp;

public class Review {
    private int courseId;
    private String userId;
    private int rating;
    private String comment;
    private Timestamp timestamp;
    private final String id;

    public Review(int courseId, String userId, int rating, String comment, Timestamp timestamp) {
        this.id = courseId + userId;
        this.courseId = courseId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Rating: " + rating + "/5, Comment: " + (comment.isEmpty() ? "No comment" : comment) + ", Date: " + timestamp.toString();
    }

}
