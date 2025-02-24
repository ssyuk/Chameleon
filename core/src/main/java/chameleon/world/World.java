package chameleon.world;

import chameleon.entity.Entity;
import chameleon.utils.Location;
import chameleon.world.tile.Tile;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class World {
    private final Map<Location, Tile> tiles = new HashMap<>();
    private final Map<Location, Integer> heightMap = new HashMap<>();
    private List<Entity> entities = new ArrayList<>();

    public Tile getTileAt(Location location) {
        tiles.putIfAbsent(location, Tile.GRASS);
        return tiles.get(location);
    }

    public void setTileAt(Location location, Tile tile) {
        tiles.put(location, tile);
    }

    public int getHeightAt(Location location) {
        heightMap.putIfAbsent(location, 0);
        return heightMap.get(location);
    }

    public void setHeightAt(Location location, int height) {
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
