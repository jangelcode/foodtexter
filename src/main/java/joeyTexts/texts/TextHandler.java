package joeyTexts.texts;

public class TextHandler {

    public static void checkIfNewNumber() {

    }

    public String generateSummary(String duration) {
        if (duration.equalsIgnoreCase("weekly")){
            return SummaryGenerator.generateWeeklySummary();
        } else if (duration.equalsIgnoreCase("daily")){
            return SummaryGenerator.generateDailySummary();
        }
        else {
            return Messages.pleaseRepeatMessage;
        }
    }

}
