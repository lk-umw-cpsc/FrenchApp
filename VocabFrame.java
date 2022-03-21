import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Ideas: 
 * Gender hints (color of flash card text is either pink or blue)
 * ^ these can be toggled
 * Three modes: show English, show French, both
 */

public class VocabFrame extends JFrame implements WindowListener {

    private JFileChooser fileChooser;

    private static final int PADDING = 16;

    private static final Color CORRECT_ANSWER_COLOR = new Color(0, 200, 0);
    private static final Color INCORRECT_ANSWER_COLOR = new Color(200, 0, 0);
    
    private JFrame owner;

    private JLabel flashcardText;
    private JLabel answerLabel;
    private JTextField answerField;

    private boolean sideShownIsFrench;
    private List<FlashCard> deck;
    private List<FlashCard> incorrectDeck;
    private FlashCard currentCard;
    
    public VocabFrame() {
        super("Vocab");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setResizable(false);

        fileChooser = new JFileChooser(new File("decks"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Deck Files", "deck"));

        deck = new ArrayList<>();
        incorrectDeck = new ArrayList<>();
        try {
            fileChooser.showOpenDialog(null);
            File f = fileChooser.getSelectedFile();
            Scanner s = new Scanner(f);
            String deckname = s.nextLine();
            while (s.hasNextLine()) {
                String line = s.nextLine();
                String[] words = line.split(" : ");
                deck.add(new FlashCard(words[1], words[0]));
            }
            Collections.shuffle(deck);
        } catch (FileNotFoundException e) {}
    }

    public void createAndShow(JFrame owner) {
        this.owner = owner;
        addWindowListener(this);

        Box horizontalContainer = Box.createHorizontalBox();
        Box verticalContainer = Box.createVerticalBox();

        horizontalContainer.add(Box.createHorizontalStrut(PADDING));
        horizontalContainer.add(verticalContainer);
        horizontalContainer.add(Box.createHorizontalStrut(PADDING));

        verticalContainer.add(Box.createVerticalStrut(PADDING));

            JPanel flashcard = new JPanel();
            flashcard.setPreferredSize(new Dimension(600, 300));
            flashcard.setBackground(Color.WHITE);
            flashcard.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            verticalContainer.add(flashcard);

            Box textContainer = Box.createVerticalBox();
            textContainer.add(Box.createVerticalStrut(120));

            Box flashcardTextContainer = Box.createHorizontalBox();
            flashcardTextContainer.add(Box.createHorizontalGlue());
            flashcardText = new JLabel();
            flashcardText.setFont(new Font("Helvetica", 0, 48));
            flashcardTextContainer.add(flashcardText);
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

        verticalContainer.add(answerField = new JTextField());
        answerField.addActionListener(this::answerSubmitted);
        answerField.setFont(new Font("Helvetica", 0, 24));
        answerField.setHorizontalAlignment(JTextField.CENTER);
        
        verticalContainer.add(Box.createVerticalStrut(PADDING));

        add(horizontalContainer);

        pack();
        setLocationRelativeTo(owner);
        pullCard();
        setVisible(true);
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

        if (input.equals(answer)) {
            answerLabel.setForeground(CORRECT_ANSWER_COLOR);
        } else {
            answerLabel.setForeground(INCORRECT_ANSWER_COLOR);
            incorrectDeck.add(currentCard);
        }
        answerLabel.setText(answer);

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
        if (deck.isEmpty()) {
            if (incorrectDeck.isEmpty()) {
                System.exit(0);
            } else {
                deck.addAll(incorrectDeck);
                incorrectDeck.clear();
                currentCard = deck.remove(deck.size() - 1);
            }
        } else {
            currentCard = deck.remove(deck.size() - 1);
        }
        // flip coin?
        if (sideShownIsFrench) {
            flashcardText.setText(currentCard.getFrench());
        } else {
            flashcardText.setText(currentCard.getEnglish());
        }
        answerLabel.setText("");
        answerField.setEnabled(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowClosing(WindowEvent e) {
        owner.setVisible(true);
        // System.out.println("Window closing!");
    }

    @Override
    public void windowClosed(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

}
