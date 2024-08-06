package joeyTexts.texts;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class SMSController {

    DatabaseUtil dbUtil = new DatabaseUtil();
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
        if (dbUtil.isNumberInDatabase(from)){
            System.out.println("Already signed up");
        }
        else {
//            uncomment later
//            Messages.signUp(from);
            System.out.println("number not in database");
            dbUtil.addNumberIfNotExists(from);
            System.out.println("Number added to database");
        }
        //else don't reply
        System.out.println("here is the text");
        System.out.println(from);
        return "twiml.toXml()";
    }
}


