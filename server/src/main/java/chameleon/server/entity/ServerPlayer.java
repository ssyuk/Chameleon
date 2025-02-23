package chameleon.server.entity;

import chameleon.entity.player.Player;
import chameleon.utils.Location;

import java.net.Socket;
import java.util.UUID;

public class ServerPlayer extends Player {
    private final Socket clientSocket;

    public ServerPlayer(String name, UUID uuid, Location location, Socket clientSocket) {
        super(name, uuid, location);
        this.clientSocket = clientSocket;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
