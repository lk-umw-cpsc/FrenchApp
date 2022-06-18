import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

public class NumberPracticeModeChooserFrame extends JFrame implements WindowListener {

    private final JFrame parent;
    
    private JRadioButton[] buttons = new JRadioButton[8];

    public NumberPracticeModeChooserFrame(JFrame parent) {
        this.parent = parent;

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        Box rowContainer = Box.createVerticalBox();
        rowContainer.setBorder(new EmptyBorder(16, 16, 8, 16));

        JRadioButton radioButton;
        ButtonGroup group = new ButtonGroup();

        Box row = Box.createHorizontalBox();
            row.add(Box.createHorizontalGlue());
            JLabel label = new JLabel("Practice numbers through...");
            label.setFont(label.getFont().deriveFont(Font.BOLD));
            row.add(label);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        rowContainer.add(Box.createVerticalStrut(8));

        row = Box.createHorizontalBox();
            row.add(radioButton = new JRadioButton("Ten (0-10)"));
            buttons[0] = radioButton;
            group.add(radioButton);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(radioButton = new JRadioButton("Teens (0-19)"));
            buttons[1] = radioButton;
            group.add(radioButton);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(radioButton = new JRadioButton("Sixty (0-69)"));
            buttons[2] = radioButton;
            group.add(radioButton);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(radioButton = new JRadioButton("One hundred (0-100)"));
            buttons[3] = radioButton;
            group.add(radioButton);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(radioButton = new JRadioButton("Hundreds (1-999)"));
            buttons[4] = radioButton;
            group.add(radioButton);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(radioButton = new JRadioButton("Thousands (1-999,999)"));
            buttons[5] = radioButton;
            group.add(radioButton);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(radioButton = new JRadioButton("Millions (1-999,999,999)"));
            buttons[6] = radioButton;
            group.add(radioButton);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(radioButton = new JRadioButton("Billions (1-999,999,999,999)"));
            buttons[7] = radioButton;
            group.add(radioButton);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        rowContainer.add(Box.createVerticalStrut(8));

        row = Box.createHorizontalBox();
            JButton practiceButton = new JButton("Start practicing!");
            Dimension d = practiceButton.getPreferredSize();
            practiceButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, d.height));
            practiceButton.addActionListener(this::practiceButtonPressed);
            row.add(practiceButton);
        rowContainer.add(row);

        buttons[0].setSelected(true);

        add(rowContainer);

        addWindowListener(this);

        pack();
    }

    private void practiceButtonPressed(ActionEvent e) {
        for (int i = 0, n = buttons.length; i < n; i++) {
            if (buttons[i].isSelected()) {
                new NumberPracticeFrame(this, i - 4).setVisible(true);
                setVisible(false);
                return;
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (parent != null) {
            parent.setLocationRelativeTo(this);
            parent.setVisible(true);
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
