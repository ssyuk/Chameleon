package chameleon.entity.tile;

import chameleon.entity.CollisionOption;
import chameleon.entity.Entity;
import chameleon.entity.player.Player;
import chameleon.utils.Direction;
import chameleon.utils.Location;
import chameleon.utils.colliding.AABB;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.UUID;

public class BrokenTree extends TileEntity {
    public BrokenTree(UUID uuid, Location location) {
        super(uuid, location);
    }

    public BrokenTree(Location location) {
        super(location);
    }

    @Override
    public String id() {
        return "broken_tree";
    }

    @Override
    public void update() {
    }

    @Override
    public AABB getBoundingBox() {
        return AABB.fromCenterAndSize(location, .35);
    }

    @Override
    public CollisionOption getCollisionOption() {
        return CollisionOption.SOLID;
    }

    public static Entity unpack(MessageUnpacker unpacker, UUID uuid, Location location, Direction direction) throws IOException {
        BrokenTree entity = new BrokenTree(uuid, location);
        entity.setDirection(direction);
        return entity;
    }
}
