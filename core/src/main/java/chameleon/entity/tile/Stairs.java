package chameleon.entity.tile;

import chameleon.entity.CollisionOption;
import chameleon.entity.Entity;
import chameleon.utils.Direction;
import chameleon.utils.Location;
import chameleon.utils.colliding.AABB;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.UUID;

public class Stairs extends TileEntity {
    public Stairs(UUID uuid, Location location) {
        super(uuid, location);
    }

    public Stairs(Location location) {
        super(location);
    }

    @Override
    public String id() {
        return "stairs";
    }

    @Override
    public void update() {
    }

    @Override
    public AABB getBoundingBox() {
        return AABB.fromCenterAndSize(location, .1);
    }

    @Override
    public CollisionOption getCollisionOption() {
        return CollisionOption.SLOPE;
    }

    public static Entity unpack(MessageUnpacker unpacker, UUID uuid, Location location, Direction direction) throws IOException {
        Stairs entity = new Stairs(uuid, location);
        entity.setDirection(direction);
        return entity;
    }
}
