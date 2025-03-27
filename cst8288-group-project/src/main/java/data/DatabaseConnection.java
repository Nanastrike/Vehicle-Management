package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            System.out.println("Loading database properties...");
            
            // Load properties from database.properties file
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties");

            // Check if the properties file is available
            if (input == null) {
                throw new RuntimeException("Unable to find database.properties. Check the file location.");
            }
            props.load(input);
            
            // Read property values
            String url = props.getProperty("jdbc.url");
            String user = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");

            System.out.println("Database URL: " + url);
            System.out.println("Database User: " + user);

            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver loaded successfully.");

            // Establish database connection
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

    // Singleton getInstance() method
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

    // Get database connection
    public Connection getConnection() {
        return connection;
    }
}
