package chameleon.client.assets;

import chameleon.client.assets.entity.EntitySprite;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private final Map<String, EntitySprite> entitySprites = new HashMap<>();

    public void load() {
        entitySprites.put("player", new EntitySprite("player"));
        entitySprites.put("bush", new EntitySprite("bush"));
        entitySprites.put("weed", new EntitySprite("weed"));
        entitySprites.put("broken_tree", new EntitySprite("broken_tree"));
    }

    public EntitySprite getEntitySprite(String entityId) {
        return entitySprites.get(entityId);
    }
}
