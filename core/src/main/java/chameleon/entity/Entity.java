package chameleon.entity;

import chameleon.Chameleon;
import chameleon.entity.player.Player;
import chameleon.entity.tile.*;
import chameleon.utils.Direction;
import chameleon.utils.Location;
import chameleon.utils.Vec2d;
import chameleon.utils.colliding.AABB;
import chameleon.utils.colliding.CollisionDetection;
import chameleon.world.World;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class Entity {
    private static final Map<String, Class<? extends Entity>> ENTITY_REGISTRY = new HashMap<>();

    static {
        ENTITY_REGISTRY.put("player", Player.class);
        ENTITY_REGISTRY.put("bush", Bush.class);
        ENTITY_REGISTRY.put("weed", Weed.class);
        ENTITY_REGISTRY.put("broken_tree", BrokenTree.class);
        ENTITY_REGISTRY.put("stairs", Stairs.class);
    }

    private final UUID uuid;
    protected Location location;
    private Direction direction = Direction.DOWN;
    protected boolean moving = false;

    public Entity(UUID uuid, Location location) {
        this.uuid = uuid;
        this.location = location;
    }

    public Entity(Location location) {
        this(UUID.randomUUID(), location);
    }

    public Location getLocation() {
        return location;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public UUID uuid() {
        return uuid;
    }

    public boolean move(Direction direction, double speedMultiplier) {
        Location original = location;

        this.direction = direction;
        location = location.add(direction.dx() * speedMultiplier, direction.dy() * speedMultiplier);

        if (detectCollision(original)) {
            location = original;
            return false;
        }
        return true;
    }

    public boolean move(Vec2d displacement, boolean force) {
        Location original = location;
        location = location.add(displacement);
        direction = displacement.x() > 0 ? Direction.RIGHT :
                displacement.x() < 0 ? Direction.LEFT :
                        displacement.y() > 0 ? Direction.DOWN :
                                displacement.y() < 0 ? Direction.UP :
                                        this.direction;

        if (detectCollision(original) && !force) {
            location = original;
            return false;
        }
        return true;
    }

    private boolean detectCollision(Location original) {
        if (getCollisionOption() == CollisionOption.SOLID) {
            World world = location.world();

            Set<Location> collidingTiles = getCollidingTiles(getBoundingBox().larger(.65, .65, .8, .35));
            Set<Location> collidingTilesForSlope = getCollidingTiles(getBoundingBox().larger(0, 0, -.3, -.3));
            Set<Location> collidingTilesWithNormal = getCollidingTiles(getBoundingBox());

            if (collidingTilesForSlope.stream().noneMatch(loc -> {
                TileEntity entity = world.getTileEntityAt(loc);
                return entity != null && entity.getCollisionOption().equals(CollisionOption.SLOPE) && CollisionDetection.isColliding(this.getBoundingBox(), entity.getInteractiveBoundingBox());
            }) &&
                    (collidingTiles.stream().anyMatch(loc -> world.getHeightAt(loc) > world.getHeightAt(original.toTileLocation()))
                            || collidingTilesWithNormal.stream().anyMatch(loc -> world.getHeightAt(loc) < world.getHeightAt(original.toTileLocation())))) {
                return true;
            }

            for (Entity entity : world.getEntities()) {
                if (this instanceof Player && entity instanceof Player) continue;
                if (entity.uuid != this.uuid && entity.getCollisionOption().equals(CollisionOption.SOLID) && CollisionDetection.isColliding(this.getBoundingBox(), entity.getBoundingBox())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void teleport(Location location) {
        this.location = location;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public abstract String id();

    public abstract void update();

    public abstract AABB getBoundingBox();

    public AABB getInteractiveBoundingBox() {
        return getBoundingBox();
    }

    public abstract CollisionOption getCollisionOption();

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Entity entity && entity.uuid().equals(uuid);
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "id=" + id() +
                ", uuid=" + uuid +
                ", location=" + location +
                ", direction=" + direction +
                ", moving=" + moving +
                '}';
    }

    public void pack(MessagePacker packer) throws IOException {
        packer.packString(id());
        packer.packString(uuid.toString());
        packer.packDouble(location.x());
        packer.packDouble(location.y());
        packer.packString(direction.name());
    }

    public int packingSize() {
        return 5;
    }

    public static Entity unpack(MessageUnpacker unpacker) throws IOException {
        String id = unpacker.unpackString();
        UUID uuid = UUID.fromString(unpacker.unpackString());
        double x = unpacker.unpackDouble();
        double y = unpacker.unpackDouble();
        Direction direction = Direction.valueOf(unpacker.unpackString());

        try {
            return (Entity) ENTITY_REGISTRY.get(id).getMethod("unpack", MessageUnpacker.class, UUID.class, Location.class, Direction.class).invoke(null, unpacker, uuid, new Location(Chameleon.getInstance().getWorld(), x, y), direction);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Location> getCollidingTiles(AABB boundingBox) {
        int minX = (int) Math.floor(boundingBox.min().x());
        int maxX = (int) Math.floor(boundingBox.max().x());
        int minY = (int) Math.floor(boundingBox.min().y());
        int maxY = (int) Math.floor(boundingBox.max().y());

        Set<Location> collidingTiles = new HashSet<>();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                collidingTiles.add(new Location(location.world(), x, y).toTileLocation());
            }
        }
        return collidingTiles;
    }
}
