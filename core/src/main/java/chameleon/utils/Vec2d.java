package chameleon.utils;

public record Vec2d(double x, double y) {
    public static Vec2d fromLocation(Location location) {
        return new Vec2d(location.x(), location.y());
    }

    public Vec2d add(double dx, double dy) {
        return new Vec2d( x + dx, y + dy);
    }

    public Vec2d add(Vec2d vec) {
        return new Vec2d( x + vec.x, y + vec.y);
    }

    public Vec2d subtract(double dx, double dy) {
        return new Vec2d( x - dx, y - dy);
    }

    public Vec2d subtract(Vec2d vec) {
        return new Vec2d( x - vec.x, y - vec.y);
    }

    public Vec2d divide(int value) {
        return new Vec2d( x / value, y / value);
    }

    public double distanceTo(Vec2d other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }
}