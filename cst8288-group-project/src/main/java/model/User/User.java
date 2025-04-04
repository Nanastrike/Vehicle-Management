package model.User;

import java.sql.Timestamp;

/**
 * Represents a system user including attributes for authentication and role-based access control.
 * This model supports both operators and managers with user type distinction.
 * 
 * <p>Typical usage:</p>
 * <pre>{@code
 * User user = new User(1, "Alice", "alice@example.com", "hashedpassword", 1, new Timestamp(System.currentTimeMillis()));
 * if (user.isManager()) {
 *     // do manager stuff
 * }
 * }</pre>
 *
 * @author Zhennan Deng
 * @version 1.0
 * @since Java 1.21
 */
public class User {
    private int userId;
    private String name;
    private String email;
    private String passwordHash;
    private int userTypeId;
    private Timestamp createdAt;

    /**
     * Default constructor.
     */
    public User() {}

    /**
     * Constructor used for login validation.
     * 
     * @param email the user's email
     * @param passwordHash the hashed password
     */
    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    /**
     * Full constructor for all user fields.
     *
     * @param userId the user's ID
     * @param name the user's full name
     * @param email the user's email
     * @param passwordHash the user's hashed password
     * @param userTypeId the user type (e.g. 1 = Manager, 2 = Operator)
     * @param createdAt the account creation timestamp
     */
    public User(int userId, String name, String email, String passwordHash, int userTypeId, Timestamp createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.userTypeId = userTypeId;
        this.createdAt = createdAt;
    }

    // Getters and Setters

    /**
     * Gets the user ID.
     * @return userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     * @param userId the user's ID
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the user's name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's name.
     * @param name full name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the user's email.
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email.
     * @param email the user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's hashed password.
     * @return passwordHash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets the user's hashed password.
     * @param passwordHash the password hash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Gets the user type ID (1 = Manager, 2 = Operator).
     * @return userTypeId
     */
    public int getUserTypeId() {
        return userTypeId;
    }

    /**
     * Sets the user type ID.
     * @param userTypeId role type
     */
    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    /**
     * Gets the timestamp when the user was created.
     * @return createdAt timestamp
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the created timestamp.
     * @param createdAt creation timestamp
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Checks if the user is a manager.
     * @return true if userTypeId is 1
     */
    public boolean isManager() {
        return userTypeId == 1;
    }

    /**
     * Checks if the user is an operator.
     * @return true if userTypeId is 2
     */
    public boolean isOperator() {
        return userTypeId == 2;
    }
}
