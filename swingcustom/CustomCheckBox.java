package swingcustom;

import javax.swing.JCheckBox;

public class CustomCheckBox extends JCheckBox {
    
    public CustomCheckBox(String text) {
        super(text);
        setOpaque(false);
        setForeground(FontsAndColors.COLOR_DARK_FOREGROUND);
        setFont(FontsAndColors.FONT_BASIC_COMPONENT);
    }

}
