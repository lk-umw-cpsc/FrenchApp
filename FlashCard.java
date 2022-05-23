import java.time.LocalDate;

public class FlashCard {

    // number of days between cards being shown for study
    private static final int STUDY_INTERVAL_CORRECT = 7;
    private static final int STUDY_INTERVAL_INCORRECT = 1;

    public static final boolean MALE = false;
    public static final boolean FEMALE = true;
    public static final Boolean NONE = null;

    private String deckFileString;
    private LocalDate dueDate;
    
    private String english;
    private String french;
    private Boolean gender;
    private String[] englishAnswers;
    private String[] frenchAnswers;

    public FlashCard(String deckFileString, LocalDate dueDate, 
            String[] english, String[] french, Boolean gender) {
        this.english = english[0];
        englishAnswers = english;
        this.french = french[0];
        frenchAnswers = french;
        this.gender = gender;

        this.deckFileString = deckFileString;
        this.dueDate = dueDate;

        for (int i = 0; i < englishAnswers.length; i++) {
            englishAnswers[i] = englishAnswers[i].replaceAll("\\([^)]+\\)", "").replaceAll("\\s+", " ").strip();
        }
    }

    /**
     * Returns a Boolean object representing this FlashCard's gender
     * @return true if female, false if male, or null if this card has no gender.
     * See FlashCard.MALE, FlashCard.FEMALE, and FlashCard.NONE.
     */
    public Boolean getGender() {
        return gender;
    }

    /**
     * Checks a String and compares it to the list of valid answers,
     * returning true if the input given is a valid answer, otherwise false
     * @param viewingFrenchSide true if the user is viewing the french side, 
     * or false if the user is viewing the english side
     * @param answer The input to check
     * @return true if the list of valid answers contained the input,
     * otherwise false
     */
    public boolean checkAnswer(boolean viewingFrenchSide, String answer) {
        if (viewingFrenchSide) {
            for (String a : englishAnswers) {
                if (a.equals(answer)) {
                    return true;
                }
            }
            return false;
        } else {
            for (String a : frenchAnswers) {
                if (a.equals(answer)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Gets the English side of this flashcard
     * @return a String containing the English translation
     */
    public String getEnglish() {
        return english;
    }

    /**
     * Returns the French side of this flashcard
     * @return a String containing the French side
     */
    public String getFrench() {
        return french;
    }

    /**
     * Returns this card's deck file string, for saving purposes
     * @return a String containing the card's information, including
     * its English and French translations, gender, and due date
     */
    public String getDeckFileString() {
        if (dueDate != null) {
            return deckFileString + " @ " + dueDate.toString();
        }
        return deckFileString;
    }

    public void updateDueDate(boolean userWasCorrect) {
        if (userWasCorrect) {
            setDueInDays(STUDY_INTERVAL_CORRECT);
        } else {
            setDueInDays(STUDY_INTERVAL_INCORRECT);
        }
    }

    /**
     * Causes this card to be due a given number of days after
     * today's date
     * @param numberOfDays the number of days (e.g. 1 for tomorrow)
     */
    public void setDueInDays(int numberOfDays) {
        LocalDate today = LocalDate.now();
        dueDate = today.plusDays(numberOfDays);
    }

    /**
     * Determines whether this card is due for study by checking
     * its due date and comparing it against today's date
     * @return true is this card is due for study, otherwise false
     */
    public boolean isDue() {
        if (dueDate == null) {
            return true;
        }
        LocalDate today = LocalDate.now();
        return dueDate.compareTo(today) <= 0;
    }
}
