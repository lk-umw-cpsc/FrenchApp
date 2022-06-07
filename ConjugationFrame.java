import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class ConjugationFrame extends JFrame implements WindowListener {
    
    private final JFrame parent;

    private final Random rng;

    private JLabel infinitiveLabel;
    private JLabel infinitiveTranslationLabel;

    private JTextField[] inputFields;
    private String[] columns;

    private JButton checkButton;

    private Verb answer;

    private final Color CORRECT_ANSWER_COLOR = new Color(198, 255, 189);
    private final Color INCORRECT_ANSWER_COLOR = new Color(255, 189, 189);
    private final Color DEFAULT_COLOR;

    private List<Verb> remainingVerbs;

    public ConjugationFrame(JFrame parent, VerbGroup verbGroup) {
        super("Pratiquer les conjugaisons");
        this.parent = parent;

        rng = new Random();

        remainingVerbs = new ArrayList<>();
        for (Verb v : verbGroup.getVerbs()) {
            remainingVerbs.add(v);
        }

        if (parent != null) {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } else {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        setResizable(false);

        Box row;

        Box rowContainer = Box.createVerticalBox();
        rowContainer.setBorder(new EmptyBorder(16, 16, 8, 16));
                
        row = Box.createHorizontalBox();
            row.add(Box.createHorizontalGlue());
            
            row.add(new JLabel(" Conjugate "));
            infinitiveLabel = new JLabel();
            infinitiveLabel.setFont(
                infinitiveLabel.getFont().deriveFont(Font.ITALIC | Font.BOLD));
            row.add(infinitiveLabel);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(Box.createHorizontalGlue());
            row.add(infinitiveTranslationLabel = new JLabel());
            infinitiveTranslationLabel.setFont(
                infinitiveTranslationLabel.getFont().deriveFont(Font.ITALIC));
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        rowContainer.add(Box.createVerticalStrut(8));

        String[] columns = verbGroup.getColumns();
        final int numColumns = columns.length - 1;
        this.columns = new String[numColumns];

        for (int i = 0; i < numColumns; i++) {
            this.columns[i] = columns[i + 1];
        }

        inputFields = new JTextField[numColumns];

        final int numRows = (numColumns / 2) + (numColumns % 2);
        for (int left = 0, right = numRows; left < numRows; left++, right++) {
            row = Box.createHorizontalBox();
            row.add(new JLabel(columns[1 + left] + " "));
            JTextField tf = new JTextField(8);
            tf.addActionListener(this::formSubmitted);
            inputFields[left] = tf;
            row.add(tf);
            row.add(Box.createHorizontalGlue());
            if (right < numColumns) {
                row.add(Box.createHorizontalStrut(32));
                row.add(new JLabel(columns[1 + right]));
                tf = new JTextField(8);
                tf.setMaximumSize(tf.getPreferredSize());
                tf.addActionListener(this::formSubmitted);
                inputFields[right] = tf;
                row.add(tf);
            } else {
                tf.setMaximumSize(tf.getPreferredSize());
            }
            rowContainer.add(row);
        }

        // rowContainer.add(Box.createVerticalStrut(8));

        row = Box.createHorizontalBox();
            checkButton = new JButton("Check");
            checkButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            checkButton.addActionListener(this::formSubmitted);
            row.add(checkButton);
        rowContainer.add(row);

        JTextField colorProp = new JTextField();
        DEFAULT_COLOR = colorProp.getBackground();

        setFocusTraversalPolicy(new BasicTraversalPolicy(inputFields));

        answer = remainingVerbs.remove(rng.nextInt(remainingVerbs.size()));
        updateFormWithNextVerb();

        add(rowContainer);
        addWindowListener(this);

        pack();
        setLocationRelativeTo(parent);
    }

    private void formSubmitted(ActionEvent e) {
        boolean correct = true;

        JTextField firstIncorrect = null;

        for (int i = 0, numFields = inputFields.length; i < numFields; i++) {
            JTextField tf = inputFields[i];
            if (tf.getText().equals(answer.getConjugation(columns[i]))) {
                tf.setBackground(CORRECT_ANSWER_COLOR);
                tf.setEnabled(false);
            } else {
                tf.setBackground(INCORRECT_ANSWER_COLOR);
                correct = false;
                if (firstIncorrect == null) {
                    firstIncorrect = tf;
                }
            }
        }

        if (correct) {
            checkButton.setEnabled(false);
            new Thread(this::pickNextVerb).start();
        } else {
            firstIncorrect.requestFocus();
            firstIncorrect.selectAll();
        }
    }

    private void pickNextVerb() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {}
        if (remainingVerbs.isEmpty()) {
            if (parent == null) {
                System.exit(0);
            } else {
                parent.setLocationRelativeTo(this);
                parent.setVisible(true);
                dispose();
            }
        } else {
            answer = remainingVerbs.remove(rng.nextInt(remainingVerbs.size()));
            SwingUtilities.invokeLater(this::updateFormWithNextVerb);
        }
    }

    private void updateFormWithNextVerb() {
        infinitiveLabel.setText(answer.getInfinitive() + " ");
        infinitiveTranslationLabel.setText(answer.translate(answer.getInfinitive()));

        for (JTextField tf : inputFields) {
            resetField(tf);
        }

        checkButton.setEnabled(true);
        inputFields[0].requestFocus();
    }

    private void resetField(JTextField field) {
        field.setBackground(DEFAULT_COLOR);
        field.setText("");
        field.setEnabled(true);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (parent != null) {
            parent.setLocationRelativeTo(this);
            parent.setVisible(true);
        } else {
            System.exit(0);
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