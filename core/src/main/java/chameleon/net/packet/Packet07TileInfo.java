package chameleon.net.packet;

import chameleon.Chameleon;
import chameleon.utils.Location;
import chameleon.world.tile.Tile;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class Packet07TileInfo extends Packet {
    private final Location tileLocation;
    private final Tile tile;
    private final int height;

    public Packet07TileInfo(MessageUnpacker unpacker) {
        try {
            double x = unpacker.unpackDouble();
            double y = unpacker.unpackDouble();
            this.tileLocation = new Location(Chameleon.getInstance().getWorld(), x, y).toTileLocation();
            this.tile = Tile.fromId(unpacker.unpackString());
            this.height = unpacker.unpackInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Packet07TileInfo(Location location, Tile tile, int height) {
        this.tileLocation = location.toTileLocation();
        this.tile = tile;
        this.height = height;
    }

    public Location tileLocation() {
        return tileLocation;
    }

    public Tile tile() {
        return tile;
    }

    public int height() {
        return height;
    }

    @Override
    public byte[] getData() throws IOException {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        packer.packInt(7);
        packer.packDouble(tileLocation.x());
        packer.packDouble(tileLocation.y());
        packer.packString(tile.id());
        packer.packInt(height);
        return packer.toByteArray();
    }
}
