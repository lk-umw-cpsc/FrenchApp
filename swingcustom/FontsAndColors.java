package swingcustom;
import java.awt.Color;
import java.awt.Font;

import resources.ResourceManager;

public class FontsAndColors {
    
    public static final Font FONT_THIN = ResourceManager.getFont("Heebo-Thin.ttf");
    public static final Font FONT_LIGHT = ResourceManager.getFont("Heebo-Light.ttf");
    public static final Font FONT_REGULAR = ResourceManager.getFont("Heebo-Regular.ttf");
    public static final Font FONT_MEDIUM = ResourceManager.getFont("Heebo-Medium.ttf");

    public static final Color COLOR_A = hex("#362E82");
    public static final Color COLOR_B = hex("#5E53C1");
    public static final Color COLOR_C = hex("#272727");
    public static final Color COLOR_D = hex("#FFFFFF");

    public static final Color COLOR_LIGHT_FOREGROUND = COLOR_C;
    public static final Color COLOR_LIGHT_BACKGROUND = COLOR_D;

    public static final Color COLOR_DARK_FOREGROUND = COLOR_D;
    public static final Color COLOR_DARK_BACKGROUND = COLOR_A;
    public static final Color COLOR_DARK_BACKGROUND_HIGHLIGHT = hex("#453AA8");

    public static final Color COLOR_DARK_BACKGROUND_CORRECT = hex("#297026");
    public static final Color COLOR_APP_BACKGROUND_CORRECT = hex("#43BF3D");

    public static final Color COLOR_DARK_BACKGROUND_INCORRECT = hex("#702626");
    public static final Color COLOR_APP_BACKGROUND_INCORRECT = hex("#BF3D3D");

    public static final Color COLOR_REVEALED_ANSWER_BACKGROUND = new Color(255, 212, 138);

    public static final Color APP_BACKGROUND = COLOR_B;

    public static final Font FONT_INPUT = FONT_LIGHT.deriveFont(20f);
    public static final Font FONT_HEADING = FONT_THIN.deriveFont(18f);
    public static final Font FONT_BUTTON = FONT_REGULAR.deriveFont(24f);
    public static final Font FONT_BUTTON_SMALL = FONT_REGULAR.deriveFont(16f);
    public static final Font FONT_PROMPT = FONT_REGULAR.deriveFont(24f);
    public static final Font FONT_BASIC_COMPONENT = FONT_LIGHT.deriveFont(16f);

    /**
     * Converts a "web" (hexadecimal) color to a Color object
     * @param h A String containing the hexcode. If a preceding
     * # is present, it's ignored.
     * @return a new Color object based on the color given.
     */
    public static Color hex(String h) {
        if (h.startsWith("#")) {
            h = h.substring(1);
        }
        int r, g, b;
        try {
            r = Integer.parseInt(h.substring(0, 2), 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid red value given for color: " + h);
        }

        try {
            g = Integer.parseInt(h.substring(2, 4), 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid green value given for color: " + h);
        }

        try {
            b = Integer.parseInt(h.substring(4, 6), 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid blue value given for color: " + h);
        }
        return new Color(r, g, b);
    }

}
