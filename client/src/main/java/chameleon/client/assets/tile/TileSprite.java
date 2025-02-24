package chameleon.client.assets.tile;

import chameleon.client.ChameleonClient;
import chameleon.client.assets.spritesheet.SpriteSheet;

import java.util.Map;

public class TileSprite {
    private final Map<String, SpriteSheet> sprites;

    public TileSprite(String tileId) {
        sprites = ChameleonClient.getInstance().getAssetLoader().loadModel("tile/" + tileId);
    }

    public SpriteSheet getSpriteSheet(String identifier) {
        return sprites.get(identifier);
    }
}
