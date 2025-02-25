package chameleon.world.generator;

import chameleon.utils.Location;
import chameleon.world.tile.Tile;

public abstract class WorldGenerator {
    public abstract Tile generateTileAt(Location location);

    public abstract int generateHeightAt(Location location);
}
