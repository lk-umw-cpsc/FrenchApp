package swingcustom;

import javax.swing.JRadioButton;

public class CustomRadioButton extends JRadioButton {
    
    public CustomRadioButton(String text) {
        super(text);
        setOpaque(false);
        setForeground(FontsAndColors.COLOR_DARK_FOREGROUND);
        setFont(FontsAndColors.FONT_BASIC_COMPONENT);
    }

}
