package swingextended;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class AutoShowParentFrame extends JFrame implements WindowListener {
    
    private final JFrame parent;

    public AutoShowParentFrame(JFrame parent, String title) {
        super(title);
        this.parent = parent;
        if (parent == null) {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }
        addWindowListener(this);
    }

    public AutoShowParentFrame(JFrame parent) {
        this.parent = parent;
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
