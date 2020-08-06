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


@WebServlet(name = "likes", urlPatterns = {"/ajax/likes", "/ajax/v1/likes", "/ajax/v2/likes"})
public class LikesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try(Connection connection = DBConnectionUtils.getConnectionFromClasspath("connection.properties")) {

            if(req.getParameter("article") != null){
                int articleId = Integer.parseInt(req.getParameter("article"));
                List<Integer> userIds = LikesDAO.getArticleLikes(connection, articleId);
                JSONResponse.send(resp, userIds);
            } else if (req.getParameter("user") != null) {
                int userId = Integer.parseInt(req.getParameter("user"));
                List<Integer> articleIds = LikesDAO.getUserLikes(connection, userId);
                JSONResponse.send(resp, articleIds);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
