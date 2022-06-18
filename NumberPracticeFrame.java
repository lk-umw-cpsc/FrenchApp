import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class NumberPracticeFrame extends JFrame implements WindowListener {

    private final JFrame parent;

    private JLabel promptLabel;

    private JTextField inputField;

    private static final int BILLIONS = 3;
    private static final int MILLIONS = 2;
    private static final int THOUSANDS = 1;
    private static final int HUNDREDS = 0;
    private static final int UP_TO_ONE_HUNDRED = -1;
    private static final int UP_TO_SIXTY = -2;
    private static final int UP_TO_TEENS = -3;
    private static final int UP_TO_TEN = -4;

    private static final int[] RANGE_CAPS = { 11, 20, 70, 101 };

    private static final Color FONT_COLOR_CORRECT = new Color(0, 189, 0);

    private int choice;
    private int range;

    private boolean usingList;

    private String[] numbers = new String[101];
    private String[] namesSingular = { "cent", "mille", "million", "milliard" };
    private String[] namesPlural = { "cents", "mille",  "millions", "milliards" };
    private List<Integer> numbersRemaining;
    private final Random rng;
    private String answer;
    private FrenchNumber currentNumber;
    
    public NumberPracticeFrame(JFrame parent, int choice) {
        super("Pratiquer les nombres");

        this.parent = parent;
        this.choice = choice;

        loadNumbers();

        rng = new Random();

        usingList = choice < 0;

        int n = 0;

        if (usingList) {
            range = RANGE_CAPS[choice + RANGE_CAPS.length];
            numbersRemaining = new ArrayList<>(2 * range);

            n = pickSmallNumber();
            answer = numbers[n];
        } else {
            currentNumber = pickLargeNumber();
            answer = currentNumber.getWordsString();
        }

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setResizable(false);
        addWindowListener(this);

        Box rowContainer = Box.createVerticalBox();
        rowContainer.setBorder(new EmptyBorder(24, 8, 16, 8));

        Box row;

        row = Box.createHorizontalBox();
            row.add(Box.createHorizontalGlue());
            row.add(new JLabel("Convert to French:"));
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        rowContainer.add(Box.createVerticalStrut(8));
        
        row = Box.createHorizontalBox();
            promptLabel = new JLabel("000 000 000 000");
            promptLabel.setFont(promptLabel.getFont().deriveFont(24.0f));
            row.add(Box.createHorizontalGlue());
            row.add(promptLabel);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);
        rowContainer.add(Box.createVerticalStrut(4));

            inputField = new JTextField(32);
            inputField.setHorizontalAlignment(JTextField.CENTER);
            inputField.setFont(inputField.getFont().deriveFont(18.0f));
            inputField.addActionListener(this::inputSubmitted);
            Dimension pref = inputField.getPreferredSize();
            inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height));
        rowContainer.add(inputField);

        add(rowContainer);
        if (usingList) {
            promptLabel.setText(Integer.toString(n));
        } else {
            promptLabel.setText(currentNumber.getDigitsString());
        }
        pack();
        setLocationRelativeTo(parent);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NumberPracticeFrame::createAndShow);
    }

    private static void createAndShow() {
        NumberPracticeFrame n = new NumberPracticeFrame(null, BILLIONS);
        n.setVisible(true);
    }

    private FrenchNumber generateNumberString(int places) {
        // if (places < 0) {
        //     return numbers[rng.nextInt(RANGE_CAPS[places + RANGE_CAPS.length])];
        // }
        FrenchNumber n = null;
        for (int place = places; place >= HUNDREDS; place--) {
            if (n == null && place == HUNDREDS
                    || rng.nextBoolean()) {
                // String placeString = generatePlace(place);
                // s += placeString + " ";
                if (n == null) {
                    n = new FrenchNumber(generatePlace(place));
                } else {
                    n.update(generatePlace(place));
                }
            } else if (n != null) {
                n.update(null);
            }
        }
        return n;
    }

    private int generateRandomHundreds() {
        return rng.nextInt(999) + 1;
    }

    private FrenchNumberPiece generatePlace(int place) {
        int n = generateRandomHundreds();
        int hundreds = n / 100;
        int tensOnes = n % 100;

        String s = "";

        if (hundreds > 0) {
            if (hundreds > 1) {
                s += numbers[hundreds];
                s += " ";
            }

            if (place == HUNDREDS && hundreds > 1 && tensOnes == 0) {
                s += namesPlural[HUNDREDS];
            } else {
                s += namesSingular[HUNDREDS];
            }
            if (tensOnes > 0) {
                s += " ";
            }
        }

        if (tensOnes > 0) {
            if (tensOnes == 80 && place > HUNDREDS) {
                s += "quatre-vingt";
            } else if (n != 1 || place > THOUSANDS) {
                s += numbers[tensOnes]; 
            }
        }

        if (n > 0 && place > HUNDREDS) {
            if (!s.isEmpty()) {
                s += " ";
            }
            if (n > 1) {
                s += namesPlural[place];                
            } else {
                s += namesSingular[place];
            }
        }

        return new FrenchNumberPiece(n, s);
    }

    /**
     * Loads words for French numbers into the class's data structures
     */
    private void loadNumbers() {
        final File inputFile = new File("data/les-nombres.txt");
        try(Scanner s = new Scanner(inputFile)) {
            for (int i = 0; i <= 100; i++) {
                numbers[i] = s.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't open " + inputFile);
        }
    }

    private void addAllNumbersToRemainingList() {
        for (int i = 0; i < range; i++) {
            numbersRemaining.add(i);
        }
    }

    /**
     * Removes a random number from the remaining numbers
     * @return the number picked
     */
    private int pickSmallNumber() {
        if (numbersRemaining.isEmpty()) {
            addAllNumbersToRemainingList();
        }
        return numbersRemaining.remove(rng.nextInt(numbersRemaining.size()));
    }

    private FrenchNumber pickLargeNumber() {
        return generateNumberString(choice);
    }

    private void inputSubmitted(ActionEvent e) {
        if (!inputField.isEnabled()) {
            return;
        }
        String input = inputField.getText().toLowerCase().strip();
        if (input.isBlank()) {
            return;
        }
        if (input.equals(answer)) {
            inputField.setEnabled(false);
            promptLabel.setText("Correct!");
            promptLabel.setForeground(FONT_COLOR_CORRECT);
            revalidate();
            repaint();
            new Thread(this::waitThenPickNext).start();
        } else {
            System.out.println("Expected: " + answer);
            return;
        }
    }

    private void pickNext() {
        String promptText;
        if (usingList) {
            int n = pickSmallNumber();
            answer = numbers[n];
            promptText = Integer.toString(n);
        } else {
            currentNumber = pickLargeNumber();
            answer = currentNumber.getWordsString();
            promptText = currentNumber.getDigitsString();
        }
        promptLabel.setForeground(Color.BLACK);
        promptLabel.setText(promptText);
        inputField.setText("");
        inputField.setEnabled(true);
        pack();
    }

    private void waitThenPickNext() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
        SwingUtilities.invokeLater(this::pickNext);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        
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
