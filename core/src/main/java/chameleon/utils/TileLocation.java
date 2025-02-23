package chameleon.utils;

import chameleon.world.World;

public record TileLocation(World world, int x, int y) {
    public TileLocation add(int dx, int dy) {
        return new TileLocation(world, x + dx, y + dy);
    }

    public TileLocation subtract(int dx, int dy) {
        return new TileLocation(world, x - dx, y - dy);
    }

    public int distanceTo(TileLocation other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    public Location toLocation() {
        return new Location(world, x + .5, y + .5);
    }
}
