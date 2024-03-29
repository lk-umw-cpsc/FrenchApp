import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import swingcustom.BasicLabel;
import swingcustom.CustomButton;
import swingcustom.CustomTextField;
import swingcustom.FontsAndColors;
import swingcustom.HeaderLabel;

public class ConjugationFrame extends JFrame implements WindowListener, MouseListener {
    
    private final JFrame parent;

    private final Random rng;

    private JLabel infinitiveLabel;
    private JLabel infinitiveTranslationLabel;

    private JTextField[] inputFields;
    private String[] columns;

    private CustomButton checkButton;

    private Verb answer;

    private final Color CORRECT_ANSWER_COLOR = new Color(198, 255, 189);
    private final Color INCORRECT_ANSWER_COLOR = new Color(255, 189, 189);
    private final Color REVEALED_ANSWER_COLOR = FontsAndColors.COLOR_REVEALED_ANSWER_BACKGROUND;
    private final Color DEFAULT_COLOR;

    private List<Verb> remainingVerbs;
    private Map<JTextField, Integer> textfieldToIndexMap;

    private JPopupMenu rightClickMenu;
    private JTextField rightClickTarget;

    public ConjugationFrame(JFrame parent, VerbGroup verbGroup) {
        super("Pratiquer les conjugaisons");
        this.parent = parent;

        rng = new Random();

        textfieldToIndexMap = new HashMap<>();

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

        rightClickMenu = new JPopupMenu();
        JMenuItem revealOption = new JMenuItem("Reveal answer");
        revealOption.addActionListener(this::revealOptionChosen);
        rightClickMenu.add(revealOption);

        Box row;

        Box rowContainer = Box.createVerticalBox();
        rowContainer.setBorder(new EmptyBorder(16, 16, 8, 16));
        rowContainer.setOpaque(true);
        rowContainer.setBackground(FontsAndColors.APP_BACKGROUND);
                
        row = Box.createHorizontalBox();
            row.add(Box.createHorizontalGlue());
            
            row.add(new HeaderLabel(" Conjugate "));
            infinitiveLabel = new HeaderLabel("");
            infinitiveLabel.setFont(
                infinitiveLabel.getFont().deriveFont(Font.ITALIC | Font.BOLD));
            row.add(infinitiveLabel);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(Box.createHorizontalGlue());
            row.add(infinitiveTranslationLabel = new BasicLabel(""));
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
            row.add(new BasicLabel(columns[1 + left] + " "));
            JTextField tf = new CustomTextField(8);
            tf.setFont(tf.getFont().deriveFont(16f));
            tf.addActionListener(this::formSubmitted);
            tf.addMouseListener(this);
            inputFields[left] = tf;
            textfieldToIndexMap.put(tf, left);
            row.add(tf);
            row.add(Box.createHorizontalGlue());
            if (right < numColumns) {
                row.add(Box.createHorizontalStrut(32));
                row.add(new BasicLabel(columns[1 + right]));
                tf = new CustomTextField(8);
                tf.setFont(tf.getFont().deriveFont(16f));
                tf.setMaximumSize(tf.getPreferredSize());
                tf.addMouseListener(this);
                tf.addActionListener(this::formSubmitted);
                inputFields[right] = tf;
                textfieldToIndexMap.put(tf, right);
                row.add(tf);
            } else {
                tf.setMaximumSize(tf.getPreferredSize());
            }
            rowContainer.add(row);
        }

        rowContainer.add(Box.createVerticalStrut(8));

        row = Box.createHorizontalBox();
            checkButton = new CustomButton("Check");
            checkButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            checkButton.addButtonListener(this::buttonPressed);
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
    }

    private void buttonPressed() {
        formSubmitted(null);
    }

    private void revealOptionChosen(ActionEvent e) {
        int index = textfieldToIndexMap.get(rightClickTarget);
        rightClickTarget.setEnabled(false);
        rightClickTarget.setText(answer.getConjugation(columns[index]));
        rightClickTarget.setBackground(REVEALED_ANSWER_COLOR);
    }

    private void formSubmitted(ActionEvent e) {
        boolean correct = true;

        JTextField firstIncorrect = null;

        for (int i = 0, numFields = inputFields.length; i < numFields; i++) {
            JTextField tf = inputFields[i];
            if (!tf.isEnabled()) {
                continue;
            }
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
        infinitiveTranslationLabel.setText(answer.translate(answer.getInfinitive()) + " ");

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

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (!((Component)e.getSource()).isEnabled()) {
                return;
            }
            rightClickTarget = (JTextField)e.getSource();
            rightClickMenu.show((Component)e.getSource(), e.getX(), e.getY());
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

}