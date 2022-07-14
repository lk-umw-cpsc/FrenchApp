import java.awt.Color;
import java.awt.Component;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.SwingUtilities;

public class ColorAnimationEngine implements Runnable {

    private static final Lock lock = new ReentrantLock();

    private static Color interpolate(Color from, Color to, double f) {
        int r = from.getRed();
        int g = from.getGreen();
        int b = from.getBlue();

        int dr = to.getRed() - r;
        int dg = to.getGreen() - g;
        int db = to.getBlue() - b;

        return new Color(
            (int)(r + f * dr),
            (int)(g + f * dg),
            (int)(b + f * db)
        );
    }

    public static boolean tryLockAndAnimateIfUnlocked(Component[] components, Color[] colorsFrom, Color[] colorsTo) {
        if (lock.tryLock()) {
            new Thread(new ColorAnimationEngine(components, colorsFrom, colorsTo)).start();
            return true;
        }
        return false;
    }

    private Component[] components;
    private Color[] colorsFrom;
    private Color[] colorsTo;

    public ColorAnimationEngine(Component[] components, Color[] colorsFrom, Color[] colorsTo) {
        this.components = components;
        this.colorsFrom = colorsFrom;
        this.colorsTo = colorsTo;
    }
    
    @Override
    public void run() {
        final int n = components.length;
        Color[] colors = new Color[n];
        double step = 1.0 / 60 * 10;
        double d = step;
        int wait = 1000 / 60;

        while (d < 1.0) {
            for (int i = 0; i < n; i++) {
                colors[i] = interpolate(colorsFrom[i], colorsTo[i], d);
            }
            SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < n; i++) {
                    components[i].setBackground(colors[i]);
                }
            });
            d += step;
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {}
        }
        d = step;
        while (d < 1.0) {
            for (int i = 0; i < n; i++) {
                colors[i] = interpolate(colorsTo[i], colorsFrom[i], d);
            }
            SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < n; i++) {
                    components[i].setBackground(colors[i]);
                }
            });
            d += step;
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {}
        }
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < n; i++) {
                components[i].setBackground(colorsFrom[i]);
            }
            lock.unlock();
        });
    }
}
