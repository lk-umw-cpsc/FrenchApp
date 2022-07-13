import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
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
    private Component outerLayer, innerLayer;
    private Color innerDefault, outerDefault;

    private volatile Thread animationThread;

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

        final int padding = ApplicationUIConstants.FRAME_CONTENT_PADDING;
        rowContainer.setBorder(new EmptyBorder(padding, padding, padding, padding));
        outerLayer = rowContainer;
        outerDefault = rowContainer.getBackground();

        // will hold each row of components
        Box row;

        row = Box.createHorizontalBox();
            timeLabel = new PromptLabel("12:00AM");
            innerLayer = timeLabel;
            innerDefault = timeLabel.getBackground();
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

    private void animate(Color innerTo, Color outerTo) {

        double step = 1.0 / 60 * 10;
        double d = step;
        int wait = 1000 / 60;
        Color innerColor = innerDefault;
        Color outerColor = outerDefault;

        while (d < 1.0) {
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {}
            Color newInner = interpolate(innerColor, innerTo, d);
            Color newOuter = interpolate(outerColor, outerTo, d);
            SwingUtilities.invokeLater(() -> {
                innerLayer.setBackground(newInner);
                outerLayer.setBackground(newOuter);
            });
            d += step;
        }
        d = step;
        while (d < 1.0) {
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {}
            Color newInner = interpolate(innerTo, innerColor, d);
            Color newOuter = interpolate(outerTo, outerColor, d);
            SwingUtilities.invokeLater(() -> {
                innerLayer.setBackground(newInner);
                outerLayer.setBackground(newOuter);
            });
            d += step;
        }
        animationThread = null;
    }

    public void animateCorrect() {
        animate(FontsAndColors.COLOR_DARK_BACKGROUND_CORRECT, FontsAndColors.COLOR_APP_BACKGROUND_CORRECT);
    }

    public void animateIncorrect() {
        animate(FontsAndColors.COLOR_DARK_BACKGROUND_INCORRECT, FontsAndColors.COLOR_APP_BACKGROUND_INCORRECT);
    }

    private Color interpolate(Color from, Color to, double f) {
        int r = from.getRed();
        int g = from.getGreen();
        int b = from.getBlue();

        int dr = to.getRed() - r;
        int dg = to.getGreen() - g;
        int db = to.getBlue() - b;

        return new Color(
            (int)(r + f * dr),
            (int)(g + f * dg),
            (int)(b + f * db)
        );
    }

    private void pickNextAnswer() {
        answer = FrenchTime.generateRandomTime();
        timeLabel.setText(answer.getDisplay());
        inputField.setText("");
    }

    private void checkAnswer() {
        String input = inputField.getText();
        if (answer.checkAnswer(input)) {
            if (animationThread == null) {
                animationThread = new Thread(this::animateCorrect);
                animationThread.start();   
            }
            pickNextAnswer();
        } else {
            if (animationThread == null) {
                animationThread = new Thread(this::animateIncorrect);
                animationThread.start();
            }
        }
    }

    private void inputSubmitted(ActionEvent e) {
        checkAnswer();
    }

}
