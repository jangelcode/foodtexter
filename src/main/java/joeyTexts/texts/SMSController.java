package joeyTexts.texts;

import joeyTexts.util.Cache;
import joeyTexts.util.DatabaseUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class SMSController {

    DatabaseUtil dbUtil = new DatabaseUtil();

    @PostMapping(value = "/sendSMS", produces = "application/xml")
    public String receiveSms(@RequestBody String request, @RequestParam("From") String from) {
        //check if not in cache, and send sign up if not
        if (!Cache.checkInCache(from)){
            Messages.signUp(from);
            System.out.println("Number added to cache");
        }
        else {
            Messages.sendTextMessage(from, "Confirmed");
        }
        return "twiml.toXml()";
    }
}

//    @GetMapping(value = "/sendSMS")
//    public String helloWeb(@RequestParam("From") String from) {
//        System.out.println(from);
//        return receiveSms();
//    }

//        Body body = new Body.Builder("The Robots are coming! Head for the hills!").build();
//        Message sms = new Message.Builder().body(body).build();
//        MessagingResponse twiml = new MessagingResponse.Builder().message(sms).build();
