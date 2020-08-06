package articleviewer;

import articleviewer.util.DBConnectionUtils;
import articleviewer.util.JSONResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


@WebServlet(name="articles", urlPatterns = {"/ajax/articles", "/ajax/v1/articles", "/ajax/v2/articles"})
public class ArticlesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = DBConnectionUtils.getConnectionFromClasspath("connection.properties")){

            if(req.getParameter("id") != null){
                int articleId = Integer.parseInt(req.getParameter("id"));
                Article article = ArticlesDAO.getArticleById(connection, articleId);
                JSONResponse.send(resp, article);

            } else if (req.getParameter("from") != null){

                List<Article> articles;
                if(req.getParameter("count") != null){
                    articles = ArticlesDAO.getArticlesInRange(connection, Integer.parseInt(req.getParameter("count")), Integer.parseInt(req.getParameter("from")));
                } else {
                    int count = 5;
                    articles = ArticlesDAO.getArticlesInRange(connection, count, Integer.parseInt(req.getParameter("from")));
                }
                JSONResponse.send(resp, articles);
            } else if(req.getParameter("count") != null){
                int from = 0;
                List<Article> articles;
                articles = ArticlesDAO.getArticlesInRange(connection, Integer.parseInt(req.getParameter("count")), from);
                JSONResponse.send(resp, articles);
            } else if(req.getParameter("author_id") != null) {

                List<Article> articles  = ArticlesDAO.getArticleByAuthor(connection, Integer.parseInt(req.getParameter("author_id")));
                JSONResponse.send(resp, articles);
            } else {
                List<Article> articles = ArticlesDAO.getAllArticles(connection);
                JSONResponse.send(resp,articles);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
