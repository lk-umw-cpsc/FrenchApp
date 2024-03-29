package swingcustom;

import java.awt.event.MouseEvent;

import swingextended.LabelButton;

public class CustomButton extends LabelButton {

    public CustomButton(String text) {
        super(text);
        setFont(FontsAndColors.FONT_BUTTON);
        setBackground(FontsAndColors.COLOR_DARK_BACKGROUND);
        setForeground(FontsAndColors.COLOR_DARK_FOREGROUND);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            setBackground(FontsAndColors.COLOR_DARK_BACKGROUND);
        }
        super.setEnabled(enabled);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (isEnabled()) {
            setBackground(FontsAndColors.COLOR_DARK_BACKGROUND_HIGHLIGHT);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackground(FontsAndColors.COLOR_DARK_BACKGROUND);
    }

}