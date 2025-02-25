package chameleon.net.packet;

import chameleon.Chameleon;
import chameleon.utils.Location;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class Packet06TileInfoRequest extends Packet {
    private final Location tileLocation;

    public Packet06TileInfoRequest(MessageUnpacker unpacker) {
        try {
            double x = unpacker.unpackDouble();
            double y = unpacker.unpackDouble();
            this.tileLocation = new Location(Chameleon.getInstance().getWorld(), x, y).toTileLocation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Packet06TileInfoRequest(Location location) {
        this.tileLocation = location.toTileLocation();
    }

    public Location tileLocation() {
        return tileLocation;
    }

    @Override
    public byte[] getData() throws IOException {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        packer.packInt(6);
        packer.packDouble(tileLocation.x());
        packer.packDouble(tileLocation.y());
        return packer.toByteArray();
    }
}
