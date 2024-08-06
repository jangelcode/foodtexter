package joeyTexts.texts;

import joeyTexts.util.Cache;
import joeyTexts.util.DatabaseUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class SMSController {

    @PostMapping(value = "/sendSMS", produces = "application/xml")
    public String receiveSms(@RequestBody String request, @RequestParam("From") String from, @RequestParam("Body") String body) {
        //check if not in cache, and send sign up if not
        if (!Cache.checkInCache(from)) {
            Messages.signUp(from);
            Messages.sendTextMessage(from, "Got your number.");
            System.out.println("Number added to cache");
            System.out.println("Number added to database");
        }
        switch (body.toLowerCase()) {
            case "help":
                Messages.setSendHelpText(from);
                break;
            case "summary":
                Messages.sendFoodList(from);
                break;
            case "weekly":
                Messages.sendTextMessage(from, "Here is your weekly summary:");
                break;
        }
        return "twiml.toXml()";
    }
}
