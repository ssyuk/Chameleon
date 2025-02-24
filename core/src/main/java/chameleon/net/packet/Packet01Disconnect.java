package chameleon.net.packet;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.UUID;

public class Packet01Disconnect extends Packet { // TODO
    private final UUID uuid;

    public Packet01Disconnect(MessageUnpacker unpacker) {
        try {
            this.uuid = UUID.fromString(unpacker.unpackString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Packet01Disconnect(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID uuid() {
        return uuid;
    }

    @Override
    public byte[] getData() throws IOException {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        packer.packInt(1);
        packer.packString(uuid.toString());
        return packer.toByteArray();
    }
}
