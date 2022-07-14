import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import swingcustom.BasicLabel;
import swingcustom.CustomButton;
import swingcustom.CustomCheckBox;
import swingcustom.CustomRadioButton;
import swingcustom.CustomTextField;
import swingcustom.FontsAndColors;

public class VocabFrame extends JFrame implements WindowListener {

    private static final int CONTENT_PANE_PADDING = 16;

    private static final String CORRECT_ANSWER_COLOR = "#a3ffbc";
    private static final String INCORRECT_ANSWER_COLOR = "#ffa3a3";

    private static final String COLOR_GENDER_NEUTRAL = "#ffffff";
    private static final String COLOR_MASCULINE = "#a3ccff";
    private static final String COLOR_FEMININE = "#ffa3f3";

    private static final int CHARACTER_COUNT_CUTOFF = 28;

    private final Random rng = new Random();
    
    private JFrame parent;

    private JLabel flashcardLabel;
    private JTextField answerField;

    private boolean sideShownIsFrench;
    private List<FlashCard> dueCardsRemaining;
    private List<FlashCard> incorrectPile;
    private FlashCard currentCard;

    private Box optionsPane;
    private Box flashcardsPane;

    private JRadioButton showFrench;
    private JRadioButton showEnglish;
    private JRadioButton showBoth;
    private ButtonGroup sideChoice;

    private JCheckBox showGenderHintsOption;

    private boolean showGender;
    private FlashCardStudyMode mode;
    private Map<JRadioButton, FlashCardStudyMode> buttonToModeMap = new HashMap<>();

    private boolean reviewingMistakes;

    private Deck deck;

    private String promptText;
    
    private Component[] animatedComponents;
    private Color[] colorsFrom, colorsCorrect, colorsIncorrect;
    
    public VocabFrame(Deck deck, JFrame parent) {
        super("Pratiquer " + deck.getDescription());
        this.deck = deck;

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setResizable(false);
        
        animatedComponents = new Component[2];
        colorsFrom = new Color[2];
        colorsCorrect = new Color[2];
        colorsIncorrect = new Color[2];

        dueCardsRemaining = deck.getDueCards();
        if (dueCardsRemaining.isEmpty()) {
            dueCardsRemaining.addAll(deck.getCards());
        }
        Collections.shuffle(dueCardsRemaining);
        incorrectPile = new ArrayList<>();
        
        this.parent = parent;
        addWindowListener(this);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        optionsPane = Box.createVerticalBox();
        optionsPane.setBorder(new EmptyBorder(CONTENT_PANE_PADDING, CONTENT_PANE_PADDING, CONTENT_PANE_PADDING, CONTENT_PANE_PADDING));
        optionsPane.setOpaque(true);
        optionsPane.setBackground(FontsAndColors.APP_BACKGROUND);

        Box layer = Box.createHorizontalBox();
            layer.add(new BasicLabel("Show side:"));
            layer.add(Box.createHorizontalGlue());
        optionsPane.add(layer);

        layer = Box.createHorizontalBox();
            showEnglish = new CustomRadioButton("English (hard)");
            layer.add(showEnglish);
            showFrench = new CustomRadioButton("French (easy)");
            layer.add(showFrench);
            showBoth = new CustomRadioButton("Both (random)");
            layer.add(showBoth);

            sideChoice = new ButtonGroup();
            sideChoice.add(showEnglish);
            sideChoice.add(showFrench);
            sideChoice.add(showBoth);
            showEnglish.setSelected(true);

            buttonToModeMap.put(showEnglish, FlashCardStudyMode.SHOW_ENGLISH);
            buttonToModeMap.put(showFrench, FlashCardStudyMode.SHOW_FRENCH);
            buttonToModeMap.put(showBoth, FlashCardStudyMode.SHOW_RANDOM);
        optionsPane.add(layer);

        optionsPane.add(Box.createHorizontalStrut(CONTENT_PANE_PADDING));

        layer = Box.createHorizontalBox();
            layer.add(new BasicLabel("Additonal options:"));
            layer.add(Box.createHorizontalGlue());
        optionsPane.add(layer);

        layer = Box.createHorizontalBox();
            showGenderHintsOption = new CustomCheckBox("Show gender hints");
            showGenderHintsOption.setSelected(true);
            layer.add(showGenderHintsOption);
            layer.add(Box.createHorizontalGlue());
        optionsPane.add(layer);

        layer = Box.createHorizontalBox();
            CustomButton startButton = new CustomButton("Start!");
            Dimension d = startButton.getPreferredSize();
            startButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)d.getHeight()));
            startButton.addButtonListener(this::startButtonPressed);
            layer.add(startButton);
        optionsPane.add(layer);

        add(optionsPane);

        flashcardsPane = Box.createVerticalBox();
        flashcardsPane.setOpaque(true);
        flashcardsPane.setBackground(FontsAndColors.APP_BACKGROUND);
        animatedComponents[0] = flashcardsPane;
        colorsFrom[0] = flashcardsPane.getBackground();
        colorsCorrect[0] = FontsAndColors.COLOR_APP_BACKGROUND_CORRECT;
        colorsIncorrect[0] = FontsAndColors.COLOR_APP_BACKGROUND_INCORRECT;

        flashcardsPane.setBorder(new EmptyBorder(CONTENT_PANE_PADDING, CONTENT_PANE_PADDING, CONTENT_PANE_PADDING, CONTENT_PANE_PADDING));

            JPanel flashcard = new JPanel();
            flashcard.setPreferredSize(new Dimension(600, 300));
            flashcard.setBackground(FontsAndColors.COLOR_DARK_BACKGROUND);
            // flashcard.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            flashcardsPane.add(flashcard);
            animatedComponents[1] = flashcard;
            colorsFrom[1] = flashcard.getBackground();
            colorsCorrect[1] = FontsAndColors.COLOR_DARK_BACKGROUND_CORRECT;
            colorsIncorrect[1] = FontsAndColors.COLOR_DARK_BACKGROUND_INCORRECT;

            Box textContainer = Box.createVerticalBox();
            textContainer.add(Box.createVerticalStrut(120));

            Box flashcardTextContainer = Box.createHorizontalBox();
            flashcardTextContainer.add(Box.createHorizontalGlue());
            flashcardLabel = new JLabel();
            flashcardLabel.setFont(FontsAndColors.FONT_PROMPT.deriveFont(48f));
            flashcardTextContainer.add(flashcardLabel);
            flashcardTextContainer.add(Box.createHorizontalGlue());
            textContainer.add(flashcardTextContainer);

            textContainer.add(Box.createVerticalStrut(16));
            
            flashcard.add(textContainer);

        flashcardsPane.add(answerField = new CustomTextField(1));
        answerField.addActionListener(this::answerSubmitted);
        // answerField.setFont(new Font("Helvetica", 0, 24));
        // answerField.setHorizontalAlignment(JTextField.CENTER);

        flashcardsPane.setVisible(false);
        
        add(flashcardsPane);

        pack();
    }

    private void startButtonPressed() {
        if (showEnglish.isSelected()) {
            mode = FlashCardStudyMode.SHOW_ENGLISH;
        } else if(showFrench.isSelected()) {
            mode = FlashCardStudyMode.SHOW_FRENCH;
        } else {
            mode = FlashCardStudyMode.SHOW_RANDOM;
        }
        showGender = showGenderHintsOption.isSelected();
        pullCard();
        optionsPane.setVisible(false);
        flashcardsPane.setVisible(true);
        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Method called when the user presses Enter/Return to submit
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
        String color;
        String answerText;
        if (currentCard.checkAnswer(sideShownIsFrench, input)) {
            ColorAnimationEngine.tryLockAndAnimateIfUnlocked(animatedComponents, colorsFrom, colorsCorrect);
            answerText = input;
            color = CORRECT_ANSWER_COLOR;
            if (reviewingMistakes) {
                currentCard.updateDueDate(FlashCard.ANSWER_REVIEWED);
            } else {
                currentCard.updateDueDate(FlashCard.ANSWER_CORRECT);
            }
        } else {
            ColorAnimationEngine.tryLockAndAnimateIfUnlocked(animatedComponents, colorsFrom, colorsIncorrect);
            color = INCORRECT_ANSWER_COLOR;
            answerText = answer;
            incorrectPile.add(currentCard);
            currentCard.updateDueDate(FlashCard.ANSWER_INCORRECT);
        }
        flashcardLabel.setText("<html><body style='text-align: center'>" 
                + promptText + "<br><font color='" + color + "'>" + answerText + "</font><br>&nbsp;</body></html>");

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
        if (dueCardsRemaining.isEmpty()) {
            reviewingMistakes = true;
            if (incorrectPile.isEmpty()) {
                deck.save();
                parent.setVisible(true);
                dispose();
            } else {
                dueCardsRemaining.addAll(incorrectPile);
                incorrectPile.clear();
                currentCard = dueCardsRemaining.remove(dueCardsRemaining.size() - 1);
            }
        } else {
            currentCard = dueCardsRemaining.remove(dueCardsRemaining.size() - 1);
        }
        Boolean gender = currentCard.getGender();
        String color = COLOR_GENDER_NEUTRAL;
        if (!showGender || gender == FlashCard.NONE) {
            // flashcardLabel.setForeground(COLOR_GENDER_NEUTRAL);\
            color = COLOR_GENDER_NEUTRAL;
        } else if (gender == FlashCard.MALE) {
            color = COLOR_MASCULINE;
        } else {
            color = COLOR_FEMININE;
        }
        // flip coin?
        switch (mode) {
            case SHOW_ENGLISH:
                sideShownIsFrench = false;
                break;
            case SHOW_FRENCH:
                sideShownIsFrench = true;
                break;
            case SHOW_RANDOM:
                sideShownIsFrench = rng.nextBoolean();
        }
        String text;
        if (sideShownIsFrench) {
            text = currentCard.getFrench();
        } else {
            text = currentCard.getEnglish();
        }
        promptText = "<font color='" + color + "'>";
        promptText += insertLineBreaks(text) + "</font>";
        flashcardLabel.setText("<html><body style='text-align: center'>" + promptText + "</body></html>");
        answerField.setEnabled(true);
    }

    private String insertLineBreaks(String s) {
        String[] words = s.split(" ");
        /*if (words.length == 1) {
            return s;
        }*/
        String currentLine = words[0];
        String html = "";
        for (int i = 1; i < words.length; i++) {
            int currentLength = currentLine.length();
            if (currentLength + words[i].length() + 1 > CHARACTER_COUNT_CUTOFF) {
                html += currentLine + "<br>";
                currentLine = words[i];
            } else {
                currentLine += " " + words[i];
            }
        }
        html += currentLine;
        return html;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        deck.save();
        if (parent != null) {
            parent.setLocationRelativeTo(this);
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
