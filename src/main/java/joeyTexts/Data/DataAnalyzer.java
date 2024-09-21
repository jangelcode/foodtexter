package joeyTexts.Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                    foodsResult.append(resultSet.getString("food"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving foods: " + e.getMessage();
        }
        return foodsResult.toString();
    }

    public String analyzeWeeklyFoods(String phoneNumber, String weeklyFoods) {
        if (weeklyFoods.isEmpty()) {
            return "No food data available for the past week.";
        }

        // Split the foods by new line
        String[] foodsArray = weeklyFoods.split("\n");
        Map<String, Integer> foodCount = new HashMap<>();
        int totalFoods = 0;

        // Count the occurrences of each food
        for (String food : foodsArray) {
            food = food.trim(); // Remove any extra whitespace
            if (!food.isEmpty()) {
                foodCount.put(food, foodCount.getOrDefault(food, 0) + 1);
                totalFoods++;
            }
        }

        // Prepare the insights
        StringBuilder insights = new StringBuilder();
        insights.append("Total foods eaten in the past week: ").append(totalFoods).append("\n");

        if (!foodCount.isEmpty()) {
            // Sort the food items by frequency in descending order and get the top 3
            List<Map.Entry<String, Integer>> sortedFoodCount = new ArrayList<>(foodCount.entrySet());
            sortedFoodCount.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

            insights.append("Top 3 most frequently eaten foods:\n");
            for (int i = 0; i < Math.min(3, sortedFoodCount.size()); i++) {
                Map.Entry<String, Integer> entry = sortedFoodCount.get(i);
                insights.append(entry.getKey()).append("- ").append(entry.getValue()).append(" times\n");
            }
        } else {
            insights.append("No valid food data available.\n");
        }

        return insights.toString();
    }

    public static String analyzeEatingTimes(String phoneNumber) {
        String timeQuery = "SELECT EXTRACT(HOUR FROM recorded_at) AS hour, COUNT(*) AS count FROM foods WHERE phone = ? AND recorded_at BETWEEN (NOW() AT TIME ZONE 'MST' - INTERVAL '7 days') AND NOW() AT TIME ZONE 'MST' GROUP BY hour";
    
        StringBuilder eatingPattern = new StringBuilder();
        try (Connection connection = getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(timeQuery)) {
            
            selectStatement.setString(1, phoneNumber);
            
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                while (resultSet.next()) {
                    int hour = resultSet.getInt("hour");
                    int count = resultSet.getInt("count");
                    eatingPattern.append("Hour: ").append(hour).append(" - ").append(count).append(" meals\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
        return eatingPattern.toString();
    }
}


