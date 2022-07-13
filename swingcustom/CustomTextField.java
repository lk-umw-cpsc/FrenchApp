package swingcustom;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class CustomTextField extends JTextField {
    public CustomTextField(int columns) {
        super(columns);
        setBackground(FontsAndColors.COLOR_LIGHT_BACKGROUND);
        setBackground(FontsAndColors.COLOR_LIGHT_BACKGROUND);
        setFont(FontsAndColors.FONT_INPUT);
        setHorizontalAlignment(SwingConstants.CENTER);
    }
}
