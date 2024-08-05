package joeyTexts.texts;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {

    @GetMapping(value = "/sendSMS")
    public ResponseEntity<String> sendSMS() {

        Twilio.init(Main.ACCOUNT_SID, Main.AUTH_TOKEN);

        Message.creator(
                new PhoneNumber("+14802951232"),
                new PhoneNumber("+17817347405"),
                "Hello from Twilio 📞").create();
        return new ResponseEntity<String>("Message sent successfully", HttpStatus.OK);
    }
}

