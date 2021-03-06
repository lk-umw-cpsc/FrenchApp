import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import swingcustom.FontsAndColors;

public class DeckChooserFrame extends JFrame implements WindowListener {

    private static final File root = new File(System.getProperty("user.dir") + "/decks");

    private Box entryContainer;

    private JFrame parent;

    private DeckUIEntry pickedDeckUIEntry;
    
    public DeckChooserFrame(JFrame parent) {
        this.parent = parent;
        addWindowListener(this);
        // setOpaque(true);
        setTitle("Choose a deck");
        entryContainer = Box.createVerticalBox();
        entryContainer.setOpaque(true);
        entryContainer.setBackground(FontsAndColors.APP_BACKGROUND);
        updateEntries(root.listFiles());

        JScrollPane scrollPane = new JScrollPane(entryContainer);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        entryContainer.setBorder(new EmptyBorder(8, 8, 8, 8));
        scrollPane.setBackground(FontsAndColors.APP_BACKGROUND);
        add(scrollPane);
        pack();
        setLocationRelativeTo(parent);
    }

    private void updateEntries(File[] files) {
        List<DeckUIEntry> entries = new ArrayList<>();
        for (File f : files) {
            if (f.getName().startsWith(".")) {
                continue;
            }
            entries.add(new DeckUIEntry(this, f));

        }
        Collections.sort(entries);
        for (DeckUIEntry entry : entries) {
            entryContainer.add(entry);
            entryContainer.add(Box.createVerticalStrut(4));
        }
        entryContainer.add(Box.createVerticalGlue());
    }

    public void folderSelected(File folder) {
        entryContainer.removeAll();
        if (!folder.equals(root)) {
            entryContainer.add(new DeckUIEntry(this, root));
            entryContainer.add(Box.createVerticalStrut(4));
        }
        updateEntries(folder.listFiles());
        revalidate();
        repaint();
    }

    public void deckSelected(DeckUIEntry caller, Deck deck) {
        pickedDeckUIEntry = caller;
        VocabFrame vf = new VocabFrame(deck, this);
        vf.setLocationRelativeTo(this);
        vf.setVisible(true);
        setVisible(false);
    }

    @Override
    public void setVisible(boolean visibility) {
        if (visibility && pickedDeckUIEntry != null) {
            pickedDeckUIEntry.updateDueness();
        }
        super.setVisible(visibility);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (parent != null) {
            parent.setLocationRelativeTo(this);
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
