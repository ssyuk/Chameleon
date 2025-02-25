package chameleon.client.screen.component;

import chameleon.client.renderer.Brush;

public abstract class Component {
    private final int x, y;
    private final int width, height;

    public Component(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public abstract void render(Brush brush);

    public abstract void onPressed();
}
