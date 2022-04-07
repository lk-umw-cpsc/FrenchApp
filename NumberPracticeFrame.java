import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class NumberPracticeFrame extends JFrame implements WindowListener {

    private final JFrame parent;

    private JLabel promptLabel;

    private JTextField inputField;
    
    public NumberPracticeFrame(JFrame parent) {
        super("Pratiquer les numeros");

        this.parent = parent;

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

            inputField = new JTextField(24);
            inputField.setHorizontalAlignment(JTextField.CENTER);
            inputField.setFont(inputField.getFont().deriveFont(18.0f));
            inputField.addActionListener(this::inputSubmitted);
            Dimension pref = inputField.getPreferredSize();
            inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height));
        rowContainer.add(inputField);

        add(rowContainer);
        pack();
        setLocationRelativeTo(parent);
    }

    public static void main(String[] args) {
        new NumberPracticeFrame(null).setVisible(true);
    }

    private void inputSubmitted(ActionEvent e) {

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
