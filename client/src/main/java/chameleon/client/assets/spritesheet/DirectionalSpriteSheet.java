package chameleon.client.assets.spritesheet;

import chameleon.utils.Direction;
import chameleon.utils.Location;
import chameleon.world.World;

import java.awt.image.BufferedImage;

public class DirectionalSpriteSheet extends SpriteSheet {
    private final BufferedImage left;
    private final BufferedImage right;
    private final BufferedImage up;
    private final BufferedImage down;
    private final DirectionalMethod method;

    public DirectionalSpriteSheet(BufferedImage left, BufferedImage right, BufferedImage up, BufferedImage down, DirectionalMethod method) {
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.method = method;
    }

    public BufferedImage left() {
        return left;
    }

    public BufferedImage right() {
        return right;
    }

    public BufferedImage up() {
        return up;
    }

    public BufferedImage down() {
        return down;
    }

    public DirectionalMethod method() {
        return method;
    }

    public BufferedImage image(World world, Location location) {
        return switch (method) {
            case SLOPE -> {
                int currentHeight = world.getHeightAt(location);
                for (Direction value : Direction.values()) {
                    Location newLocation = location.add(value.dx(), value.dy());
                    int newHeight = world.getHeightAt(newLocation);
                    if (newHeight > currentHeight) {
                        yield switch (value) {
                            case LEFT -> right;
                            case RIGHT -> left;
                            case UP -> down;
                            case DOWN -> up;
                        };
                    }
                }
                throw new IllegalStateException("Invalid slope");
            }
            case INVALID -> throw new IllegalStateException("Invalid method");
        };
    }

    public enum DirectionalMethod {
        SLOPE, INVALID
    }
}