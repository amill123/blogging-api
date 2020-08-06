package articleviewer;

public class Comment {

    private int id;
    private String content;
    private String date;
    private int author_id;
    private int article_id;
    private String fullName;

    public Comment() {
    }

    public Comment(int id, String content, int author_id, int article_id) {
        this.id = id;
        this.content = content;
        this.author_id = author_id;
        this.article_id = article_id;
    }

    public Comment(int id, String content, String date, int author_id, int article_id) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.author_id = author_id;
        this.article_id = article_id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }
}
