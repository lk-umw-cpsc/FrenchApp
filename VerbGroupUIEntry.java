import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class VerbGroupUIEntry extends JPanel implements MouseListener {

    private final VerbGroup verbGroup;
    private final VerbGroupChooserFrame parent;

    public VerbGroupUIEntry(VerbGroupChooserFrame parent, File f) {
        this.parent = parent;
        verbGroup = new VerbGroup(f);

        setBorder(new EmptyBorder(8, 8, 8, 8));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JLabel label;
        add(label = new JLabel(verbGroup.getDescription()));
        setBackground(label.getBackground());
        
        add(Box.createHorizontalGlue()); // left-align

        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
            parent.groupSelected(verbGroup);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}