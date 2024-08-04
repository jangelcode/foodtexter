package joeyTexts.texts;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.chat.v1.service.User;
import com.twilio.type.PhoneNumber;


public class Main {
    // Find your Account SID and Auth Token at twilio.com/console
    // and set the environment variables. See http://twil.io/secure
    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");

    public static void main(String[] args) throws InterruptedException {
        if (ACCOUNT_SID == null || AUTH_TOKEN == null) {
            System.err.println("Environment variables for Twilio are not set!");
            return;
        }
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String phoneNumber = "+14802951232";
//        Messages.signUp(phoneNumber);
        Message message = Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber("+17817347405"), // Your Twilio number
                "Test"
        ).create();
    }

}