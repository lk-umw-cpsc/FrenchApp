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

public class DeckChooserFrame extends JFrame implements WindowListener {

    private static final File root = new File(System.getProperty("user.dir") + "/decks");

    private Box entryContainer;

    private JFrame parent;
    
    public DeckChooserFrame(JFrame parent) {
        this.parent = parent;
        addWindowListener(this);
        setTitle("Choose a deck");
        List<DeckUIEntry> entries = new ArrayList<>();
        for (File f : root.listFiles()) {
            if (f.getName().startsWith(".")) {
                continue;
            }
            entries.add(new DeckUIEntry(this, f));
        }
        Collections.sort(entries);
        entryContainer = Box.createVerticalBox();
        for (DeckUIEntry entry : entries) {
            entryContainer.add(entry);
        }
        entryContainer.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(entryContainer);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        add(scrollPane);
        pack();
        setLocationRelativeTo(parent);
    }

    public void folderSelected(File folder) {
        entryContainer.removeAll();
        if (!folder.equals(root)) {
            entryContainer.add(new DeckUIEntry(this, root));
        }
        for (File f : folder.listFiles()) {
            if (f.getName().startsWith(".")) {
                continue;
            }
            entryContainer.add(new DeckUIEntry(this, f));
        }
        entryContainer.add(Box.createVerticalGlue());
        revalidate();
        repaint();
    }

    public void deckSelected(File deck) {
        VocabFrame vf = new VocabFrame(deck);
        vf.createAndShow(this);
        setVisible(false);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (parent != null) {
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
