package chameleon.net.packet;

import chameleon.entity.Entity;
import chameleon.entity.player.Player;
import chameleon.utils.Location;
import chameleon.world.World;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Packet02WorldData extends Packet { // TODO
    private final World world;

    public Packet02WorldData(MessageUnpacker unpacker) {
        this.world = new World();
        List<Entity> entities = new ArrayList<>();

        try {
            int entityCount = unpacker.unpackArrayHeader();
            for (int i = 0; i < entityCount; i++) {
                unpacker.unpackMapHeader();
                entities.add(Entity.unpack(unpacker));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        world.setEntities(entities);
    }

    public Packet02WorldData(World world) {
        this.world = world;
    }

    public World world() {
        return world;
    }

    @Override
    public byte[] getData() throws IOException {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        packer.packInt(2);
        // TODO: tile
        List<Entity> entities = world.getEntities();
        packer.packArrayHeader(entities.size());
        for (Entity entity : entities) {
            packer.packMapHeader(entity.packingSize());
            entity.pack(packer);
        }
        return packer.toByteArray();
    }
}
