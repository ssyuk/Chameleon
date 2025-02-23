package chameleon.entity.tile;

import chameleon.entity.CollisionOption;
import chameleon.entity.Entity;
import chameleon.utils.Direction;
import chameleon.utils.Location;
import chameleon.utils.colliding.AABB;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.UUID;

public class Weed extends TileEntity {
    public Weed(UUID uuid, Location location) {
        super(uuid, location);
    }

    public Weed(Location location) {
        super(location);
    }

    @Override
    public String id() {
        return "weed";
    }

    @Override
    public void update() {
    }

    @Override
    public AABB getBoundingBox() {
        return AABB.fromCenterAndSize(location, .3);
    }

    @Override
    public CollisionOption getCollisionOption() {
        return CollisionOption.NONE;
    }

    public static Entity unpack(MessageUnpacker unpacker, UUID uuid, Location location, Direction direction) throws IOException {
        Weed entity = new Weed(uuid, location);
        entity.setDirection(direction);
        return entity;
    }
}
