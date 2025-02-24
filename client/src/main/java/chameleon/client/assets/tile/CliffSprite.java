package chameleon.client.assets.tile;

import chameleon.client.ChameleonClient;
import chameleon.client.assets.SpriteSheet;

import java.util.Map;

public class CliffSprite {
    private static Map<String, SpriteSheet> sprites;

    public static void loadSprite() {
        sprites = ChameleonClient.getInstance().getAssetLoader().loadModel("tile/cliff");
    }

    public static SpriteSheet getSpriteSheet(String mode) {
        return sprites.get("default_" + mode);
    }
}
