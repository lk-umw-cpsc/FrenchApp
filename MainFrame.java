import java.awt.Dimension;
import java.awt.event.ActionEvent;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

    private DeckChooserFrame deckChooserFrame;
    private NumberPracticeModeChooserFrame numberPracticeModeChooserFrame;
    private VerbGroupChooserFrame verbGroupChooserFrame;
    private TimePracticeFrame timePracticeFrame;

    private static final int PADDING = 16;

    public MainFrame() {
        super("French App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }
    
    public void createAndShow() {
        Box mainBox = Box.createVerticalBox();
        mainBox.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        JButton button;
        List<JButton> buttons = new ArrayList<>();

        mainBox.add(button = new JButton("Practice Vocabulary"));
        button.addActionListener(this::vocabPressed);
        buttons.add(button);

        mainBox.add(button = new JButton("Practice Numbers"));
        button.addActionListener(this::numbersPressed);
        buttons.add(button);

        mainBox.add(button = new JButton("Practice Verb Conjugation"));
        button.addActionListener(this::conjugatePressed);
        buttons.add(button);
        
        mainBox.add(button = new JButton("Practice Telling Time"));
        button.addActionListener(this::timePressed);
        buttons.add(button);

        for (JButton b : buttons) {
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)b.getPreferredSize().getHeight()));
        }
        
        add(mainBox);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void vocabPressed(ActionEvent e) {
        if (deckChooserFrame == null) {
            deckChooserFrame = new DeckChooserFrame(this);
        }
        deckChooserFrame.setLocationRelativeTo(this);
        deckChooserFrame.setVisible(true);
        setVisible(false);
    }

    private void numbersPressed(ActionEvent e) {
        if (numberPracticeModeChooserFrame == null) {
            numberPracticeModeChooserFrame = new NumberPracticeModeChooserFrame(this);
        }
        numberPracticeModeChooserFrame.setLocationRelativeTo(this);
        numberPracticeModeChooserFrame.setVisible(true);
        setVisible(false);
    }

    private void conjugatePressed(ActionEvent e) {
        if (verbGroupChooserFrame == null) {
            verbGroupChooserFrame = new VerbGroupChooserFrame(this);
        }
        verbGroupChooserFrame.setLocationRelativeTo(this);
        verbGroupChooserFrame.setVisible(true);
        setVisible(false);
    }

    private void timePressed(ActionEvent e) {
        if (timePracticeFrame == null) {
            timePracticeFrame = new TimePracticeFrame(this);
        }
        timePracticeFrame.setLocationRelativeTo(this);
        timePracticeFrame.setVisible(true);
        setVisible(false);
    }

}