import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import swingcustom.BasicLabel;
import swingcustom.CustomTextField;
import swingcustom.FontsAndColors;
import swingcustom.PromptLabel;
import swingextended.ApplicationUIConstants;
import swingextended.AutoShowParentFrame;

public class TimePracticeFrame extends AutoShowParentFrame {
    
    private JLabel timeLabel;
    private JTextField inputField;

    private FrenchTime answer;

    private Component[] animatedComponents;
    private Color[] colorsFrom, colorsCorrect, colorsIncorrect;

    public TimePracticeFrame(JFrame parent) {
        super(parent, "Quelle heure est-il?");

        setResizable(false);

        initializeChildComponents();
        pickNextAnswer();
    }

    private void initializeChildComponents() {
        // lay rows out vertically
        Box rowContainer = Box.createVerticalBox();
        rowContainer.setOpaque(true);
        rowContainer.setBackground(FontsAndColors.APP_BACKGROUND);

        animatedComponents = new Component[2];
        colorsFrom = new Color[2];
        colorsCorrect = new Color[2];
        colorsIncorrect = new Color[2];

        final int padding = ApplicationUIConstants.FRAME_CONTENT_PADDING;
        rowContainer.setBorder(new EmptyBorder(padding, padding, padding, padding));
        animatedComponents[0] = rowContainer;
        colorsFrom[0] = rowContainer.getBackground();
        colorsCorrect[0] = FontsAndColors.COLOR_APP_BACKGROUND_CORRECT;
        colorsIncorrect[0] = FontsAndColors.COLOR_APP_BACKGROUND_INCORRECT;

        // will hold each row of components
        Box row;

        row = Box.createHorizontalBox();
            timeLabel = new PromptLabel("12:00AM");
            animatedComponents[1] = timeLabel;
            colorsFrom[1] = timeLabel.getBackground();
            colorsCorrect[1] = FontsAndColors.COLOR_DARK_BACKGROUND_CORRECT;
            colorsIncorrect[1] = FontsAndColors.COLOR_DARK_BACKGROUND_INCORRECT;
            // timeLabel.setFont(timeLabel.getFont().deriveFont(24f));
            // row.add(Box.createHorizontalGlue());
            row.add(timeLabel);
            // row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(Box.createHorizontalGlue());
            row.add(new BasicLabel("Il est "));
            inputField = new CustomTextField(16);
            inputField.addActionListener(this::inputSubmitted);
            row.add(inputField);
            row.add(new BasicLabel("."));
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        add(rowContainer);

        pack();
    }

    private void pickNextAnswer() {
        answer = FrenchTime.generateRandomTime();
        timeLabel.setText(answer.getDisplay());
        inputField.setText("");
    }

    private void checkAnswer() {
        String input = inputField.getText();
        if (answer.checkAnswer(input)) {
            ColorAnimationEngine.tryLockAndAnimateIfUnlocked(animatedComponents, colorsFrom, colorsCorrect);
            pickNextAnswer();
        } else {
            ColorAnimationEngine.tryLockAndAnimateIfUnlocked(animatedComponents, colorsFrom, colorsIncorrect);
        }
    }

    private void inputSubmitted(ActionEvent e) {
        checkAnswer();
    }

}
