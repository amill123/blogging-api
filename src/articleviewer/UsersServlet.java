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


@WebServlet(name = "users", urlPatterns = {"/ajax/users", "/ajax/v1/users", "/ajax/v2/users"})
public class UsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try(Connection connection = DBConnectionUtils.getConnectionFromClasspath("connection.properties")){

            if(req.getParameter("id") != null){
                int userId = Integer.parseInt(req.getParameter("id"));
                User user = UserDAO.getUserById(connection, userId);
                List<Article> articles = LikesDAO.getUserLikedArticles(connection, userId);
                user.setLikedArticles(articles);
                JSONResponse.send(resp,user);
            } else {
                List<User> users;
                users = UserDAO.getAllUsers(connection);
                JSONResponse.send(resp, users);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
