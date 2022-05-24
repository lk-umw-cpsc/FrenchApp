import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Deck {
    
    private final File file;

    private final List<FlashCard> cards;

    private String description;

    public Deck(File deckFile) {
        file = deckFile;

        cards = new ArrayList<>();

        try (Scanner s = new Scanner(deckFile, "UTF-8")) {
            description = s.nextLine();
            int lineNumber = 1;
            String line = "";
            try {
                while (s.hasNextLine()) {
                    line = s.nextLine().strip();;
                    String[] stringAndDueDate = line.split(" @ ");
                    String string = stringAndDueDate[0];
                    LocalDate dueDate = null;
                    if (stringAndDueDate.length > 1) {
                        try {
                            dueDate = LocalDate.parse(stringAndDueDate[1]);
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid due date for following card:");
                            System.out.println(line);
                        }
                    }
                    String[] pieces = string.split(" \\| ");
                    String[] englishAnswers = pieces[1].split(",");
                    for (int i = 0; i < englishAnswers.length; i++) {
                        englishAnswers[i] = englishAnswers[i].strip();
                    }
                    String[] frenchAnswers = pieces[0].split(",");
                    for (int i = 0; i < frenchAnswers.length; i++) {
                        frenchAnswers[i] = frenchAnswers[i].strip();
                    }
                    Boolean gender = null;
                    char genderChar = pieces[2].strip().charAt(0);
                    if (genderChar == 'm') {
                        gender = FlashCard.MALE;
                    } else if(genderChar == 'f') {
                        gender = FlashCard.FEMALE;
                    }
                    FlashCard card = new FlashCard(string, dueDate, englishAnswers, frenchAnswers, gender);
                    cards.add(card);
                    lineNumber++;
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Fatal error reading line " + lineNumber + " of " + file + ":");
                System.out.println(line);
                System.out.println("Incorrect number of arguments on this line");
                System.exit(0);
            }
        } catch (FileNotFoundException e) {
            
        }
    }

    public List<FlashCard> getDueCards() {
        List<FlashCard> due = new ArrayList<>(cards.size());
        for (FlashCard c : cards) {
            if (c.isDue()) {
                due.add(c);
            }
        }
        return due;
    }

    public void save() {
        try (PrintWriter out = new PrintWriter(file)) {
            out.println(description);
            for (FlashCard c : cards) {
                out.println(c.getDeckFileString());
            }
        } catch (IOException e) {}
    }

    public String getDescription() {
        return description;
    }

    public List<FlashCard> getCards() {
        return cards;
    }

}
