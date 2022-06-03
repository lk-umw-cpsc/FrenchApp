import java.util.Map;

public class Verb {
    private String infinitive;
    private Map<String, String> conjugations;

    public Verb(String infinitive, Map<String, String> conjugations) {
        this.infinitive = infinitive;
        this.conjugations = conjugations;
        this.conjugations.put("infinitive", infinitive);
    }

    public String getInfinitive() {
        return infinitive;
    }

    public String getConjugation(String form) {
        return conjugations.get(form);
    }
}
