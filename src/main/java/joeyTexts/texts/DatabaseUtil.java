package joeyTexts.texts;

import java.sql.*;

public class DatabaseUtil {

    private static final String URL = "jdbc:postgresql://u8u6n5jevrfgcr:p25123c225b25505c76201377b2de2e33120982e75f73c8454db8d4f3918017af@cd1goc44htrmfn.cluster-czrs8kj4isg7.us-east-1.rds.amazonaws.com:5432/ddasd54vutrh71";
    private static final String USER = "u8u6n5jevrfgcr";
    private static final String PASSWORD = "p25123c225b25505c76201377b2de2e33120982e75f73c8454db8d4f3918017af";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public boolean isNumberInDatabase(String phoneNumber) {
        String query = "SELECT 1 FROM phonenumbers WHERE phoneNumber = " + phoneNumber;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, phoneNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // returns true if a row exists, false otherwise
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
