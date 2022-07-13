package swingextended;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class LabelButton extends JLabel implements MouseListener{
    
    private final List<ButtonListener> listeners;

    public LabelButton(String text) {
        super(text);
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(new EmptyBorder(6, 12, 6, 12));
        setOpaque(true);
        listeners = new ArrayList<>();
        addMouseListener(this);
    }

    public void addButtonListener(ButtonListener l) {
        listeners.add(l);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (ButtonListener l : listeners) {
            l.buttonPressed();
        }        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

}
