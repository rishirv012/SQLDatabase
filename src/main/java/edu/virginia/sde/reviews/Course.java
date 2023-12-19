package edu.virginia.sde.reviews;
public class Course {
    private String subject;
    private int number;
    private String title;
    private double averageRating;
    private int id;
    public static int GlobalID= 0;

    public Course(int id, String subject, int number, String title, double averageRating) {
        this.id = id;
        this.subject = subject;
        this.number = number;
        this.title = title;
        this.averageRating = averageRating;
    }

    // Getters and setters
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAverageRating() {
        return averageRating;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    // Optionally override toString, equals, and hashCode
}
