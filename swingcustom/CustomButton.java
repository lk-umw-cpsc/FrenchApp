package swingcustom;

import swingextended.LabelButton;

public class CustomButton extends LabelButton {

    public CustomButton(String text) {
        super(text);
        setFont(FontsAndColors.FONT_BUTTON);
        setBackground(FontsAndColors.COLOR_DARK_BACKGROUND);
        setForeground(FontsAndColors.COLOR_DARK_FOREGROUND);
    }

}