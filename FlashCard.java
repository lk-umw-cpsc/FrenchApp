
public class FlashCard {

    public static final boolean MALE = false;
    public static final boolean FEMALE = true;
    public static final Boolean NONE = null;
    
    private String english;
    private String french;
    private Boolean gender;
    private String[] englishAnswers;
    private String[] frenchAnswers;

    public FlashCard(String[] english, String[] french, Boolean gender) {
        this.english = english[0];
        englishAnswers = english;
        this.french = french[0];
        frenchAnswers = french;
        this.gender = gender;

        for (int i = 0; i < englishAnswers.length; i++) {
            englishAnswers[i] = englishAnswers[i].replaceAll("\\([^)]+\\)", "").strip();
        }
    }

    public Boolean getGender() {
        return gender;
    }

    public boolean checkAnswer(boolean frenchSide, String answer) {
        if (frenchSide) {
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

    public String getEnglish() {
        return english;
    }

    public String getFrench() {
        return french;
    }
}
