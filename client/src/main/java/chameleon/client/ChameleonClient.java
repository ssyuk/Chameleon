package chameleon.client;

import chameleon.Chameleon;
import chameleon.client.assets.AssetLoader;
import chameleon.client.assets.AssetManager;
import chameleon.client.entity.player.ClientPlayer;
import chameleon.client.net.ConnectorClient;
import chameleon.client.renderer.MasterRenderer;
import chameleon.client.renderer.entity.EntityRenderer;
import chameleon.client.renderer.entity.PlayerRenderer;
import chameleon.client.renderer.entity.TileEntityRenderer;
import chameleon.client.utils.KeyHandler;
import chameleon.client.utils.MouseHandler;
import chameleon.client.window.Window;
import chameleon.world.generator.NetworkWorldGenerator;
import chameleon.entity.Entity;
import chameleon.entity.tile.BrokenTree;
import chameleon.entity.tile.Stairs;
import chameleon.net.packet.Packet01Disconnect;
import chameleon.utils.Location;
import chameleon.world.World;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;

public class ChameleonClient extends Chameleon {
    private ChameleonClient() {
        super("Chameleon Client");
        Chameleon.INSTANCE = this;
    }

    public static final int TILE_SIZE = 64;

    private static final ChameleonClient INSTANCE = new ChameleonClient();
    private boolean running = false;

    private int frames = 0;
    private int updates = 0;

    private final KeyHandler keyHandler = new KeyHandler();
    private final MouseHandler mouseHandler = new MouseHandler();
    private final Window window = new Window(this);
    private final MasterRenderer renderer = new MasterRenderer();
    private final AssetManager assetManager = new AssetManager();
    private final AssetLoader assetLoader = new AssetLoader();

    private World world;
    private ClientPlayer player;

    private @Nullable ConnectorClient connector;

    public static ChameleonClient getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isServer() {
        return false;
    }

    public boolean isRunning() {
        return running;
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public MouseHandler getMouseHandler() {
        return mouseHandler;
    }

    public Window getWindow() {
        return window;
    }

    public MasterRenderer getRenderer() {
        return renderer;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;

        List<Entity> entities = world.getEntities();
        entities.removeIf(entity -> entity.uuid().equals(getClientPlayer().uuid()));
        entities.add(getClientPlayer());

        for (Entity entity : entities) {
            entity.teleport(new Location(world, entity.getLocation().x(), entity.getLocation().y()));
        }

        world.setEntities(entities);
    }

    public ClientPlayer getClientPlayer() {
        return player;
    }

    public boolean isOnline() {
        return connector != null;
    }

    @Override
    public ConnectorClient getConnector() {
        return connector;
    }

    @Override
    public void run() {
        running = true;

        assetManager.load();

        try {
            String address = JOptionPane.showInputDialog(null, "Address", "Enter the server address (without port)", JOptionPane.QUESTION_MESSAGE);
            int port = Integer.parseInt(JOptionPane.showInputDialog(null, "Port", "Enter the server port", JOptionPane.QUESTION_MESSAGE));
            connector = new ConnectorClient(InetAddress.getByName(address), port);
            connector.start();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        world = new World(new NetworkWorldGenerator());
        player = new ClientPlayer("player" + (new Random().nextInt(899) + 100), new Location(world, 0.5, 0.5));

        world.addEntity(player);

        world.addEntity(new BrokenTree(new Location(world, 1.5, .5)));

//        // load height.txt
//        Path path = Paths.get("height.txt");
//        try {
//            AtomicInteger y = new AtomicInteger(-10);
//            Files.lines(path).forEach(s -> {
//                int x = -10;
//                for (char h : s.toCharArray()) {
//                    world.setHeightAt(new Location(world, x, y.get()), Integer.parseInt(h + ""));
//                    x++;
//                }
//                y.getAndIncrement();
//            });
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        world.addEntity(new Stairs(new Location(world, 4, 0)));
        world.addEntity(new Stairs(new Location(world, 1, 2)));

        EntityRenderer.register("player", entity -> new PlayerRenderer());
        EntityRenderer.register("bush", entity -> new TileEntityRenderer());
        EntityRenderer.register("weed", entity -> new TileEntityRenderer());
        EntityRenderer.register("broken_tree", entity -> new TileEntityRenderer());
        EntityRenderer.register("stairs", entity -> new TileEntityRenderer());

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

            if ((now - lastRender) / 1.0E9 > 1.0 / 120/*frames per second*/) {
                frames++;
                lastRender = System.nanoTime();
                renderer.render();
            }

            if (System.currentTimeMillis() - lastTimer1 > 1000) {
                lastTimer1 += 1000;

                frames = 0;
                updates = 0;
            }
        }

        window.dispose();

        System.out.println("Game ended");
    }

    public void update() {
        if (!isOnline()) world.update();
        else player.update();
    }

    public int currentUpdateCount() {
        return updates;
    }

    public void end() {
        if (isOnline()) {
            connector.send(new Packet01Disconnect(getClientPlayer().uuid()));
        }
        running = false;
        System.out.println("Ending game");
    }
}
