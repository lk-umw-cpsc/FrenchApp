import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MainFrame extends JFrame {

    private DeckChooserFrame deckChooserFrame;

    private static final int PADDING = 16;

    public MainFrame() {
        super("French App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }
    
    public void createAndShow() {
        Box horizontallyPaddedBox = Box.createHorizontalBox();
        Box mainBox = Box.createVerticalBox();

        JButton button;

        horizontallyPaddedBox.add(Box.createHorizontalStrut(PADDING));
        horizontallyPaddedBox.add(mainBox);
        horizontallyPaddedBox.add(Box.createHorizontalStrut(PADDING));

        mainBox.add(Box.createVerticalStrut(PADDING));
        // vertical.add(button = new JButton("Practice Conjugations"));
        mainBox.add(button = new JButton("Practice Vocabulary"));
        button.addActionListener(this::vocabPressed);
        mainBox.add(Box.createVerticalStrut(PADDING));

        add(horizontallyPaddedBox);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void vocabPressed(ActionEvent e) {
        if (deckChooserFrame == null) {
            deckChooserFrame = new DeckChooserFrame(this);
        }
        deckChooserFrame.setVisible(true);
        setVisible(false);
    }

}