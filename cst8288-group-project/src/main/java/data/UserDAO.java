package data;

import model.User.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Data Access Object (DAO) for managing user-related operations in the database.
 * Provides methods for user login validation, registration, retrieval by ID, and email existence check.
 *
 * <p>This class interacts with the Users table in the database to perform authentication and user management tasks.</p>
 *
 * @author Zhennan Deng
 * @version 1.0
 * @since Java 1.21
 * @see model.User.User
 */
public class UserDAO {

    /**
     * Validates a user's login credentials.
     *
     * @param email    The user's email.
     * @param password The user's hashed password.
     * @return A {@link User} object if the credentials are valid, otherwise {@code null}.
     */
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

    /**
     * Registers a new user in the system.
     *
     * @param user The {@link User} object to be registered.
     * @return {@code true} if registration is successful, otherwise {@code false}.
     */
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

            success = rowsInserted > 0;
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * Retrieves a user by their user ID.
     *
     * @param userId The ID of the user to be fetched.
     * @return The {@link User} object if found, otherwise {@code null}.
     */
    public User getUserById(int userId) {
        User user = null;
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT * FROM Users WHERE UserID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
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

    /**
     * Checks if an email is already registered in the system.
     *
     * @param email The email to check.
     * @return {@code true} if the email exists, otherwise {@code false}.
     */
    public boolean isEmailRegistered(String email) {
        boolean exists = false;
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT 1 FROM Users WHERE Email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            exists = rs.next(); // Email exists if a result is returned

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
}
