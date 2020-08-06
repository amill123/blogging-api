package articleviewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LikesDAO {

    public static List<Integer> getArticleLikes(Connection connection, int id) throws SQLException {
        List<Integer> userIds = new ArrayList<>();

        try(PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM likes WHERE article_id = ?")){
            statement.setInt(1, id);
            try(ResultSet res = statement.executeQuery()){
                while(res.next()){
                    userIds.add(res.getInt(2));
                }
            }
        }
        return userIds;
    }

    //This method is only used by V0 and V1 - I've left it here so that the V0 and V1 pages still work otherwise it should be deleted
    public static List<Integer> getUserLikes(Connection connection, int id) throws SQLException {
        List<Integer> articleIds = new ArrayList<>();

        try(PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM likes WHERE user_id = ?")){
            statement.setInt(1, id);
            try(ResultSet res = statement.executeQuery()){
                while(res.next()){
                    articleIds.add(res.getInt(1));
                }
            }
        }
        return articleIds;
    }

    //We'll return the article name and article id. Presently the only things required to be displayed is a list of liked article names,
    // if we pair the article name with the article id, perhaps in the future the list may be clickable to take the user to the article.
    public static List<Article> getUserLikedArticles(Connection connection, int authorId) throws SQLException {
        List<Article> articles = new ArrayList<>();

        try(PreparedStatement statement = connection.prepareStatement(
                "SELECT articles.id,articles.title FROM likes, articles WHERE (user_id = ?) AND (likes.article_id = articles.id)")) {
            statement.setInt(1, authorId);

            try(ResultSet res = statement.executeQuery()) {
                while(res.next()){
                    Article article = new Article(res.getInt(1), res.getString(2));
                    articles.add(article);
                }
            }
        }
        return articles;
    }

}
