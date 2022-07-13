import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import swingcustom.CustomButton;
import swingcustom.CustomRadioButton;
import swingcustom.FontsAndColors;
import swingcustom.HeaderLabel;

public class NumberPracticeModeChooserFrame extends JFrame implements WindowListener {

    private final JFrame parent;
    
    private JRadioButton[] buttons = new JRadioButton[8];

    public NumberPracticeModeChooserFrame(JFrame parent) {
        this.parent = parent;

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        Box rowContainer = Box.createVerticalBox();
        rowContainer.setBorder(new EmptyBorder(16, 16, 8, 16));
        rowContainer.setOpaque(true);
        rowContainer.setBackground(FontsAndColors.APP_BACKGROUND);

        JRadioButton radioButton;
        ButtonGroup group = new ButtonGroup();

        Box row = Box.createHorizontalBox();
            row.add(Box.createHorizontalGlue());
            JLabel label = new HeaderLabel("Practice numbers through...");
            label.setFont(label.getFont().deriveFont(Font.BOLD));
            row.add(label);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        rowContainer.add(Box.createVerticalStrut(8));

        row = Box.createHorizontalBox();
            row.add(radioButton = new CustomRadioButton("Ten (0-10)"));
            buttons[0] = radioButton;
            group.add(radioButton);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(radioButton = new CustomRadioButton("Teens (0-19)"));
            buttons[1] = radioButton;
            group.add(radioButton);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(radioButton = new CustomRadioButton("Sixty (0-69)"));
            buttons[2] = radioButton;
            group.add(radioButton);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(radioButton = new CustomRadioButton("One hundred (0-100)"));
            buttons[3] = radioButton;
            group.add(radioButton);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(radioButton = new CustomRadioButton("Hundreds (1-999)"));
            buttons[4] = radioButton;
            group.add(radioButton);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(radioButton = new CustomRadioButton("Thousands (1-999,999)"));
            buttons[5] = radioButton;
            group.add(radioButton);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(radioButton = new CustomRadioButton("Millions (1-999,999,999)"));
            buttons[6] = radioButton;
            group.add(radioButton);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(radioButton = new CustomRadioButton("Billions (1-999,999,999,999)"));
            buttons[7] = radioButton;
            group.add(radioButton);
            row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        rowContainer.add(Box.createVerticalStrut(8));

        row = Box.createHorizontalBox();
            CustomButton practiceButton = new CustomButton("Start practicing!");
            Dimension d = practiceButton.getPreferredSize();
            practiceButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, d.height));
            practiceButton.addButtonListener(this::practiceButtonPressed);
            row.add(practiceButton);
        rowContainer.add(row);

        buttons[0].setSelected(true);

        add(rowContainer);

        addWindowListener(this);

        pack();
    }

    private void practiceButtonPressed() {
        for (int i = 0, n = buttons.length; i < n; i++) {
            if (buttons[i].isSelected()) {
                NumberPracticeFrame npf = new NumberPracticeFrame(this, i - 4);
                npf.setLocationRelativeTo(this);
                npf.setVisible(true);
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
