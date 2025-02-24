package chameleon.server.net;

import chameleon.entity.Entity;
import chameleon.entity.player.Player;
import chameleon.net.Connector;
import chameleon.net.packet.*;
import chameleon.server.ChameleonServer;
import chameleon.server.entity.ServerPlayer;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.*;

public class ConnectorServer extends Connector {
    private final int port;
    private final ExecutorService clientHandlers = Executors.newCachedThreadPool();

    public ConnectorServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (ChameleonServer.getInstance().isRunning()) {
            try {
                Socket clientSocket = serverSocket.accept();
                clientHandlers.submit(() -> handleClient(clientSocket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (InputStream inputStream = clientSocket.getInputStream();
             DataInputStream dataInputStream = new DataInputStream(inputStream);
             OutputStream outputStream = clientSocket.getOutputStream();
             DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            while (ChameleonServer.getInstance().isRunning()) {
                int length = dataInputStream.readInt();
                if (length == -1) break;

                byte[] data = new byte[length];
                dataInputStream.readFully(data);
                System.out.println("[" + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + "] " + "Received data... : " + Arrays.toString(data));
                if (parsePacket(data, clientSocket)) break;
            }
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean parsePacket(byte[] data, Socket clientSocket) throws IOException {
        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(data);
        Packet.PacketTypes type = Packet.lookupPacket(unpacker.unpackInt());

        ChameleonServer server = ChameleonServer.getInstance();

        System.out.println("[" + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + "] " + "Received packet... : " + type);
        return switch (type) {
            case LOGIN -> {
                Packet00Login packet = new Packet00Login(unpacker);
                System.out.println("[" + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + "] " + packet.username() + " has connected...");
                ServerPlayer player = new ServerPlayer(packet.username(), packet.uuid(), packet.location(), clientSocket);
                server.joinPlayer(player);

                Packet02WorldData worldData = new Packet02WorldData(server.getWorld());
                sendToAll(worldData);
                yield false;
            }
            case DISCONNECT -> {
                Packet01Disconnect packet = new Packet01Disconnect(unpacker);
                System.out.println("[SERVER] " + ((Player) server.getWorld().getEntityByUuid(packet.uuid())).getName() + " has left the game.");
                server.leavePlayer(packet.uuid());
                yield true;
            }
            case ENTITY_MOVE -> {
                Packet03EntityMove packet = new Packet03EntityMove(unpacker);
                Entity entity = server.getWorld().getEntityByUuid(packet.getTargetUuid());
                if (entity == null) {
                    System.out.println("[" + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + "] " + "Entity not found... : " + packet.getTargetUuid());
                    yield false;
                }
                if (entity.move(packet.getDisplacement(), false)) {
                    entity.setMoving(packet.isMoving());
                    sendToAll(packet);
                }
                yield false;
            }
            default -> {
                System.out.println("[" + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + "] " + "Invalid packet received... : " + type);
                yield false;
            }
        };
    }

    public void sendToAll(Packet packet) {
        for (ServerPlayer player : ChameleonServer.getInstance().getPlayers()) {
            try {
                send(packet.getData(), new DataOutputStream(player.getClientSocket().getOutputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void send(byte[] data, DataOutputStream dataOutputStream) {
        try {
            System.out.println("Send: " + data.length);
            dataOutputStream.writeInt(data.length);
            dataOutputStream.write(data);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
