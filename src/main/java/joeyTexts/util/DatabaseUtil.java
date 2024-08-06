package joeyTexts.util;

import java.sql.*;

public class DatabaseUtil {

    private static final String URL = "jdbc:postgresql://cd1goc44htrmfn.cluster-czrs8kj4isg7.us-east-1.rds.amazonaws.com:5432/ddasd54vutrh71";
    private static final String USER = "u8u6n5jevrfgcr";
    private static final String PASSWORD = "p25123c225b25505c76201377b2de2e33120982e75f73c8454db8d4f3918017af";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void addNumberToDatabase(String phoneNumber) {
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
}
