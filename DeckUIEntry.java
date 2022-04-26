import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DeckUIEntry extends JPanel implements MouseListener {

    private boolean isFolder;

    private DeckChooserFrame parent;
    private File file;
    
    public DeckUIEntry(DeckChooserFrame parent, File file) {
        this.parent = parent;
        this.file = file;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        addMouseListener(this);
        isFolder = file.isDirectory();
        ImageIcon icon;
        if (isFolder) {
            icon = IconManager.get("folder.png");
        } else {
            icon = IconManager.get("deck.png");
        }
        if (icon == null) {
            add(new JLabel("ERROR"));
        } else {
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setBorder(new EmptyBorder(8, 8, 8, 8));
            add(iconLabel);
        }
        String desc = "..";
        if (isFolder && !file.getName().equals("decks")) {
            desc = file.getName();
        } else {
            try (Scanner in = new Scanner(file, "UTF-8")) {
                desc = in.nextLine();
            } catch (FileNotFoundException e) {}
        }
        add(new JLabel(desc));
        add(Box.createHorizontalGlue());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
            if (isFolder) {
                parent.folderSelected(file);
            } else {
                parent.deckSelected(file);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
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
