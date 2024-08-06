package joeyTexts.texts;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class SmsController {

    @GetMapping
    public String helloWeb(@RequestParam("From") String from) {
        return from;
    }

    @PostMapping(value = "/sendSMS", produces = "application/xml")
    public String receiveSms(@RequestBody String request) {
        Body body = new Body.Builder("The Robots are coming! Head for the hills!").build();
        Message sms = new Message.Builder().body(body).build();
        MessagingResponse twiml = new MessagingResponse.Builder().message(sms).build();
        System.out.println();
        return twiml.toXml();
    }
}


