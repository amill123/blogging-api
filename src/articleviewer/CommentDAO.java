package articleviewer;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    public static List<Comment> getAllCommentsByArticle(Connection connection, int articleId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM comments WHERE (article_id = ?)")){
            statement.setInt(1, articleId);
            try(ResultSet res = statement.executeQuery()){

                while(res.next()){
                    Comment comment = createCommentFromResultSet(res);
                    try(PreparedStatement statementQuery = connection.prepareStatement(
                            "SELECT fname, lname FROM users WHERE (id = ?)")){
                        statementQuery.setInt(1,comment.getAuthor_id());
                        try(ResultSet result = statementQuery.executeQuery()){
                            while(result.next()){
                                String fullName = result.getString(1) + " " + result.getString(2);
                                comment.setFullName(fullName);
                            }
                        }
                    }
                    comments.add(comment);
                }
            }
        }
        return comments;
    }

    public static List<Comment> getAllCommentsByAuthor(Connection connection, int authorId) throws SQLException {
        List<Comment> comments = new ArrayList<>();

        try(PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM comments WHERE (author_id = ?)")){
            statement.setInt(1, authorId);

            try(ResultSet res = statement.executeQuery()){

                while(res.next()){

                }
            }
        }
        return comments;
    }

    private static Comment createCommentFromResultSet(ResultSet res) throws SQLException {
        int id = res.getInt(1);
        String content = res.getString(2);
        String date = res.getString(3);
        int authorId = res.getInt(4);
        int articleId = res.getInt(5);
        Comment comment = new Comment(id, content, date, authorId, articleId);
        return comment;
    }
}
