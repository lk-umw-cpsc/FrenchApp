package swingcustom;

import javax.swing.JLabel;

public class BasicLabel extends JLabel {
    
    public BasicLabel(String text) {
        super(text);
        setForeground(FontsAndColors.COLOR_DARK_FOREGROUND);
        setFont(FontsAndColors.FONT_BASIC_COMPONENT);
    }

}
