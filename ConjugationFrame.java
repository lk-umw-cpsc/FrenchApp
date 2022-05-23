import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ConjugationFrame extends JFrame implements WindowListener {
    
    private final JFrame parent;

    public ConjugationFrame(JFrame parent) {
        super("Pratiquer les conjugaisons");
        this.parent = parent;

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        Box rowContainer = Box.createVerticalBox();


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
