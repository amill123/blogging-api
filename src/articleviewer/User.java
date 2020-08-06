package articleviewer;

import java.util.List;

public class User {
    private int id;
    private String fname;
    private String lname;
    private char gender;
    private List<Article> likedArticles;

    public User() {
    }

    public User(int id, String fname) {
        this.id = id;
        this.fname = fname;
    }

    public User(int id, String fname, String lname, char gender) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
    }

    public List<Article> getLikedArticles() {
        return likedArticles;
    }

    public void setLikedArticles(List<Article> likedArticles) {
        this.likedArticles = likedArticles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }
}
