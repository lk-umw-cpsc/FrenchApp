package swingcustom;

import javax.swing.JLabel;

public class HeaderLabel extends JLabel {
    public HeaderLabel(String text) {
        super(text);
        setFont(FontsAndColors.FONT_HEADING);
        setForeground(FontsAndColors.COLOR_DARK_FOREGROUND);
    }
}
