package chameleon.net.packet;

import chameleon.entity.Entity;
import chameleon.utils.Location;
import chameleon.world.World;
import chameleon.world.generator.NetworkWorldGenerator;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Packet03WorldData extends Packet { // TODO
    private final World world;

    public Packet03WorldData(MessageUnpacker unpacker) {
        this.world = new World(new NetworkWorldGenerator());

        try {
            // unpack height map
            Map<Location, Integer> heightMap = world.getHeightMap();
            int heightMapLength = unpacker.unpackArrayHeader();
            for (int i = 0; i < heightMapLength; i++) {
                unpacker.unpackMapHeader();
                double x = unpacker.unpackDouble();
                double y = unpacker.unpackDouble();
                int height = unpacker.unpackInt();
                heightMap.put(new Location(world, x, y), height);
            }

            // unpack entity
            List<Entity> entities = new ArrayList<>();

            int entityCount = unpacker.unpackArrayHeader();
            for (int i = 0; i < entityCount; i++) {
                unpacker.unpackMapHeader();
                entities.add(Entity.unpack(unpacker));
            }

            world.setEntities(entities);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Packet03WorldData(World world) {
        this.world = world;
    }

    public World world() {
        return world;
    }

    @Override
    public byte[] getData() throws IOException {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        packer.packInt(3);
        // TODO: tile

        // pack height map
        Map<Location, Integer> heightMap = world.getHeightMap();
        packer.packArrayHeader(heightMap.size());
        for (Map.Entry<Location, Integer> entry : heightMap.entrySet()) {
            packer.packMapHeader(3);
            packer.packDouble(entry.getKey().x());
            packer.packDouble(entry.getKey().y());
            packer.packInt(entry.getValue());
        }

        // pack entity
        List<Entity> entities = world.getEntities();
        packer.packArrayHeader(entities.size());
        for (Entity entity : entities) {
            packer.packMapHeader(entity.packingSize());
            entity.pack(packer);
        }
        return packer.toByteArray();
    }
}
