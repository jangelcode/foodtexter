package joeyTexts.texts;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {

    @PostMapping("/sms")
    public String receiveSms(@RequestParam("From") String from,
                             @RequestParam("Body") String body) {
        // Process the incoming message
        System.out.println("Received message from " + from + ": " + body);

        // Create a response
        Body responseBody = new Body
                .Builder("Thank you for your message!")
                .build();
        Message responseMessage = new Message
                .Builder()
                .body(responseBody)
                .build();
        MessagingResponse response = new MessagingResponse
                .Builder()
                .message(responseMessage)
                .build();

        return response.toXml();
    }
}

