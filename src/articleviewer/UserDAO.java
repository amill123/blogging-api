package articleviewer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public static List<User> getAllUsers(Connection connection) throws SQLException {
        List<User> users = new ArrayList<>();

        try(Statement statement = connection.createStatement()){

            try(ResultSet res = statement.executeQuery("SELECT * FROM users")){

                while(res.next()){
                    int userId = res.getInt(1);
                    String userName = res.getString(2);
                    User user = new User(userId, userName);
                    users.add(user);
                }
            }
        }
        return users;
    }

    public static User getUserById(Connection connection, int id) throws SQLException {
        User user;

        try(PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM users WHERE (id = ?)")){
                statement.setInt(1, id);
            try(ResultSet res = statement.executeQuery()) {
                int userId = 0;
                while(res.next()){
                    if(res.getInt(1)==id){
                        userId = res.getInt(1);
                        break;
                    }
                }
                if(userId <= 0){
                    return null;
                }
                user = new User(res.getInt(1),res.getString(2), res.getString(3), res.getString(4).charAt(0));

            }
        }
        return user;
    }


}
