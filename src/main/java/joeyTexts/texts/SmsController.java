package joeyTexts.texts;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class SmsController {

//    @GetMapping(value = "/sendSMS")
//    public String helloWeb(@RequestParam("From") String from) {
//        System.out.println(from);
//        return receiveSms();
//    }

    @PostMapping(value = "/sendSMS", produces = "application/xml")
    public String receiveSms(@RequestBody String request, @RequestParam("From") String from) {
//        Body body = new Body.Builder("The Robots are coming! Head for the hills!").build();
//        Message sms = new Message.Builder().body(body).build();
//        MessagingResponse twiml = new MessagingResponse.Builder().message(sms).build();
        Messages.signUp(from);
        System.out.println("here is the text");
        System.out.println(from);
        return "twiml.toXml()";
    }
}


