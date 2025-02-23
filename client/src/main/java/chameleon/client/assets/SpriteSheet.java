package chameleon.client.assets;

import java.awt.image.BufferedImage;
import java.util.List;

public record SpriteSheet(List<BufferedImage> sprites, int animatedDuration, SpriteSelection selection) {
    public static SpriteSheet singleSprite(BufferedImage image) {
        return new SpriteSheet(List.of(image), -1, SpriteSelection.NONE);
    }

    public static SpriteSheet animatedSprite(List<BufferedImage> images, int duration) {
        return new SpriteSheet(images, duration, SpriteSelection.ANIMATION);
    }

    public enum SpriteSelection {
        NONE, ANIMATION, RANDOM_BY_ENTITY
    }
}
