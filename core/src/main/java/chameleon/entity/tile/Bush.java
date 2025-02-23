package chameleon.entity.tile;

import chameleon.entity.CollisionOption;
import chameleon.entity.Entity;
import chameleon.utils.Direction;
import chameleon.utils.Location;
import chameleon.utils.colliding.AABB;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.UUID;

public class Bush extends TileEntity {
    public Bush(UUID uuid, Location location) {
        super(uuid, location);
    }

    public Bush(Location location) {
        super(location);
    }

    @Override
    public String id() {
        return "bush";
    }

    @Override
    public void update() {
    }

    @Override
    public AABB getBoundingBox() {
        return AABB.fromCenterAndSize(location, .5);
    }

    @Override
    public CollisionOption getCollisionOption() {
        return CollisionOption.SOLID;
    }

    public static Entity unpack(MessageUnpacker unpacker, UUID uuid, Location location, Direction direction) throws IOException {
        Bush entity = new Bush(uuid, location);
        entity.setDirection(direction);
        return entity;
    }
}
