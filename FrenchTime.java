import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class FrenchTime {

    private static final Random rng = new Random();
    private static final List<Integer> hoursPool = new ArrayList<>();
    private static final List<Integer> minutesPool = new ArrayList<>();

    private static String[] frenchHours = {
        "minuit",
        "une heure",
        "deux heures",
        "trois heures",
        "quatre heures",
        "cinq heures",
        "six heures",
        "sept heures",
        "huit heures",
        "neuf heures",
        "dix heures",
        "onze heures",
        "midi",
        "treize heures",
        "quatorze heures",
        "quinze heures",
        "seize heures",
        "dix-sept heures",
        "dix-huit heures",
        "dix-neuf heures",
        "vingt heures",
        "vingt et un heures",
        "vingt-deux heures",
        "vingt-trois heures"
    };

    private static final String[] FRENCH_HALF_HOUR_FEM = { "trente", "et demie"};
    private static final String[] FRENCH_HALF_HOUR_MASC = { "trente", "et demi"};

    private static String[][] frenchMinutes;

    // private static final Map<Integer, String> additionalAnswers = new HashMap<>();

    private static int lastHour = -1;
    private static int lastMinute = -1;

    static {

        File answersFile = new File(System.getProperty("user.dir") + "/data/les-temps.txt");
        frenchMinutes = new String[60][];
        try (Scanner in = new Scanner(answersFile)) {
            for (int i = 0; i < 60; i++) {
                String line = in.nextLine();
                frenchMinutes[i] = line.split(",");
            }
        } catch (FileNotFoundException e) {

        }

        for (int hour = 0; hour < 24; hour++) {
            hoursPool.add(hour);
        }

        // increase odds of midnight/noon
        for (int i = 0; i < 5; i++) {
            hoursPool.add(0);
            hoursPool.add(12);
        }

        for (int minute = 0; minute < 60; minute++) {
            minutesPool.add(minute);
        }

        // increase odds of hour/15/30/45
        for (int i = 0; i < 15; i++) {
            minutesPool.add(0);
            minutesPool.add(15);
            minutesPool.add(30);
            minutesPool.add(45);
        }
    }

    public static FrenchTime generateRandomTime() {
        final int nextHour = hoursPool.remove(rng.nextInt(hoursPool.size()));
        final int nextMinute = minutesPool.remove(rng.nextInt(minutesPool.size()));
        
        if (lastHour >= 0) {
            hoursPool.add(lastHour);
            minutesPool.add(lastMinute);
        }

        lastHour = nextHour;
        lastMinute = nextMinute;
        return generate(nextHour, nextMinute);
    }

    public static FrenchTime generate(int hour, int minute) {
        String display = String.format("%d:%02d", hour, minute);
        String h = frenchHours[hour];
        String[] minuteOptions;
        if (minute == 30) {
            if (hour == 0 || hour == 12) {
                minuteOptions = FRENCH_HALF_HOUR_MASC;
            } else {
                minuteOptions = FRENCH_HALF_HOUR_FEM;
            }
        } else {
            minuteOptions = frenchMinutes[minute];
        }

        int numAnswers = minuteOptions.length;
        if (minute == 45) {
            numAnswers += 1;
        }
        String[] possibleAnswers = new String[numAnswers];
        if (minute == 45) {
            possibleAnswers[numAnswers - 1] = frenchHours[(hour + 1) % 24] + " moins le quart";
            numAnswers--;
        }
        for (int i = 0; i < numAnswers; i++) {
            if (minute == 0) {
                possibleAnswers[0] = h;
            } else {
                possibleAnswers[i] = h + " " + minuteOptions[i];
            }
        }
        return new FrenchTime(display, possibleAnswers);
    }

    private String display;
    private String[] answers;

    private FrenchTime(String display, String[] answers) {
        this.display = display;
        this.answers = answers;
    }

    public boolean checkAnswer(String answer) {
        for (String a : answers) {
            if (answer.equals(a)) {
                return true;
            }
        }
        return false;
    }

    public String getDisplay() {
        return display;
    }

    public String[] getPossibleAnswers() {
        return answers;
    }

}
