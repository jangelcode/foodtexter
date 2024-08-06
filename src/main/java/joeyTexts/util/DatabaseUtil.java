package joeyTexts.util;

import java.sql.*;
import java.util.HashSet;

public class DatabaseUtil {

    private static final String URL = "jdbc:postgresql://cd1goc44htrmfn.cluster-czrs8kj4isg7.us-east-1.rds.amazonaws.com:5432/ddasd54vutrh71";
    private static final String USER = "u8u6n5jevrfgcr";
    private static final String PASSWORD = "p25123c225b25505c76201377b2de2e33120982e75f73c8454db8d4f3918017af";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void addNumberToDatabase(String phoneNumber) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Establish connection
            connection = getConnection();

            // Prepare the SELECT query
            String selectQuery = "SELECT * FROM phonenumbers WHERE phone = ?";
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, phoneNumber);
            resultSet = preparedStatement.executeQuery();


            // Check if a result was returned
            if (!resultSet.next()) {
                // Insert the phone number if it doesn't exist
                String insertQuery = "INSERT INTO phonenumbers (phone) VALUES (?)";
                preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, phoneNumber);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void storeFoodInDatabase(String food, String phoneNumber) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        HashSet<String> setOfFood = extractWords(food);
        if (setOfFood.isEmpty()){
            return;
        }

        try {
            connection = getConnection();
            String logFoodQuery = "INSERT INTO foods (phone, food, recorded_at) VALUES (?, ?, NOW())";
            preparedStatement = connection.prepareStatement(logFoodQuery);

            for(String foodItem : setOfFood) {
                preparedStatement.setString(1, phoneNumber);
                preparedStatement.setString(2, foodItem);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Proper error handling here
        } finally {
            // Close resources
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Proper error handling here
            }
        }
    }

    public static HashSet<String> extractWords(String text) {
        //split the text on commas, semicolons, or any whitespace characters
        String[] words = text.split("[,\n]+");

        // Create a HashSet to store the words without duplicates
        HashSet<String> wordSet = new HashSet<>();

        // Iterate over the array of words and add them to the HashSet
        for (String word : words) {
            if (!word.isEmpty()) { // Check to make sure the string is not empty
                wordSet.add(word);
            }
        }

        return wordSet;
    }

    public static String getDailyFood(String phoneNumber) {
        StringBuilder foodListBuilder = new StringBuilder();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            String queryUserDailyFoods = "SELECT food FROM foods WHERE phone = ? AND DATE(recorded_at) = CURRENT_DATE";
            preparedStatement = connection.prepareStatement(queryUserDailyFoods);
            preparedStatement.setString(1, phoneNumber);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String food = resultSet.getString("food");
                foodListBuilder.append(food).append("\n");
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Proper error handling should be implemented here
            }
        }
        if (!foodListBuilder.isEmpty()) {
            foodListBuilder.setLength(foodListBuilder.length() - 1);
        }
        else {
            return "No foods logged today.";
        }
        System.out.println(foodListBuilder.toString());

        return foodListBuilder.toString();
    }

    public static boolean deleteEntry(String phoneNumber, String foodToDelete){
        String query = "DELETE FROM foods WHERE food = (SELECT food FROM foods WHERE phone = ? AND LOWER(food) = ? ORDER BY recorded_at DESC LIMIT 1)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, phoneNumber);
            stmt.setString(2, foodToDelete.toLowerCase());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0){
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
