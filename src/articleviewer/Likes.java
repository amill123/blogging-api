package articleviewer;

public class Likes {
    private int articleId;
    private int userId;

    public Likes() {
    }

    public Likes(int articleId, int userId) {
        this.articleId = articleId;
        this.userId = userId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
