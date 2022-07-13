package resources;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;

public class ResourceManager {
    
    private static final Map<String, ImageIcon> icons = new HashMap<>();
    private static final Map<String, Font> fonts = new HashMap<>();

    public static ImageIcon getImage(String iconName) {
        ImageIcon icon = icons.get(iconName);
        if (icon == null) {
            try {
                icon = new ImageIcon(ImageIO.read(new File("images/" + iconName)));
                icons.put(iconName, icon);
            } catch (IOException e) {
                System.out.println("Note: unable to load icon " + iconName);
            }
        }
        return icon;
    }

    public static Font getFont(String fontName) {
        Font font = fonts.get(fontName);
        if (font == null) {
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/" + fontName));
            } catch (FontFormatException | IOException e) {
                System.out.println("Note: unable to load font: " + fontName);
                e.printStackTrace();
            }
        }
        return font;
    }

}
