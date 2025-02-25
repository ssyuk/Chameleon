package chameleon.net.packet;

import chameleon.entity.Entity;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.UUID;

public class Packet05EntityMoved extends Packet {
    private final UUID targetUuid;
    private final double destinationX;
    private final double destinationY;
    private final boolean moving;

    public Packet05EntityMoved(MessageUnpacker unpacker) {
        try {
            targetUuid = UUID.fromString(unpacker.unpackString());
            destinationX = unpacker.unpackDouble();
            destinationY = unpacker.unpackDouble();
            moving = unpacker.unpackBoolean();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Packet05EntityMoved(Entity entity) {
        this.targetUuid = entity.uuid();
        this.destinationX = entity.getLocation().x();
        this.destinationY = entity.getLocation().y();
        this.moving = entity.isMoving();
    }

    public UUID targetUuid() {
        return targetUuid;
    }

    public double destinationX() {
        return destinationX;
    }

    public double destinationY() {
        return destinationY;
    }

    public boolean moving() {
        return moving;
    }

    @Override
    public byte[] getData() throws IOException {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        packer.packInt(5);
        packer.packString(targetUuid.toString());
        packer.packDouble(destinationX);
        packer.packDouble(destinationY);
        packer.packBoolean(moving);
        return packer.toByteArray();
    }
}
