package joeyTexts.texts;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.MediaType;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;

@RestController
public class ReceiveSMS {

    @PostMapping(value = "/sms", produces = MediaType.APPLICATION_XML_VALUE)
    public String receiveSms(@RequestParam("Body") String body) {
        Body messageBody = new Body.Builder(body).build();
        Message message = new Message.Builder()
                .body(messageBody)
                .build();

        MessagingResponse response = new MessagingResponse.Builder()
                .message(message)
                .build();

        return response.toXml();
    }
}


