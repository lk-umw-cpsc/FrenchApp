import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

import resources.ResourceManager;
import swingcustom.BasicLabel;
import swingcustom.FontsAndColors;

public class DeckUIEntry extends JPanel implements MouseListener, Comparable<DeckUIEntry> {

    private boolean isFolder;

    private DeckChooserFrame parent;
    private File file;
    private int order;
    private Deck deck;
    private JLabel dueIcon;
    private final JPopupMenu rightClickMenu;
    
    public DeckUIEntry(DeckChooserFrame parent, File file) {
        this.parent = parent;
        this.file = file;
        setBackground(FontsAndColors.COLOR_DARK_BACKGROUND);
        setForeground(FontsAndColors.COLOR_DARK_FOREGROUND);
        setOpaque(true);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        addMouseListener(this);
        isFolder = file.isDirectory();
        ImageIcon icon;
        if (isFolder) {
            icon = ResourceManager.getImage("folder.png");
        } else {
            icon = ResourceManager.getImage("deck.png");
        }
        if (icon == null) {
            add(new JLabel("ERROR"));
        } else {
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setBorder(new EmptyBorder(8, 8, 8, 16));
            add(iconLabel);
        }
        boolean containsDueCards = false;
        String desc = "..";
        if (isFolder) {
            if (file.getName().equals("decks")) {
                
            } else {
                desc = file.getName();
                File descriptionFile = new File(file.getAbsolutePath() + "/.description");
                if (descriptionFile.exists()) {
                    try (Scanner in = new Scanner(descriptionFile, "UTF-8")) {
                        desc = in.nextLine();
                        order = in.nextInt();
                    } catch (FileNotFoundException e) {
                        
                    }
                }
            }
        } else {
            deck = new Deck(file);
            desc = deck.getDescription();
            if (!deck.getDueCards().isEmpty()) {
                containsDueCards = true;
            }
        }
        JLabel label = new BasicLabel(desc);
        add(label);
        dueIcon = new JLabel(ResourceManager.getImage("due.png"));
        dueIcon.setBorder(new EmptyBorder(16, 8, 16, 8));
        dueIcon.setToolTipText("Cards are due for study");
        add(dueIcon);
        dueIcon.setVisible(containsDueCards);

        add(Box.createHorizontalGlue());

        rightClickMenu = new JPopupMenu();
        JMenuItem resetOption = new JMenuItem("Reset due dates");
        resetOption.addActionListener(this::resetDueDates);
        rightClickMenu.add(resetOption);
    }

    private void resetDueDates(ActionEvent e) {
        deck.resetDueDates();
        deck.save();
        dueIcon.setVisible(true);
        revalidate();
    }

    public void updateDueness() {
        dueIcon.setVisible(!deck.getDueCards().isEmpty());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
            if (isFolder) {
                parent.folderSelected(file);
            } else {
                parent.deckSelected(this, deck);
            }
        } else if(e.getButton() == MouseEvent.BUTTON3) {
            rightClickMenu.show(this, e.getX(), e.getY());
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

    @Override
    public int compareTo(DeckUIEntry e) {
        return order - e.order;
    }
}
