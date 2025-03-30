package data;

import model.User.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserDAO {

    public User validateLogin(String email, String password) {
        User user = null;
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT * FROM users WHERE email = ? AND PasswordHash = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Create user object if found
                user = new User(
                    rs.getInt("UserID"),
                    rs.getString("Name"),
                    rs.getString("Email"),
                    rs.getString("PasswordHash"),
                    rs.getInt("UserTypeID"),
                    rs.getTimestamp("CreatedAt")
                );
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


public boolean registerUser(User user) {
    boolean success = false;
    try {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "INSERT INTO Users (Name, Email, PasswordHash, UserTypeID) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, user.getName());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, user.getPasswordHash());
        stmt.setInt(4, user.getUserTypeId());

        System.out.println("Executing query: " + stmt.toString());

        int rowsInserted = stmt.executeUpdate();
        System.out.println("Rows inserted: " + rowsInserted);

        if (rowsInserted > 0) {
            success = true;
        }
        stmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return success;
}


public User getUserById(int userId){
    User user = null;
    try{
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            user = new User(
            rs.getInt("UserID"),
            rs.getString("Name"),
            rs.getString("Email"),
            rs.getString("PasswordHash"),
            rs.getInt("UserTypeID"),
            rs.getTimestamp("CreatedAt")
            );
        }
        rs.close();
        stmt.close();
    } catch (Exception e){
        e.printStackTrace();
    }
    return user;
}
public boolean isEmailRegistered(String email) {
    boolean exists = false;
    try {
        // Get database connection
        Connection conn = DatabaseConnection.getInstance().getConnection();

        // SQL query to check if email exists
        String sql = "SELECT 1 FROM Users WHERE Email = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);

        // Execute query
        ResultSet rs = stmt.executeQuery();

        // Check if any result was returned
        if (rs.next()) {
            exists = true; // Email already exists
        }

        // Close result set and statement
        rs.close();
        stmt.close();

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return exists;
}
}