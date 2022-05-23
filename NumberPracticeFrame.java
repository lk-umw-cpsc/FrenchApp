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

    private String[] numbers = new String[101];
    private List<Integer> numbersRemaining;
    private final Random rng;
    private String answer;
    
    public NumberPracticeFrame(JFrame parent) {
        super("Pratiquer les nombres");

        this.parent = parent;

        loadNumbers();

        numbersRemaining = new ArrayList<>(256);
        addAllNumbersToRemainingList();

        rng = new Random();

        int n = pickNumber();
        answer = numbers[n];

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
            promptLabel = new JLabel("2 000 000 000");
            promptLabel.setFont(promptLabel.getFont().deriveFont(24.0f));
            row.add(Box.createHorizontalGlue());
            row.add(promptLabel);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);
        rowContainer.add(Box.createVerticalStrut(4));

            inputField = new JTextField(24);
            inputField.setHorizontalAlignment(JTextField.CENTER);
            inputField.setFont(inputField.getFont().deriveFont(18.0f));
            inputField.addActionListener(this::inputSubmitted);
            Dimension pref = inputField.getPreferredSize();
            inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height));
        rowContainer.add(inputField);

        add(rowContainer);
        promptLabel.setText(Integer.toString(n));
        pack();
        setLocationRelativeTo(parent);
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
        for (int i = 0; i < numbers.length; i++) {
            numbersRemaining.add(i);
        }
    }

    /**
     * Removes a random number from the remaining numbers
     * @return the number picked
     */
    private int pickNumber() {
        return rng.nextInt(numbersRemaining.size());
    }

    private void inputSubmitted(ActionEvent e) {
        String input = inputField.getText().toLowerCase().strip();
        if (input.isBlank()) {
            return;
        }
        if (input.equals(answer)) {
            inputField.setEnabled(false);
            promptLabel.setText("Correct!");
            promptLabel.setForeground(Color.GREEN);
            new Thread(this::waitThenPickNext).start();
        } else {
            return;
        }
        if (numbersRemaining.isEmpty()) {
            addAllNumbersToRemainingList();
        }
    }

    private void pickNext() {
        int n = pickNumber();
        answer = numbers[n];
        promptLabel.setForeground(Color.BLACK);
        promptLabel.setText(Integer.toString(n));
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
