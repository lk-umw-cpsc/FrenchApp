import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class DeckChooserFrame extends JFrame implements WindowListener {

    private static final File root = new File(System.getProperty("user.dir") + "/decks");

    private Box entryContainer;

    private JFrame owner;

    public static void main(String[] args) {
        System.out.println(root);
        new DeckChooserFrame(null).setVisible(true);
    }
    
    public DeckChooserFrame(JFrame owner) {
        this.owner = owner;
        addWindowListener(this);
        setTitle("Choose a deck");
        entryContainer = Box.createVerticalBox();
        for (File f : root.listFiles()) {
            if (f.getName().startsWith(".")) {
                continue;
            }
            entryContainer.add(new DeckUIEntry(this, f));
        }
        entryContainer.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(entryContainer);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        add(scrollPane);
        pack();
        setLocationRelativeTo(null);
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
        if (owner != null) {
            owner.setVisible(true);
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
