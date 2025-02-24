package chameleon.utils;

import chameleon.world.World;

public record Location(World world, double x, double y) {
    public Location add(double dx, double dy) {
        return new Location(world, x + dx, y + dy);
    }

    public Location add(Location location) {
        return new Location(world, x + location.x, y + location.y);
    }

    public Location add(Vec2d vec) {
        return new Location(world, x + vec.x(), y + vec.y());
    }

    public Location subtract(double dx, double dy) {
        return new Location(world, x - dx, y - dy);
    }

    public Location subtract(Location location) {
        return new Location(world, x - location.x, y - location.y);
    }

    public Location divide(int value) {
        return new Location(world, x / value, y / value);
    }

    public double distanceTo(Location other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    public Location toTileLocation() {
        // 버림
        return new Location(world, Math.floor(x) + .5, Math.floor(y) + .5);
    }

    public Vec2d toVec2d() {
        return new Vec2d(x, y);
    }
}
