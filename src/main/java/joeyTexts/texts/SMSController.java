package joeyTexts.texts;

import joeyTexts.Data.DataAnalyzer;
import joeyTexts.util.Cache;
import joeyTexts.util.DatabaseUtil;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;

@RestController
@RequestMapping("/")
public class SMSController {

    DataAnalyzer dataAnalyzer = new DataAnalyzer();

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
        if (body.toLowerCase().contains("delete") || Cache.isDeleteMode(from)) {
            handleDeleteRequest(from, body.toLowerCase());
            return "twiml.toXml()";
        }
        switch (body.toLowerCase()) {
            case "commands":
                Messages.setSendHelpText(from);
                break;
            case "summary":
            case "daily":
                Messages.sendFoodList(from);
                break;
            case "weekly":
                Messages.sendTextMessage(from, dataAnalyzer.getWeeklyFoods(from));
                break;
            case "insights":
                Messages.sendTextMessage(from, dataAnalyzer.analyzeWeeklyFoods(from, dataAnalyzer.getWeeklyFoods(from)));
            case "times":
                Messages.analyzeEatingTimes(from);
            default:
                DatabaseUtil.storeFoodInDatabase(body, from);
                break;
        }
        return "twiml.toXml()";
    }

    private void handleDeleteRequest(String from, String body) {
        if (Cache.isDeleteMode(from)){
            Messages.deleteEntry(from, body);
            Cache.setDeleteModeFalse(from);
        }
        else if (body.startsWith("delete ")) {
            String foodToDelete = body.substring(7).trim();
            if (!foodToDelete.isEmpty()) {
                Messages.deleteEntry(from, foodToDelete);
            }
            Cache.setDeleteModeFalse(from);
        }
        else if (body.trim().equals("delete")){
            Messages.sendTextMessage(from, "Please text the food you would like to delete.");
            Cache.setDeleteMode(from);
        }
        else {
            Messages.sendTextMessage(from, "Invalid format. Please use the format 'delete [food name]'.");
        }
    }
}
