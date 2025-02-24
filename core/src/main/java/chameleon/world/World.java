package chameleon.world;

import chameleon.entity.Entity;
import chameleon.utils.Location;
import chameleon.world.tile.Tile;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class World {
    private final Map<Location, Tile> tiles = new HashMap<>();
    private final Map<Location, Tile> upperTiles = new HashMap<>(); // TODO 타일 시스템 정리
    private final Map<Location, Integer> heightMap = new HashMap<>();
    private List<Entity> entities = new ArrayList<>();

    public Tile getTileAt(Location location) {
        tiles.putIfAbsent(location.toTileLocation(), Tile.GRASS);
        return tiles.get(location.toTileLocation());
    }

    public void setTileAt(Location location, Tile tile) {
        tiles.put(location.toTileLocation(), tile);
    }

    public @Nullable Tile getUpperTileAt(Location location) {
        return upperTiles.get(location.toTileLocation());
    }

    public void setUpperTileAt(Location location, @Nullable Tile tile) {
        upperTiles.put(location.toTileLocation(), tile);
    }

    public int getHeightAt(Location location) {
        heightMap.putIfAbsent(location.toTileLocation(), 0);
        return heightMap.get(location.toTileLocation());
    }

    public void setHeightAt(Location location, int height) {
        heightMap.put(location.toTileLocation(), height);
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
