package chameleon.client.assets;

import chameleon.client.assets.entity.EntitySprite;
import chameleon.client.assets.tile.CliffSprite;
import chameleon.client.assets.tile.TileSprite;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private final Map<String, EntitySprite> entitySprites = new HashMap<>();
    private final Map<String, TileSprite> tileSprites = new HashMap<>();

    public void load() {
        entitySprites.put("player", new EntitySprite("player"));
        entitySprites.put("bush", new EntitySprite("bush"));
        entitySprites.put("weed", new EntitySprite("weed"));
        entitySprites.put("broken_tree", new EntitySprite("broken_tree"));
        entitySprites.put("stairs", new EntitySprite("stairs"));

        tileSprites.put("grass", new TileSprite("grass"));

        CliffSprite.loadSprite();
    }

    public EntitySprite getEntitySprite(String entityId) {
        return entitySprites.get(entityId);
    }

    public TileSprite getTileSprite(String tileId) {
        return tileSprites.get(tileId);
    }
}
