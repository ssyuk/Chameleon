package chameleon.client.assets.entity;

import chameleon.client.ChameleonClient;
import chameleon.client.assets.spritesheet.SpriteSheet;

import java.util.Map;

public class EntitySprite {
    private final Map<String, SpriteSheet> sprites;

    public EntitySprite(String entityId) {
        sprites = ChameleonClient.getInstance().getAssetLoader().loadModel("entity/" + entityId);
    }

    public SpriteSheet getSpriteSheet(String identifier) {
        return sprites.get(identifier);
    }
}
