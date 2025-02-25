package chameleon.net.packet;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class Packet02ServerInfo extends Packet { // TODO
    public Packet02ServerInfo(MessageUnpacker unpacker) {
    }

    public Packet02ServerInfo() {
    }

    @Override
    public byte[] getData() throws IOException {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        packer.packInt(2);
        return packer.toByteArray();
    }
}
