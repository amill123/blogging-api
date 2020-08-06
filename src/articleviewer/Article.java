package articleviewer;

import java.util.List;

public class Article {
    private int id;
    private String title;
    private String content;
    private int author_id;
    private String author_fname;
    private String author_lname;
    private List<Comment> comments;

    public Article() {
    }

    public Article(int id, String title){
        this.id = id;
        this.title = title;
    }

    public Article(int id, String title, String content, int author_id) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author_id = author_id;
    }

    public Article(int id, String title, String content, int author_id, String author_fname, String author_lname) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author_id = author_id;
        this.author_fname = author_fname;
        this.author_lname = author_lname;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_fname() {
        return author_fname;
    }

    public void setAuthor_fname(String author_fname) {
        this.author_fname = author_fname;
    }

    public String getAuthor_lname() {
        return author_lname;
    }

    public void setAuthor_lname(String author_lname) {
        this.author_lname = author_lname;
    }
}
