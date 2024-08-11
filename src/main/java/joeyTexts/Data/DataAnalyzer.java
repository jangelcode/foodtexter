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
        String weeklyFoodsQuery = "Select food FROM foods WHERE phone = ? AND recorded_at BETWEEN (NOW() AT TIME ZONE 'MST' - INTERVAL '7 days') AND NOW() AT TIME ZONE 'MST'";
        StringBuilder foodsResult = new StringBuilder();

        try (Connection connection = getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(weeklyFoodsQuery)) {

            // Set the phone number parameter in the SELECT statement
            selectStatement.setString(1, phoneNumber);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                // Process the results
                while (resultSet.next()) {
                    if (foodsResult.length() > 0) {
                        foodsResult.append("\n");
                    }
                    foodsResult.append(resultSet.getString("food_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving foods: " + e.getMessage();
        }
        return foodsResult.toString();
    }
}


