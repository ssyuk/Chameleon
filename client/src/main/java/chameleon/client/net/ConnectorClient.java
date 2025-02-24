package chameleon.client.net;

import chameleon.client.ChameleonClient;
import chameleon.entity.Entity;
import chameleon.entity.player.Player;
import chameleon.net.Connector;
import chameleon.net.packet.*;
import chameleon.utils.Location;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.*;
import java.net.*;

public class ConnectorClient extends Connector {
    private final InetAddress address;
    private final int port;
    private DataOutputStream dataOutputStream;

    public ConnectorClient(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        Socket socket;
        try {
            socket = new Socket(address, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (OutputStream outputStream = socket.getOutputStream();
             DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
             InputStream inputStream = socket.getInputStream();
             DataInputStream dataInputStream = new DataInputStream(inputStream)) {

            this.dataOutputStream = dataOutputStream;
            send(new Packet00Login(ChameleonClient.getInstance().getClientPlayer()));

            while (ChameleonClient.getInstance().isRunning()) {
                int length = dataInputStream.readInt();
                if (length == -1) break;

                byte[] data = new byte[length];
                dataInputStream.readFully(data);
                parsePacket(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parsePacket(byte[] data) throws IOException {
        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(data);
        Packet.PacketTypes type = Packet.lookupPacket(unpacker.unpackInt());

        ChameleonClient client = ChameleonClient.getInstance();

        switch (type) {
            case LOGIN: {
                Packet00Login packet = new Packet00Login(unpacker);
                if (!packet.uuid().equals(client.getClientPlayer().uuid())) {
                    System.out.println("[SERVER] " + packet.username() + " has joined the game.");
                    Player player = new Player(packet.username(), packet.uuid(), new Location(client.getWorld(), packet.location().x(), packet.location().y()));
                    client.getWorld().addEntity(player);
                }
                break;
            }
            case DISCONNECT: {
                Packet01Disconnect packet = new Packet01Disconnect(unpacker);
                if (!packet.uuid().equals(client.getClientPlayer().uuid())) {
                    System.out.println("[SERVER] " + ((Player) client.getWorld().getEntityByUuid(packet.uuid())).getName() + " has left the game.");
                    client.getWorld().removeEntity(packet.uuid());
                }
                break;
            }
            case WORLD_DATA: {
                Packet02WorldData packet = new Packet02WorldData(unpacker);
                client.setWorld(packet.world());
                break;
            }
            case ENTITY_MOVE: {
                Packet03EntityMove packet = new Packet03EntityMove(unpacker);
                if (!packet.getTargetUuid().equals(client.getClientPlayer().uuid())) {
                    Entity entity = client.getWorld().getEntityByUuid(packet.getTargetUuid());
                    entity.setMoving(packet.isMoving());
                }
                break;
            }
            case INVALID:
            default:
                System.out.println("[SERVER] Invalid packet received: " + type);
                break;
        }
    }

    public void send(Packet packet) {
        try {
            send(packet.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(byte[] data) {
        try {
            dataOutputStream.writeInt(data.length);
            dataOutputStream.write(data);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
