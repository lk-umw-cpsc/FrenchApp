import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;

public class IconManager {
    
    private static final Map<String, ImageIcon> icons = new HashMap<>();

    public static ImageIcon get(String iconName) {
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

}
