package joeyTexts.Data;

import java.sql.*;

public class DataAnalyzer {

    private static final String URL = System.getenv("POSTGRES_URL");
    private static final String USER = System.getenv("POSTGRES_USER");
    private static final String PASSWORD = System.getenv("POSTGRES_PASSWORD");

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public String getWeeklyFoods(String phoneNumber) {
        String weeklyFoodsQuery = "Select * FROM foods WHERE recorded_at BETWEEN (NOW() AT TIME ZONE 'MST' - INTERVAL '7 days') AND NOW() AT TIME ZONE 'MST'";

        try (Connection connection = getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(weeklyFoodsQuery)) {

            // Set the phone number parameter in the SELECT statement
            selectStatement.setString(1, phoneNumber);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                // Check if the phone number exists in the database
                if (!resultSet.next()) {
                    // Set the phone number parameter in the INSERT statement and execute the update
                    selectStatement.setString(1, phoneNumber);
                    selectStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

