package chameleon.net.packet;

import java.io.IOException;

public abstract class Packet {
    public static PacketTypes lookupPacket(int id) {
        for (PacketTypes value : PacketTypes.values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        return PacketTypes.INVALID;
    }

    public enum PacketTypes {
        INVALID(-1), LOGIN(0), DISCONNECT(1), SERVER_INFO(2), WORLD_DATA(3), ENTITY_MOVE_REQUEST(4), ENTITY_MOVED(5), TILE_INFO_REQUEST(6), TILE_INFO(7);

        private final int id;

        PacketTypes(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public abstract byte[] getData() throws IOException;
}
