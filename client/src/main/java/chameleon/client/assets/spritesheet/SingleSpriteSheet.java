package chameleon.client.assets.spritesheet;

import java.awt.image.BufferedImage;

public class SingleSpriteSheet extends SpriteSheet {
    private final BufferedImage image;

    public SingleSpriteSheet(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage image() {
        return image;
    }
}
