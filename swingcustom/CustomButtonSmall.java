package swingcustom;

import swingcustom.CustomButton;
import swingcustom.FontsAndColors;

public class CustomButtonSmall extends CustomButton {
    
    public CustomButtonSmall(String text) {
        super(text);
        setFont(FontsAndColors.FONT_BUTTON_SMALL);
    }

}
