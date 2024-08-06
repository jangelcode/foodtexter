package joeyTexts.texts;

import java.sql.*;

public class DatabaseUtil {

    private static final String URL = "jdbc:postgresql://cd1goc44htrmfn.cluster-czrs8kj4isg7.us-east-1.rds.amazonaws.com:5432/ddasd54vutrh71";
    private static final String USER = "u8u6n5jevrfgcr";
    private static final String PASSWORD = "p25123c225b25505c76201377b2de2e33120982e75f73c8454db8d4f3918017af";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public boolean isNumberInDatabase(String phoneNumber) {
        int count = 0;
        boolean exists = false;
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

            // Execute the query
            resultSet = preparedStatement.executeQuery();

//            count = resultSet.getInt(1); // getInt(1) gets the first column of the result set
//            System.out.println("This is the count: " + count);

            // Check if a result was returned
            if (resultSet.next()) {
                exists = true;
            } else {
                // Insert the phone number if it doesn't exist
                String insertQuery = "INSERT INTO phonenumbers (phone) VALUES (?)";
                preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, phoneNumber);
                preparedStatement.executeUpdate();
                exists = false; // phone number was not in the database but now it is added
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
        return exists;
    }
}
