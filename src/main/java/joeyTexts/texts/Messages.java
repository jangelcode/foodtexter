package joeyTexts.texts;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class Messages {
    public static String pleaseRepeatMessage = "Please repeat your message";
    public static String welcomeMessage = "Hi, welcome to the Food Texter! Whenever you eat something, please send us a text of what you ate.";
    public static String howToFormat = " Here's an example of how you can format that text, you can either send a message individually such as:";
    public static String apple = "apple";
    public static String howToFormat2 = "Or on separate lines like the below:";
    public static String foodList = "Apple \n Fries \n Hamburger \n Pepsi";
    public static String summaryMessage = "At the end of each day/week, we will provide you with insights on what you ate!";

    public static void signUp(String phoneNumber) {
        try {
            sendTextMessage(phoneNumber, welcomeMessage);
            sendTextMessage(phoneNumber, howToFormat);
            sendTextMessage(phoneNumber, apple);
            sendTextMessage(phoneNumber, howToFormat2);
            sendTextMessage(phoneNumber, foodList);
            sendTextMessage(phoneNumber, summaryMessage);
        } catch (Exception e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }

    private static void sendTextMessage(String to, String body) {
        Twilio.init(Main.ACCOUNT_SID, Main.AUTH_TOKEN);

        Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber("+17817347405"), // Your Twilio number
                body
        ).create();
        System.out.println("Message status: " + message.getStatus());
    }
}

