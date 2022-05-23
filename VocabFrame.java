import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class VocabFrame extends JFrame implements WindowListener {

    private static final int PADDING = 16;

    private static final Color CORRECT_ANSWER_COLOR = new Color(0, 200, 0);
    private static final Color INCORRECT_ANSWER_COLOR = new Color(200, 0, 0);

    private static final Color COLOR_GENDER_NEUTRAL = Color.BLACK;
    private static final Color COLOR_MASCULINE = new Color(10, 0, 194);
    private static final Color COLOR_FEMININE = new Color(204, 0, 139);

    private final Random rng = new Random();
    
    private JFrame parent;

    private JLabel flashcardLabel;
    private JLabel answerLabel;
    private JTextField answerField;

    private boolean sideShownIsFrench;
    private List<FlashCard> cardsBeingStudied;
    private List<FlashCard> incorrectDeck;
    private List<FlashCard> allCards;
    private FlashCard currentCard;

    private Box optionsPane;
    private Box flashcardsPane;

    private JRadioButton showFrench;
    private JRadioButton showEnglish;
    private JRadioButton showBoth;
    private ButtonGroup sideChoice;

    private JCheckBox showGenderHintsOption;

    private boolean showGender;
    private Mode mode;
    private Map<JRadioButton, Mode> buttonToModeMap = new HashMap<>();

    private final File deckFile;
    private String deckDescription;
    
    public VocabFrame(File f) {
        super("Vocab");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setResizable(false);

        deckFile = f;

        allCards = new ArrayList<>();
        cardsBeingStudied = new ArrayList<>();
        incorrectDeck = new ArrayList<>();
        try (Scanner s = new Scanner(f, "UTF-8")) {
            deckDescription = s.nextLine();
            setTitle("Pratiquer " + deckDescription);
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
                    allCards.add(card);
                    if (card.isDue()) {
                        cardsBeingStudied.add(card);
                    }
                    lineNumber++;
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Fatal error reading line " + lineNumber + " of " + f + ":");
                System.out.println(line);
                System.out.println("Incorrect number of arguments on this line");
                System.exit(0);
            }
            Collections.shuffle(cardsBeingStudied);
        } catch (FileNotFoundException e) {}
    }

    public void createAndShow(JFrame parent) {
        this.parent = parent;
        addWindowListener(this);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        optionsPane = Box.createVerticalBox();
        optionsPane.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        Box layer = Box.createHorizontalBox();
            layer.add(new JLabel("Show side:"));
            layer.add(Box.createHorizontalGlue());
        optionsPane.add(layer);

        layer = Box.createHorizontalBox();
            showEnglish = new JRadioButton("English (hard)");
            layer.add(showEnglish);
            showFrench = new JRadioButton("French (easy)");
            layer.add(showFrench);
            showBoth = new JRadioButton("Both (random)");
            layer.add(showBoth);

            sideChoice = new ButtonGroup();
            sideChoice.add(showEnglish);
            sideChoice.add(showFrench);
            sideChoice.add(showBoth);
            showEnglish.setSelected(true);

            buttonToModeMap.put(showEnglish, Mode.ENGLISH);
            buttonToModeMap.put(showFrench, Mode.ENGLISH);
            buttonToModeMap.put(showBoth, Mode.RANDOM);
        optionsPane.add(layer);

        optionsPane.add(Box.createHorizontalStrut(PADDING));

        layer = Box.createHorizontalBox();
            layer.add(new JLabel("Additonal options:"));
            layer.add(Box.createHorizontalGlue());
        optionsPane.add(layer);

        layer = Box.createHorizontalBox();
            showGenderHintsOption = new JCheckBox("Show gender hints");
            showGenderHintsOption.setSelected(true);
            layer.add(showGenderHintsOption);
            layer.add(Box.createHorizontalGlue());
        optionsPane.add(layer);

        JButton startButton = new JButton("Start!");
        Dimension d = startButton.getPreferredSize();
        startButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)d.getHeight()));
        startButton.addActionListener(this::startButtonPressed);
        optionsPane.add(startButton);

        add(optionsPane);

        flashcardsPane = Box.createVerticalBox();

        flashcardsPane.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

            JPanel flashcard = new JPanel();
            flashcard.setPreferredSize(new Dimension(600, 300));
            flashcard.setBackground(Color.WHITE);
            flashcard.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            flashcardsPane.add(flashcard);

            Box textContainer = Box.createVerticalBox();
            textContainer.add(Box.createVerticalStrut(120));

            Box flashcardTextContainer = Box.createHorizontalBox();
            flashcardTextContainer.add(Box.createHorizontalGlue());
            flashcardLabel = new JLabel();
            flashcardLabel.setFont(new Font("Helvetica", 0, 48));
            flashcardTextContainer.add(flashcardLabel);
            flashcardTextContainer.add(Box.createHorizontalGlue());
            textContainer.add(flashcardTextContainer);

            textContainer.add(Box.createVerticalStrut(16));

            Box answerTextContainer = Box.createHorizontalBox();
            answerTextContainer.add(Box.createHorizontalGlue());
            answerLabel = new JLabel();
            answerLabel.setFont(new Font("Helvetica", 0, 48));
            answerTextContainer.add(answerLabel);
            answerTextContainer.add(Box.createHorizontalGlue());
            textContainer.add(answerTextContainer);
            
            flashcard.add(textContainer);

        flashcardsPane.add(answerField = new JTextField());
        answerField.addActionListener(this::answerSubmitted);
        answerField.setFont(new Font("Helvetica", 0, 24));
        answerField.setHorizontalAlignment(JTextField.CENTER);

        flashcardsPane.setVisible(false);
        
        add(flashcardsPane);

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void startButtonPressed(ActionEvent e) {
        if (showEnglish.isSelected()) {
            mode = Mode.ENGLISH;
        } else if(showFrench.isSelected()) {
            mode = Mode.FRENCH;
        } else {
            mode = Mode.RANDOM;
        }
        showGender = showGenderHintsOption.isSelected();

        pullCard();
        optionsPane.setVisible(false);
        flashcardsPane.setVisible(true);
        pack();
    }

    /**
     * Method called when the user pressed Enter/Return to submit
     * their answer for the current flash card.
     * 
     * The correct answer is displayed below the flash card's text,
     * in green if the input was correct, or red otherwise.
     * 
     * If incorrect, the card is added to the incorrect pile.
     * @param e Event information passed by Swing event thread
     */
    private void answerSubmitted(ActionEvent e) {
        String input = answerField.getText().strip();

        String answer;
        if (sideShownIsFrench) {
            answer = currentCard.getEnglish();
        } else {
            answer = currentCard.getFrench();
        }

        if (currentCard.checkAnswer(sideShownIsFrench, input)) {
            answerLabel.setText(input);
            answerLabel.setForeground(CORRECT_ANSWER_COLOR);
            currentCard.updateDueDate(true);
        } else {
            answerLabel.setForeground(INCORRECT_ANSWER_COLOR);
            answerLabel.setText(answer);
            incorrectDeck.add(currentCard);
            currentCard.updateDueDate(false);
        }

        answerField.setEnabled(false);
        answerField.setText("");
        new Thread(this::updateCard).start();
    }

    /**
     * Waits 2.5 seconds, then picks a new card.
     */
    private void updateCard() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {}
        SwingUtilities.invokeLater(this::pullCard);
    }

    /**
     * Chooses a new card and displays it.
     */
    private void pullCard() {
        if (cardsBeingStudied.isEmpty()) {
            if (incorrectDeck.isEmpty()) {
                parent.setVisible(true);
                dispose();
            } else {
                cardsBeingStudied.addAll(incorrectDeck);
                incorrectDeck.clear();
                currentCard = cardsBeingStudied.remove(cardsBeingStudied.size() - 1);
            }
        } else {
            currentCard = cardsBeingStudied.remove(cardsBeingStudied.size() - 1);
        }
        Boolean gender = currentCard.getGender();
        if (!showGender || gender == FlashCard.NONE) {
            flashcardLabel.setForeground(COLOR_GENDER_NEUTRAL);
        } else if (gender == FlashCard.MALE) {
            flashcardLabel.setForeground(COLOR_MASCULINE);
        } else {
            flashcardLabel.setForeground(COLOR_FEMININE);
        }
        // flip coin?
        switch (mode) {
            case ENGLISH:
                sideShownIsFrench = false;
                break;
            case FRENCH:
                sideShownIsFrench = true;
                break;
            case RANDOM:
                sideShownIsFrench = rng.nextBoolean();
        }
        if (sideShownIsFrench) {
            flashcardLabel.setText(currentCard.getFrench());
        } else {
            flashcardLabel.setText(currentCard.getEnglish());
        }
        answerLabel.setText("");
        answerField.setEnabled(true);
    }

    /**
     * Saves the current deck's cards, updating their
     * due dates
     */
    private void saveDeck() {
        try (PrintWriter out = new PrintWriter(deckFile)) {
            out.println(deckDescription);
            for (FlashCard c : allCards) {
                out.println(c.getDeckFileString());
            }
        } catch (IOException e) {}
    }

    @Override
    public void windowClosing(WindowEvent e) {
        saveDeck();
        if (parent != null) {
            parent.setVisible(true);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        
    }

    @Override
    public void windowClosed(WindowEvent e) {
        
    }

    @Override
    public void windowIconified(WindowEvent e) {
        
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        
    }

    @Override
    public void windowActivated(WindowEvent e) {
        
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        
    }

}
