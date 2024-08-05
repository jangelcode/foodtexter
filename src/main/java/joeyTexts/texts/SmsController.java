package joeyTexts.texts;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {

    @PostMapping(value = "/sendSMS")
    public ResponseEntity<String> sendSMS(
            @RequestParam("to") String toNumber,
            @RequestParam("from") String fromNumber,
            @RequestParam("message") String messageContent) {
        System.out.println(toNumber + " " + fromNumber + " " + messageContent);

        Twilio.init(Main.ACCOUNT_SID, Main.AUTH_TOKEN);

        Message message = Message.creator(
                new PhoneNumber(fromNumber), // Dynamic to number
                new PhoneNumber("+17817347405"), // Dynamic from number
                "Test").create(); // Dynamic message content

        return new ResponseEntity<>("Message sent successfully from " + fromNumber, HttpStatus.OK);
    }
}



