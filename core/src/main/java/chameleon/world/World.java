package chameleon.world;

import chameleon.entity.Entity;
import chameleon.utils.Location;
import chameleon.utils.TileLocation;
import chameleon.utils.colliding.AABB;
import chameleon.world.tile.Tile;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class World {
    private final Map<TileLocation, Tile> tiles = new HashMap<>();
    private final Map<TileLocation, Integer> heightMap = new HashMap<>();
    private List<Entity> entities = new ArrayList<>();

    public Tile getTileAt(TileLocation location) {
        tiles.putIfAbsent(location, Tile.GRASS);
        return tiles.get(location);
    }

    public void setTileAt(TileLocation location, Tile tile) {
        tiles.put(location, tile);
    }

    public int getHeightAt(TileLocation location) {
        if (location.x() < 0)
            heightMap.putIfAbsent(location,location.y() / 3);
        else
            heightMap.putIfAbsent(location, location.x() / 3);
        return heightMap.get(location);
    }

    public void setHeightAt(TileLocation location, int height) {
        heightMap.put(location, height);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void update() {
        for (Entity entity : entities) entity.update();
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public @Nullable Entity getEntityByUuid(UUID uuid) {
        return entities.stream().filter(entity -> entity.uuid().equals(uuid)).findFirst().orElse(null);
    }
}
