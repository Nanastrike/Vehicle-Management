package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

/**
 * Singleton class responsible for managing a single database connection instance.
 * It loads connection properties from a `database.properties` file on the classpath.
 *
 * <p>This class ensures that only one connection is established and reused across the application.</p>
 *
 * @author Zhennan Deng
 * @version 1.0
 * @since Java 1.21
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    /**
     * Private constructor for Singleton. Loads properties, initializes the JDBC driver,
     * and establishes a database connection.
     */
    private DatabaseConnection() {
        try {
            System.out.println("Loading database properties...");

            // Load properties from database.properties file
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties");

            if (input == null) {
                throw new RuntimeException("Unable to find database.properties. Check the file location.");
            }

            props.load(input);

            String url = props.getProperty("jdbc.url");
            String user = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");

            System.out.println("Database URL: " + url);
            System.out.println("Database User: " + user);

            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver loaded successfully.");

            // Establish connection
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully!");

        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Include it in your library path.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database. Please check URL, username, or password.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to establish database connection.");
        }
    }

    /**
     * Returns the singleton instance of the DatabaseConnection class.
     *
     * @return the singleton instance
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Provides access to the established database connection.
     *
     * @return a {@link Connection} object
     */
    public Connection getConnection() {
        return connection;
    }
}
