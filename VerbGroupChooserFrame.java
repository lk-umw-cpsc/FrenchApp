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

public class VerbGroupChooserFrame extends JFrame implements WindowListener {
    
    private final JFrame parent;

    private final Box groupsContainer;

    public VerbGroupChooserFrame(JFrame parent) {
        this.parent = parent;

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        groupsContainer = Box.createVerticalBox();

        JScrollPane scrollPane = new JScrollPane(groupsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        add(scrollPane);

        List<VerbGroupUIEntry> entries = new ArrayList<>(32);

        File verbsFolder = new File("verbs");
        for (File f : verbsFolder.listFiles()) {
            entries.add(new VerbGroupUIEntry(this, f));
        }

        Collections.sort(entries);
        for (VerbGroupUIEntry entry : entries) {
            groupsContainer.add(entry);
        }
        
        addWindowListener(this);

        pack();
        setLocationRelativeTo(parent);
    }

    public void groupSelected(VerbGroup group) {
        new ConjugationFrame(this, group).setVisible(true);
        setVisible(false);
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
