package chameleon.client.assets.spritesheet;

import java.awt.image.BufferedImage;
import java.util.List;

public class AnimatedSpriteSheet extends SpriteSheet {
    private final List<BufferedImage> images;
    private final int duration;

    public AnimatedSpriteSheet(List<BufferedImage> images, int duration) {
        this.images = images;
        this.duration = duration;
    }

    public List<BufferedImage> images() {
        return images;
    }

    public int duration() {
        return duration;
    }
}