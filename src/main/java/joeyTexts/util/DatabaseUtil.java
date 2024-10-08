package joeyTexts.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class DatabaseUtil {

    private static final String URL = System.getenv("POSTGRES_URL");
    private static final String USER = System.getenv("POSTGRES_USER");
    private static final String PASSWORD = System.getenv("POSTGRES_PASSWORD");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void addNumberToDatabase(String phoneNumber) {
        String selectQuery = "SELECT * FROM phonenumbers WHERE phone = ?";
        String insertQuery = "INSERT INTO phonenumbers (phone) VALUES (?)";

        try (Connection connection = getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {

            // Set the phone number parameter in the SELECT statement
            selectStatement.setString(1, phoneNumber);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                // Check if the phone number exists in the database
                if (!resultSet.next()) {
                    // Set the phone number parameter in the INSERT statement and execute the update
                    insertStatement.setString(1, phoneNumber);
                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void storeFoodInDatabase(String food, String phoneNumber) {
        String logFoodQuery = "INSERT INTO foods (phone, food, recorded_at) VALUES (?, ?, NOW() AT TIME ZONE 'MST' AT TIME ZONE 'UTC')";
        ArrayList<String> arrayOfFood = extractWords(food);
        if (arrayOfFood.isEmpty()) {
            return;
        }

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(logFoodQuery)) {
            for (String foodItem : arrayOfFood) {
                preparedStatement.setString(1, phoneNumber);
                preparedStatement.setString(2, foodItem);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Proper error handling here
        }
    }

    public static ArrayList<String> extractWords(String text) {
        //split the text on commas, semicolons, or any whitespace characters
        String[] words = text.split("[,\n]+");
        return new ArrayList<>(Arrays.asList(words));
    }

    public static String getDailyFood(String phoneNumber) {
        StringBuilder foodListBuilder = new StringBuilder();
        String queryUserDailyFoods = "SELECT food FROM foods WHERE phone = ? AND DATE(recorded_at AT TIME ZONE 'UTC' AT TIME ZONE 'MST') = (CURRENT_DATE AT TIME ZONE 'MST')::date";
        ResultSet resultSet = null;

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(queryUserDailyFoods)) {
            preparedStatement.setString(1, phoneNumber);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String food = resultSet.getString("food");
                foodListBuilder.append(food).append("\n");
            }
        } catch (SQLException e){
            e.printStackTrace();
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
        String query = "DELETE FROM foods WHERE phone = ? AND LOWER(food) = ? AND recorded_at = ("
                + "SELECT recorded_at FROM foods WHERE phone = ? AND LOWER(food) = ? "
                + "ORDER BY recorded_at DESC LIMIT 1)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, foodToDelete.toLowerCase());
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setString(4, foodToDelete.toLowerCase());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0){
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static HashMap<String, Boolean> retrievePhoneNumbers() {
        ResultSet resultSet = null;
        HashMap<String, Boolean> phoneNumbers = new HashMap<>();
        String distinctPhoneNumbers = "SELECT DISTINCT phone FROM phonenumbers";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(distinctPhoneNumbers)) {
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Add each phone number to the map with a default delete mode status of false
                phoneNumbers.put(resultSet.getString("phone"), false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phoneNumbers;
    }

}
