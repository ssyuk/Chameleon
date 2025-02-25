package chameleon.utils;

public enum Direction {
    LEFT(-1, 0),
    RIGHT(1, 0),
    UP(0, -1),
    DOWN(0, 1);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public static Direction calculate(Location original, Location newLocation, Direction defaultDirection) {
        return byDisplacement(newLocation.subtract(original).toVec2d(), defaultDirection);
    }

    private static Direction byDisplacement(Vec2d displacement, Direction defaultDirection) {
        return displacement.x() > 0 ? RIGHT :
                displacement.x() < 0 ? LEFT :
                        displacement.y() > 0 ? DOWN :
                                displacement.y() < 0 ? UP :
                                        defaultDirection;
    }

    public int dx() {
        return dx;
    }

    public int dy() {
        return dy;
    }

    public String id() {
        return switch (this) {
            case LEFT -> "left";
            case RIGHT -> "right";
            case UP -> "up";
            case DOWN -> "down";
        };
    }
}
