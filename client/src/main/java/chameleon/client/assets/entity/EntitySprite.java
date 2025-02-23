package chameleon.client.assets.entity;

import chameleon.client.ChameleonClient;
import chameleon.client.assets.SpriteSheet;
import chameleon.entity.Entity;
import chameleon.utils.Direction;

import java.util.Map;

public class EntitySprite {
    private final Map<String, SpriteSheet> sprites;

    public EntitySprite(String entityId) {
        sprites = ChameleonClient.getInstance().getAssetLoader().loadModel("entity/" + entityId);
    }

    public SpriteSheet getSpriteSheet(String mode, Entity entity) {
        Direction direction = entity.getDirection();
        return sprites.get(mode + "_" + direction.id());
    }

    public SpriteSheet getSpriteSheet(Entity entity) {
        return getSpriteSheet("default", entity);
    }
}
