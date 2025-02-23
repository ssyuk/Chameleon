package chameleon.server;

import chameleon.Chameleon;
import chameleon.entity.tile.BrokenTree;
import chameleon.entity.tile.Bush;
import chameleon.entity.tile.TileEntity;
import chameleon.entity.tile.Weed;
import chameleon.net.packet.Packet00Login;
import chameleon.server.entity.ServerPlayer;
import chameleon.server.net.ConnectorServer;
import chameleon.utils.Location;
import chameleon.utils.TileLocation;
import chameleon.world.World;
import chameleon.world.tile.Tile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChameleonServer extends Chameleon {
    private ChameleonServer() {
        super("Chameleon Dedicated Server");
        Chameleon.INSTANCE = this;
    }

    private static final ChameleonServer INSTANCE = new ChameleonServer();
    private boolean running = false;

    private int updates = 0;

    private final World world = new World();
    private final List<ServerPlayer> players = new ArrayList<>();

    private final ConnectorServer connector = new ConnectorServer(8793);

    public static ChameleonServer getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    public World getWorld() {
        return world;
    }

    public List<ServerPlayer> getPlayers() {
        return players;
    }

    @Override
    public ConnectorServer getConnector() {
        return connector;
    }

    @Override
    public void run() {
        running = true;

        connector.start();

        long lastTime = System.nanoTime();
        long lastRender = System.nanoTime();
        double unprocessed = 0;
        long lastTimer1 = System.currentTimeMillis();

        while (running) {
            long now = System.nanoTime();
            double nsPerTick = 1E9D / 60/*updates per second*/;
            unprocessed += (now - lastTime) / nsPerTick;
            lastTime = now;
            while (unprocessed >= 1) {
                updates++;
                update();
                unprocessed--;
            }

            if (System.currentTimeMillis() - lastTimer1 > 1000) {
                lastTimer1 += 1000;

                System.out.println("UPS: " + updates);
                updates = 0;
            }
        }
    }

    public void update() {
        world.update();
    }

    public void joinPlayer(ServerPlayer player) {
        players.add(player);
        world.addEntity(player);
        connector.sendToAll(new Packet00Login(player));
    }
}
