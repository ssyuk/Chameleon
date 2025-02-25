package chameleon.client.net;

import chameleon.client.ChameleonClient;
import chameleon.entity.Entity;
import chameleon.entity.player.Player;
import chameleon.net.Connector;
import chameleon.net.packet.*;
import chameleon.utils.Direction;
import chameleon.utils.Location;
import chameleon.utils.Version;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ConnectorClient extends Connector {
    private Socket socket;
    private final InetAddress address;
    private final int port;
    private DataOutputStream dataOutputStream;
    private boolean connected = false;
    private boolean ended = false;

    public ConnectorClient(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
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
                if (parsePacket(data)) {
                    break;
                }
            }

            socket.close();
            ended = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean parsePacket(byte[] data) throws IOException {
        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(data);
        Packet.PacketTypes type = Packet.lookupPacket(unpacker.unpackInt());

        ChameleonClient client = ChameleonClient.getInstance();

        return switch (type) {
            case LOGIN -> {
                Packet00Login packet = new Packet00Login(unpacker);
                if (!packet.uuid().equals(client.getClientPlayer().uuid())) {
                    System.out.println("[SERVER] " + packet.username() + " has joined the game.");
                    Player player = new Player(packet.username(), packet.uuid(), new Location(client.getWorld(), packet.location().x(), packet.location().y()));
                    client.getWorld().addEntity(player);
                }
                yield false;
            }
            case DISCONNECT -> {
                Packet01Disconnect packet = new Packet01Disconnect(unpacker);
                if (!packet.uuid().equals(client.getClientPlayer().uuid())) {
                    System.out.println("[SERVER] " + ((Player) client.getWorld().getEntityByUuid(packet.uuid())).getName() + " has left the game.");
                    client.getWorld().removeEntity(packet.uuid());
                }
                yield false;
            }
            case SERVER_INFO -> {
                Packet02ServerInfo packet = new Packet02ServerInfo(unpacker);
                Version serverVersion = packet.serverVersion();

                if (serverVersion.isNewerThan(client.getVersion())) {
                    System.out.println("Server is running a newer version of the client.");
                    System.out.println("Server version: " + serverVersion);
                    System.out.println("Client version: " + client.getVersion());
                    System.out.println("Please update your client to connect to the server.");
                    yield true;
                }

                if (serverVersion.isOlderThan(client.getVersion())) {
                    System.out.println("Server is running an older version of the client.");
                    System.out.println("Server version: " + serverVersion);
                    System.out.println("Client version: " + client.getVersion());
                    System.out.println("Please use legacy version of the client to connect to the server.");
                    yield true;
                }

                System.out.println("Connected!!!");
                connected = true;
                yield false;
            }
            case WORLD_DATA -> {
                Packet03WorldData packet = new Packet03WorldData(unpacker);
                client.setWorld(packet.world());
                yield false;
            }
            case ENTITY_MOVED -> {
                Packet05EntityMoved packet = new Packet05EntityMoved(unpacker);
                Entity entity = client.getWorld().getEntityByUuid(packet.targetUuid());
                Location newLocation = new Location(client.getWorld(), packet.destinationX(), packet.destinationY());
                entity.setMoving(packet.moving());
                entity.setDirection(Direction.calculate(entity.getLocation(), newLocation, entity.getDirection()));
                entity.teleport(newLocation);
                yield false;
            }
            case TILE_INFO -> {
                Packet07TileInfo packet = new Packet07TileInfo(unpacker);
                client.getWorld().setTileAt(packet.tileLocation(), packet.tile());
                client.getWorld().setHeightAt(packet.tileLocation(), packet.height());
                yield false;
            }
            default -> {
                System.out.println("[SERVER] Invalid packet received: " + type);
                yield false;
            }
        };
    }

    @Override
    public void send(Packet packet) {
        try {
            send(packet.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(byte[] data) {
        try {
            if (!socket.isClosed()) {
                dataOutputStream.writeInt(data.length);
                dataOutputStream.write(data);
                dataOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isEnded() {
        return ended;
    }

    public boolean isConnected() {
        return connected;
    }
}
