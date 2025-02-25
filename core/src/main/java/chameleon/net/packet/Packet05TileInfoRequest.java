package chameleon.net.packet;

import chameleon.Chameleon;
import chameleon.utils.Location;
import chameleon.world.tile.Tile;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.UUID;

public class Packet05TileInfoRequest extends Packet {
    private final Location tileLocation;

    public Packet05TileInfoRequest(MessageUnpacker unpacker) {
        try {
            double x = unpacker.unpackDouble();
            double y = unpacker.unpackDouble();
            this.tileLocation = new Location(Chameleon.getInstance().getWorld(), x, y).toTileLocation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Packet05TileInfoRequest(Location location) {
        this.tileLocation = location.toTileLocation();
    }

    public Location tileLocation() {
        return tileLocation;
    }

    @Override
    public byte[] getData() throws IOException {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        packer.packInt(5);
        packer.packDouble(tileLocation.x());
        packer.packDouble(tileLocation.y());
        return packer.toByteArray();
    }
}
