package chameleon.client.renderer;

import java.awt.*;

public class Brush {
    private final Insets insets;
    private final Graphics g;

    public Brush(Insets insets, Graphics g) {
        this.insets = insets;
        this.g = g;
    }

    public void drawRect(int x, int y, int width, int height, int color) {
        g.setColor(new Color(color));
        g.fillRect(x + insets.left, y + insets.top, width, height);
    }

    public void drawImage(int x, int y, int width, int height, Image image) {
        g.drawImage(image, x + insets.left, y + insets.top, width, height, null);
    }

    public void drawString(String text, int x, int y, int color) {
        g.setColor(new Color(color));
        g.drawString(text, x + insets.left, y + insets.top);
    }
}
