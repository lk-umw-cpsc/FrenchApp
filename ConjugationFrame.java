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

    private JTextField jeField;
    private JTextField tuField;
    private JTextField ilElleOnField;
    private JTextField nousField;
    private JTextField vousField;
    private JTextField ilsEllesField;

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
            infinitiveLabel = new JLabel("manger ");
            infinitiveLabel.setFont(infinitiveLabel.getFont().deriveFont(Font.ITALIC));
            row.add(infinitiveLabel);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        rowContainer.add(Box.createVerticalStrut(8));

        row = Box.createHorizontalBox();
            row.add(new JLabel("je/j' "));
            row.add(jeField = new JTextField(8));
            row.add(Box.createHorizontalGlue());
            row.add(Box.createHorizontalStrut(32));
            row.add(new JLabel("nous "));
            row.add(nousField = new JTextField(8));
        rowContainer.add(row);

        // rowContainer.add(Box.createVerticalStrut(4));

        row = Box.createHorizontalBox();
            row.add(new JLabel("tu "));
            row.add(tuField = new JTextField(8));
            row.add(Box.createHorizontalGlue());
            row.add(Box.createHorizontalStrut(32));
            row.add(new JLabel("vous "));
            row.add(vousField = new JTextField(8));
        rowContainer.add(row);

        // rowContainer.add(Box.createVerticalStrut(4));

        row = Box.createHorizontalBox();
            row.add(new JLabel("il/elle/on "));
            row.add(ilElleOnField = new JTextField(8));
            row.add(Box.createHorizontalGlue());
            row.add(Box.createHorizontalStrut(32));
            row.add(new JLabel("ils/elles "));
            row.add(ilsEllesField = new JTextField(8));
        rowContainer.add(row);

        rowContainer.add(Box.createVerticalStrut(8));

        row = Box.createHorizontalBox();
            checkButton = new JButton("Check");
            checkButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            checkButton.addActionListener(this::formSubmitted);
            row.add(checkButton);
        rowContainer.add(row);

        setFocusTraversalPolicy(new BasicTraversalPolicy(
            jeField, tuField, ilElleOnField, nousField, 
            vousField, ilsEllesField, checkButton
        ));

        jeField.addActionListener(this::formSubmitted);
        tuField.addActionListener(this::formSubmitted);
        ilElleOnField.addActionListener(this::formSubmitted);
        nousField.addActionListener(this::formSubmitted);
        vousField.addActionListener(this::formSubmitted);
        ilsEllesField.addActionListener(this::formSubmitted);

        DEFAULT_COLOR = jeField.getBackground();

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

        if (jeField.getText().equals(answer.getConjugation("je"))) {
            jeField.setBackground(CORRECT_ANSWER_COLOR);
            jeField.setEnabled(false);
        } else {
            jeField.setBackground(INCORRECT_ANSWER_COLOR);
            correct = false;
            firstIncorrect = jeField;
        }

        if (tuField.getText().equals(answer.getConjugation("tu"))) {
            tuField.setBackground(CORRECT_ANSWER_COLOR);
            tuField.setEnabled(false);
        } else {
            tuField.setBackground(INCORRECT_ANSWER_COLOR);
            correct = false;
            if (firstIncorrect == null) {
                firstIncorrect = tuField;
            }
        }

        if (ilElleOnField.getText().equals(answer.getConjugation("il/elle/on"))) {
            ilElleOnField.setBackground(CORRECT_ANSWER_COLOR);
            ilElleOnField.setEnabled(false);
        } else {
            ilElleOnField.setBackground(INCORRECT_ANSWER_COLOR);
            correct = false;
            if (firstIncorrect == null) {
                firstIncorrect = ilElleOnField;
            }
        }

        if (nousField.getText().equals(answer.getConjugation("nous"))) {
            nousField.setBackground(CORRECT_ANSWER_COLOR);
            nousField.setEnabled(false);
        } else {
            nousField.setBackground(INCORRECT_ANSWER_COLOR);
            correct = false;
            if (firstIncorrect == null) {
                firstIncorrect = nousField;
            }
        }

        if (vousField.getText().equals(answer.getConjugation("vous"))) {
            vousField.setBackground(CORRECT_ANSWER_COLOR);
            vousField.setEnabled(false);
        } else {
            vousField.setBackground(INCORRECT_ANSWER_COLOR);
            correct = false;
            if (firstIncorrect == null) {
                firstIncorrect = vousField;
            }
        }

        if (ilsEllesField.getText().equals(answer.getConjugation("ils/elles"))) {
            ilsEllesField.setBackground(CORRECT_ANSWER_COLOR);
            ilsEllesField.setEnabled(false);
        } else {
            ilsEllesField.setBackground(INCORRECT_ANSWER_COLOR);
            correct = false;
            if (firstIncorrect == null) {
                firstIncorrect = ilsEllesField;
            }
        }

        if (correct) {
            checkButton.setEnabled(false);
            new Thread(this::pickNextVerb).start();
        } else {
            firstIncorrect.requestFocus();
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

        resetField(jeField);
        resetField(tuField);
        resetField(ilElleOnField);
        resetField(nousField);
        resetField(vousField);
        resetField(ilsEllesField);

        checkButton.setEnabled(true);
        jeField.requestFocus();
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
