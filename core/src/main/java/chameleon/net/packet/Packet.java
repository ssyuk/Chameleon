package chameleon.net.packet;

import chameleon.Chameleon;

import java.io.IOException;
import java.net.InetAddress;

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
        INVALID(-1), LOGIN(0), DISCONNECT(1), SERVER_INFO(2), WORLD_DATA(3), ENTITY_MOVE(4), TILE_INFO_REQUEST(5), TILE_INFO(6);

        private final int id;

        PacketTypes(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public String readData(byte[] data) {
        return new String(data).trim().substring(2);
    }

    public abstract byte[] getData() throws IOException;
}
