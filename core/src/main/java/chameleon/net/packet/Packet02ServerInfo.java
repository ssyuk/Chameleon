package chameleon.net.packet;

import chameleon.utils.Version;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class Packet02ServerInfo extends Packet {
    private final Version serverVersion;

    public Packet02ServerInfo(MessageUnpacker unpacker) {
        try {
            int major = unpacker.unpackInt();
            int minor = unpacker.unpackInt();
            int patch = unpacker.unpackInt();
            this.serverVersion = new Version(major, minor, patch);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Packet02ServerInfo(Version serverVersion) {
        this.serverVersion = serverVersion;
    }

    public Version serverVersion() {
        return serverVersion;
    }

    @Override
    public byte[] getData() throws IOException {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        packer.packInt(2);
        packer.packInt(serverVersion.major());
        packer.packInt(serverVersion.minor());
        packer.packInt(serverVersion.patch());
        return packer.toByteArray();
    }
}
