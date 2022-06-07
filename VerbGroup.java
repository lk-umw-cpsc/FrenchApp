import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VerbGroup {

    private static final char COMMENT_CHARACTER = '#';

    private String description;
    private String[] columns;
    private List<Verb> verbs;

    public VerbGroup(File f) {
        verbs = new ArrayList<>();
        parseFile(f);
    }

    public String getDescription() {
        return description;
    }

    private void parseFile(File f) {
        try (Scanner in = new Scanner(f)) {
            description = in.nextLine();
            columns = in.nextLine().split(",");
            for (int i = 0; i < columns.length; i++) {
                columns[i] = columns[i].strip();
            }

            boolean state = false;
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.isBlank() || line.charAt(0) == COMMENT_CHARACTER) {
                    continue;
                }

                if (state) {
                    // translations; currently not used
                    state = false;
                } else {
                    String[] french = line.split(",");
                    Map<String, String> verbMap = new HashMap<>();
                    for (int word = 0; word < french.length; word++) {
                        french[word] = french[word].strip();
                        verbMap.put(columns[word], french[word]);
                    }
                    Verb verb = new Verb(french[0], verbMap);
                    verbs.add(verb);
                    state = true;
                }
            }
        } catch (FileNotFoundException e) {}
    }

    public List<Verb> getVerbs() {
        return verbs;
    }
    
}
