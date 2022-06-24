import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import swingextended.ApplicationUIConstants;
import swingextended.AutoShowParentFrame;

public class TimePracticeFrame extends AutoShowParentFrame {
    
    private JLabel timeLabel;
    private JTextField inputField;

    private FrenchTime answer;

    public TimePracticeFrame(JFrame parent) {
        super(parent, "Quelle heure est-il?");

        setResizable(false);

        initializeChildComponents();
        pickNextAnswer();
    }

    private void initializeChildComponents() {
        // lay rows out vertically
        Box rowContainer = Box.createVerticalBox();

        final int padding = ApplicationUIConstants.FRAME_CONTENT_PADDING;
        rowContainer.setBorder(new EmptyBorder(padding, padding, padding, padding));

        // will hold each row of components
        Box row;

        row = Box.createHorizontalBox();
            timeLabel = new JLabel("12:00AM");
            timeLabel.setFont(timeLabel.getFont().deriveFont(24f));
            row.add(Box.createHorizontalGlue());
            row.add(timeLabel);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(Box.createHorizontalGlue());
            row.add(new JLabel("Il est "));
            inputField = new JTextField(16);
            inputField.addActionListener(this::inputSubmitted);
            row.add(inputField);
            row.add(new JLabel("."));
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
            System.out.println("correct");
            pickNextAnswer();
        } else {
            System.out.println("expected " + String.join("\n", answer.getPossibleAnswers()));
        }
    }

    private void inputSubmitted(ActionEvent e) {
        checkAnswer();
    }

}
