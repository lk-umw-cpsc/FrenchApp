import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MainFrame extends JFrame {

    private VocabFrame vocabFrame;

    private static final int PADDING = 16;

    public MainFrame() {
        super("French App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }
    
    public void createAndShow() {
        Box horizontal = Box.createHorizontalBox();
        Box vertical = Box.createVerticalBox();

        JButton button;

        horizontal.add(Box.createHorizontalStrut(PADDING));
        horizontal.add(vertical);
        horizontal.add(Box.createHorizontalStrut(PADDING));

        vertical.add(Box.createVerticalStrut(PADDING));
        // vertical.add(button = new JButton("Practice Conjugations"));
        vertical.add(button = new JButton("Practice Vocabulary"));
        button.addActionListener(this::vocabPressed);
        vertical.add(Box.createVerticalStrut(PADDING));

        add(horizontal);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void vocabPressed(ActionEvent e) {
        if (vocabFrame == null) {
            vocabFrame = new VocabFrame();
            vocabFrame.createAndShow(this);
        } else {
            vocabFrame.setVisible(true);
            setVisible(false);
        }
    }

}