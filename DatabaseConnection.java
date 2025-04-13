package MiniProject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Updated database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/DBSProject";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Establish connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            // Handle connection errors
            e.printStackTrace();
            System.out.println("Database connection failed!");
        }
        return connection;
    }

    public static void main(String[] args) {
        // Test the connection
        Connection connection = getConnection();
        if (connection != null) {
            System.out.println("Connection is ready for use.");
        }
    }
}