package articleviewer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticlesDAO {


    public static List<Article> getAllArticles(Connection connection) throws SQLException {
        List<Article> articles = new ArrayList<>();

        try(Statement statement = connection.createStatement()){

            try(ResultSet res = statement.executeQuery("SELECT * FROM articles")){

                while(res.next()){
                    Article article = createArticleFromResultSet(res);
                    article.setContent(article.getContent().substring(0,50));
                    articles.add(article);
                }
            }
        }

        return articles;
    }


    public static List<Article> getArticlesInRange(Connection connection, int count, int from) throws SQLException {
        List<Article> articles = new ArrayList<>();

        try(PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM articles, users WHERE (articles.author_id = users.id) AND(articles.id >= ?) LIMIT ?")){
            statement.setInt(1, from);
            statement.setInt(2, count);
            try(ResultSet res = statement.executeQuery()) {
                while (res.next()){
                    Article article = createArticleFromResultSet(res);
                    article.setAuthor_fname(res.getString(6));
                    article.setAuthor_lname(res.getString(7));
                    List<Comment> comments = CommentDAO.getAllCommentsByArticle(connection,article.getId());
                    article.setComments(comments);
                    articles.add(article);
                }
            }
        }
        return articles;
    }


    public static Article getArticleById(Connection connection, int id) throws SQLException {
        Article article;
        try(PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM articles, users WHERE (articles.id = ?) AND (articles.author_id = users.id)")){
            statement.setInt(1, id);
            try(ResultSet res = statement.executeQuery()) {
                int articleId = 0;
                while (res.next()){
                    if(res.getInt(1) == id){
                        articleId = res.getInt(1);
                        break;
                    }
                }
                if(articleId <= 0){
                    return null;
                }
                article = createArticleFromResultSet(res);
                article.setAuthor_fname(res.getString(6));
                article.setAuthor_lname(res.getString(7));
                List<Comment> comments =CommentDAO.getAllCommentsByArticle(connection,articleId);
                article.setComments(comments);
            }

        }
        return article;
    }

    public static List<Article> getArticleByAuthor(Connection connection, int authorId) throws SQLException {
        List<Article> articles = new ArrayList<>();

        try(PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM articles WHERE (author_id = ?)")){
            statement.setInt(1, authorId);
            try(ResultSet res = statement.executeQuery()){

                while(res.next()){
                    Article article = createArticleFromResultSet(res);
                    articles.add(article);
                }
            }
        }

        return  articles;
    }



    private static Article createArticleFromResultSet(ResultSet res) throws SQLException{
        Article article = new Article(
                res.getInt(1),
                res.getString(2),
                res.getString(3),
                res.getInt(4)
        );
        return article;
    }
}
