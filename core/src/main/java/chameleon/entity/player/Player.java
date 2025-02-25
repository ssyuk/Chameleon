package chameleon.entity.player;

import chameleon.entity.CollisionOption;
import chameleon.entity.Entity;
import chameleon.utils.Direction;
import chameleon.utils.Location;
import chameleon.utils.colliding.AABB;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.UUID;

public class Player extends Entity {
    protected static final double SPEED = 0.03;
    private final String name;

    public Player(String name, UUID uuid, Location location) {
        super(uuid, location);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String id() {
        return "player";
    }

    @Override
    public void update() {
    }

    @Override
    public AABB getBoundingBox() {
        return AABB.fromLRTB(location, .25, .25, .1, .15);
    }

    @Override
    public AABB getInteractiveBoundingBox() {
        return AABB.fromLRTB(location, .25, .25, .75, .15);
    }

    @Override
    public CollisionOption getCollisionOption() {
        return CollisionOption.SOLID;
    }

    @Override
    public void pack(MessagePacker packer) throws IOException {
        super.pack(packer);
        packer.packString(name);
    }

    @Override
    public int packingSize() {
        return super.packingSize() + 1;
    }

    public static Entity unpack(MessageUnpacker unpacker, UUID uuid, Location location, Direction direction) throws IOException {
        String name = unpacker.unpackString();
        Player player = new Player(name, uuid, location);
        player.setDirection(direction);
        return player;
    }
}
