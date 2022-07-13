package swingcustom;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class PromptLabel extends JLabel {

    public PromptLabel(String text) {
        super(text);
        setOpaque(true);
        setFont(FontsAndColors.FONT_PROMPT);
        setForeground(FontsAndColors.COLOR_DARK_FOREGROUND);
        setBackground(FontsAndColors.COLOR_DARK_BACKGROUND);
        Dimension d = getPreferredSize();
        setHorizontalAlignment(SwingConstants.CENTER);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, d.width));
    }
    
}
