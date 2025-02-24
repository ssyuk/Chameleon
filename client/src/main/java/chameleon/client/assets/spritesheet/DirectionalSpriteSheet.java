package chameleon.client.assets.spritesheet;

import java.awt.image.BufferedImage;

public class DirectionalSpriteSheet extends SpriteSheet {
    private final BufferedImage left;
    private final BufferedImage right;
    private final BufferedImage up;
    private final BufferedImage down;
    private final DirectionalMethod method;

    public DirectionalSpriteSheet(BufferedImage left, BufferedImage right, BufferedImage up, BufferedImage down, DirectionalMethod method) {
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.method = method;
    }

    public BufferedImage left() {
        return left;
    }

    public BufferedImage right() {
        return right;
    }

    public BufferedImage up() {
        return up;
    }

    public BufferedImage down() {
        return down;
    }

    public DirectionalMethod method() {
        return method;
    }

    public enum DirectionalMethod {
        SLOPE
    }
}