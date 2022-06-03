import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class ConjugationFrame extends JFrame implements WindowListener {
    
    private final JFrame parent;

    public ConjugationFrame(JFrame parent) {
        super("Pratiquer les conjugaisons");
        this.parent = parent;

        if (parent != null) {
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        } else {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        Box rowContainer = Box.createVerticalBox();
        rowContainer.setBorder(new EmptyBorder(16, 16, 16, 16));
        
        Box row;
        
        row = Box.createHorizontalBox();
            // row.add(Box.createHorizontalGlue());
            row.add(new JLabel("<html><body style='text-align: center'>Conjugate <i>manger</i></body></html>"));
            // row.add(Box.createHorizontalGlue());
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(new JLabel("je "));
            row.add(new JTextField(8));
            row.add(Box.createHorizontalGlue());
            row.add(Box.createHorizontalStrut(32));
            row.add(new JLabel("nous "));
            row.add(new JTextField(8));
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(new JLabel("tu "));
            row.add(new JTextField(8));
            row.add(Box.createHorizontalGlue());
            row.add(Box.createHorizontalStrut(32));
            row.add(new JLabel("vous "));
            row.add(new JTextField(8));
        rowContainer.add(row);

        row = Box.createHorizontalBox();
            row.add(new JLabel("il/elle/on "));
            row.add(new JTextField(8));
            row.add(Box.createHorizontalGlue());
            row.add(Box.createHorizontalStrut(32));
            row.add(new JLabel("ils/elles "));
            row.add(new JTextField(8));
        rowContainer.add(row);

        add(rowContainer);

        pack();
        setLocationRelativeTo(parent);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ConjugationFrame(null).setVisible(true);
        });
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (parent != null) {
            setLocationRelativeTo(parent);
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
