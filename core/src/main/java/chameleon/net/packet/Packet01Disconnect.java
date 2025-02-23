package chameleon.net.packet;

import chameleon.Chameleon;
import chameleon.entity.player.Player;
import chameleon.utils.Location;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.UUID;

public class Packet01Disconnect extends Packet { // TODO
    private final String username;
    private final UUID uuid;
    private final Location location;

    public Packet01Disconnect(MessageUnpacker unpacker) {
        try {
            this.username = unpacker.unpackString();
            this.uuid = UUID.fromString(unpacker.unpackString());
            this.location = new Location(Chameleon.getInstance().getWorld(), unpacker.unpackDouble(), unpacker.unpackDouble());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Packet01Disconnect(Player player) {
        this.username = player.getName();
        this.uuid = player.uuid();
        this.location = player.getLocation();
    }

    public String username() {
        return username;
    }

    public UUID uuid() {
        return uuid;
    }

    public Location location() {
        return location;
    }

    @Override
    public byte[] getData() throws IOException {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        packer.packInt(1);
        packer.packString(username);
        packer.packString(uuid.toString());
        packer.packDouble(location.x());
        packer.packDouble(location.y());
        return packer.toByteArray();
    }
}
