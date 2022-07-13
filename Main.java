import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

    public static void main(String[] args) {
        initializeOSLookAndFeel();
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void initializeOSLookAndFeel() {
        try {
            System.setProperty( "apple.awt.application.appearance", "system" );
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                    | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println("Unable to set system L&F.");
        }
    }

    private static void createAndShowGUI() {
        new MainFrame().setVisible(true);
    }

}