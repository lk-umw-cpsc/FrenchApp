import java.awt.Component;
import java.awt.Dimension;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import swingcustom.CustomButton;
import swingcustom.FontsAndColors;

public class MainFrame extends JFrame {

    private DeckChooserFrame deckChooserFrame;
    private NumberPracticeModeChooserFrame numberPracticeModeChooserFrame;
    private VerbGroupChooserFrame verbGroupChooserFrame;
    private TimePracticeFrame timePracticeFrame;

    private static final int PADDING = 16;

    public MainFrame() {
        super("French Study Tool by Lauren Knight");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        Box rowContainer = Box.createVerticalBox();
        rowContainer.setBorder(new EmptyBorder(8, PADDING, PADDING, PADDING));

        rowContainer.setOpaque(true);
        rowContainer.setBackground(FontsAndColors.APP_BACKGROUND);

        CustomButton button;
        List<Component> buttons = new ArrayList<>();

        Box row = Box.createHorizontalBox();
            row.add(Box.createHorizontalGlue());
            JLabel label = new JLabel("Choose an activity:");
            label.setFont(FontsAndColors.FONT_HEADING);
            label.setForeground(FontsAndColors.COLOR_DARK_FOREGROUND);
            row.add(label);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        rowContainer.add(Box.createVerticalStrut(4));

        row = Box.createHorizontalBox();
            // row.add(button = new JButton("Vocabulary"));
            button = new CustomButton("Vocabulary");
            button.addButtonListener(this::vocabPressed);
            row.add(button);
            buttons.add(button);
        rowContainer.add(row);

        rowContainer.add(Box.createVerticalStrut(4));

        row = Box.createHorizontalBox();
            row.add(button = new CustomButton("Numbers"));
        rowContainer.add(row);
        button.addButtonListener(this::numbersPressed);
        buttons.add(button);
        rowContainer.add(Box.createVerticalStrut(4));

        row = Box.createHorizontalBox();
            row.add(button = new CustomButton("Conjugation"));
        rowContainer.add(row);
        button.addButtonListener(this::conjugatePressed);
        buttons.add(button);
        rowContainer.add(Box.createVerticalStrut(4));
        
        row = Box.createHorizontalBox();
            row.add(button = new CustomButton("Time"));
        rowContainer.add(row);
        button.addButtonListener(this::timePressed);
        buttons.add(button);

        for (Component b : buttons) {
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, b.getPreferredSize().height));
        }
        
        add(rowContainer);

        pack();
        Dimension d = rowContainer.getSize();
        d.width = 250;
        rowContainer.setPreferredSize(d);
        pack();
        setLocationRelativeTo(null);
    }

    private void vocabPressed() {
        if (deckChooserFrame == null) {
            deckChooserFrame = new DeckChooserFrame(this);
        }
        deckChooserFrame.setLocationRelativeTo(this);
        deckChooserFrame.setVisible(true);
        setVisible(false);
    }

    private void numbersPressed() {
        if (numberPracticeModeChooserFrame == null) {
            numberPracticeModeChooserFrame = new NumberPracticeModeChooserFrame(this);
        }
        numberPracticeModeChooserFrame.setLocationRelativeTo(this);
        numberPracticeModeChooserFrame.setVisible(true);
        setVisible(false);
    }

    private void conjugatePressed() {
        if (verbGroupChooserFrame == null) {
            verbGroupChooserFrame = new VerbGroupChooserFrame(this);
        }
        verbGroupChooserFrame.setLocationRelativeTo(this);
        verbGroupChooserFrame.setVisible(true);
        setVisible(false);
    }

    private void timePressed() {
        if (timePracticeFrame == null) {
            timePracticeFrame = new TimePracticeFrame(this);
        }
        timePracticeFrame.setLocationRelativeTo(this);
        timePracticeFrame.setVisible(true);
        setVisible(false);
    }

}