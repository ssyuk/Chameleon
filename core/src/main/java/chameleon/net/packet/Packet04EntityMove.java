package chameleon.net.packet;

import chameleon.utils.Vec2d;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.UUID;

public class Packet04EntityMove extends Packet { // TODO
    private final UUID targetUuid;
    private final Vec2d displacement;
    private final boolean moving;

    public Packet04EntityMove(MessageUnpacker unpacker) {
        try {
            targetUuid = UUID.fromString(unpacker.unpackString());
            double x = unpacker.unpackDouble();
            double y = unpacker.unpackDouble();
            displacement = new Vec2d(x, y);
            moving = unpacker.unpackBoolean();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Packet04EntityMove(UUID targetUuid, Vec2d displacement, boolean moving) {
        this.targetUuid = targetUuid;
        this.displacement = displacement;
        this.moving = moving;
    }

    public UUID getTargetUuid() {
        return targetUuid;
    }

    public Vec2d getDisplacement() {
        return displacement;
    }

    public boolean isMoving() {
        return moving;
    }

    @Override
    public byte[] getData() throws IOException {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        packer.packInt(4);
        packer.packString(targetUuid.toString());
        packer.packDouble(displacement.x());
        packer.packDouble(displacement.y());
        packer.packBoolean(moving);
        return packer.toByteArray();
    }
}
