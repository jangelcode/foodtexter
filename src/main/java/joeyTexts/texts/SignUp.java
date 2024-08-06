package joeyTexts.texts;

import joeyTexts.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUp {

    public static void signUp(String phoneNumber) {
        if (!isPhoneNumberInDatabase(phoneNumber)) {
            // Trigger the sign-up process
            System.out.println("Phone number not found. Triggering sign-up process.");
        } else {
            System.out.println("Phone number already exists in the database.");
        }
    }

    private static boolean isPhoneNumberInDatabase(String phoneNumber) {
        String query = "SELECT * FROM textlogs WHERE phone_number = " + phoneNumber;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, phoneNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//    public static void main(String[] args) {
//        signUp("1234567890");
//    }
}