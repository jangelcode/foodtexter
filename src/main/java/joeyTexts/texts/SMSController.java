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
            DatabaseUtil.addNumberToDatabase(from);
//            Messages.signUp(from);
            Messages.sendTextMessage(from, "Got your number.");
            System.out.println("Number added to cache");
            System.out.println("Number added to database");
        }
        switch (body.toLowerCase()) {
            case "commands":
                Messages.setSendHelpText(from);
                break;
            case "summary":
                Messages.sendFoodList(from);
                break;
            case "daily":
                Messages.sendTextMessage(from, "Here is your daily summary:");
                break;
            case "weekly":
                Messages.sendTextMessage(from, "Here is your weekly summary:");
                break;
            default:
                if (body.toLowerCase().startsWith("delete ")) {
                    String foodToDelete = body.substring(7).trim();
                    if (!foodToDelete.isEmpty()) {
                        // Call a method to delete the most recent instance of the specified food item
                        Messages.deleteEntry(from, foodToDelete);
                    }
                }
                else {
                    DatabaseUtil.storeFoodInDatabase(body, from);
                }
                break;
        }
        return "twiml.toXml()";
    }
}
