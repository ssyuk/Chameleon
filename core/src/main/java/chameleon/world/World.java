package chameleon.world;

import chameleon.entity.Entity;
import chameleon.entity.tile.TileEntity;
import chameleon.utils.Location;
import chameleon.world.generator.WorldGenerator;
import chameleon.world.tile.Tile;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class World {
    private final WorldGenerator generator;
    private final Map<Location, Tile> tiles = new HashMap<>();
    private final Map<Location, Integer> heightMap = new HashMap<>();
    private List<Entity> entities = new ArrayList<>();

    public World(WorldGenerator generator) {
        this.generator = generator;
    }

    public Tile getTileAt(Location location) {
        if (!tiles.containsKey(location.toTileLocation()))
            tiles.put(location.toTileLocation(), generator.generateTileAt(location));
        return tiles.get(location.toTileLocation());
    }

    public void setTileAt(Location location, Tile tile) {
        tiles.put(location.toTileLocation(), tile);
    }

    public @Nullable TileEntity getTileEntityAt(Location location) {
        return (TileEntity) entities.stream().filter(entity -> entity instanceof TileEntity && entity.getLocation().equals(location.toTileLocation())).findFirst().orElse(null);
    }

    public int getHeightAt(Location location) {
        if (!heightMap.containsKey(location.toTileLocation()))
            heightMap.put(location.toTileLocation(), generator.generateHeightAt(location));
        return heightMap.get(location.toTileLocation());
    }

    public void setHeightAt(Location location, int height) {
        heightMap.put(location.toTileLocation(), height);
    }

    public Map<Location, Integer> getHeightMap() {
        return heightMap;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void update() {
        for (Entity entity : new ArrayList<>(entities)) entity.update();
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(UUID uuid) {
        entities.removeIf(entity -> entity.uuid().equals(uuid));
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public @Nullable Entity getEntityByUuid(UUID uuid) {
        return entities.stream().filter(entity -> entity.uuid().equals(uuid)).findFirst().orElse(null);
    }
}
