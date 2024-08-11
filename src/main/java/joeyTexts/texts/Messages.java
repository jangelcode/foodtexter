package joeyTexts.texts;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import joeyTexts.util.DatabaseUtil;

public class Messages {
    private final static String pleaseRepeatMessage = "Please repeat your message";
    private final static String welcomeMessage = "Hi, welcome to the Food Texter! Whenever you eat something, please send us a text of what you ate.";
    private final static String howToFormat = "Here's an example of how you can format that text, you can either send a message individually such as:";
    private final static String apple = "Apple";
    private final static String howToFormat2 = "Or on separate lines like the below:";
    private final static String foodList = "Apple\nFries\nHamburger\nPepsi";
    private final static String summaryMessage = "At the end of each day/week, we will provide you with insights on what you ate!";
    private final static String sendHelpText = "Text COMMANDS to see a list of commands.";
    private final static String listOfCommands = "List of Commands\nDaily: Sends the foods you've eaten today.\nWeekly: Sends the foods you've eaten this week\nDelete: deletes the most recent instance of a food you specify. ex: 'Delete Pizza'.";


    public static void signUp(String phoneNumber) {
        try {
            DatabaseUtil.addNumberToDatabase(phoneNumber);
            sendTextMessage(phoneNumber, welcomeMessage);
            sendTextMessage(phoneNumber, howToFormat);
            sendTextMessage(phoneNumber, apple);
            sendTextMessage(phoneNumber, howToFormat2);
            sendTextMessage(phoneNumber, foodList);
            sendTextMessage(phoneNumber, summaryMessage);
            sendTextMessage(phoneNumber, sendHelpText);
            sendTextMessage(phoneNumber, listOfCommands);
        } catch (Exception e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }

    public static void sendTextMessage(String to, String body) {
        Twilio.init(Main.ACCOUNT_SID, Main.AUTH_TOKEN);

        Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber("+17817347405"), // Your Twilio number
                body
        ).create();
        System.out.println("Message status: " + message.getStatus());
    }

    public static void setSendHelpText(String to){
        sendTextMessage(to, listOfCommands);
    }

    public static void sendFoodList(String to){
        sendTextMessage(to, DatabaseUtil.getDailyFood(to));
    }

    public static void deleteEntry(String to, String foodToDelete){
        boolean deleted = DatabaseUtil.deleteEntry(to, foodToDelete);
        if (deleted) {
            sendTextMessage(to, "The most recent instance of " + foodToDelete + " has been deleted.");
        } else {
            sendTextMessage(to, "No recent instance of " + foodToDelete + " found to delete.");
        }
    }

}

