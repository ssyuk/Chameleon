package chameleon.client;

import chameleon.Chameleon;
import chameleon.client.assets.AssetLoader;
import chameleon.client.assets.AssetManager;
import chameleon.client.entity.player.ClientPlayer;
import chameleon.client.net.ConnectorClient;
import chameleon.client.renderer.Brush;
import chameleon.client.renderer.GameRenderer;
import chameleon.client.renderer.entity.EntityRenderer;
import chameleon.client.renderer.entity.PlayerRenderer;
import chameleon.client.renderer.entity.TileEntityRenderer;
import chameleon.client.screen.Screen;
import chameleon.client.screen.TitleScreen;
import chameleon.client.utils.KeyHandler;
import chameleon.client.utils.MouseHandler;
import chameleon.client.window.Window;
import chameleon.entity.Entity;
import chameleon.net.packet.Packet01Disconnect;
import chameleon.utils.Location;
import chameleon.utils.Version;
import chameleon.world.World;
import chameleon.world.generator.NetworkWorldGenerator;
import chameleon.world.generator.NoiseWorldGenerator;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
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
    private final GameRenderer renderer = new GameRenderer();
    private final AssetManager assetManager = new AssetManager();
    private final AssetLoader assetLoader = new AssetLoader();

    private Screen screen;
    private World world;
    private ClientPlayer player;

    private @Nullable ConnectorClient connector;

    public static ChameleonClient getInstance() {
        return INSTANCE;
    }

    @Override
    public Version getVersion() {
        return new Version(1, 0, 0);
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

    public GameRenderer getRenderer() {
        return renderer;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
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

        EntityRenderer.register("player", entity -> new PlayerRenderer());
        EntityRenderer.register("bush", entity -> new TileEntityRenderer());
        EntityRenderer.register("weed", entity -> new TileEntityRenderer());
        EntityRenderer.register("broken_tree", entity -> new TileEntityRenderer());
        EntityRenderer.register("stairs", entity -> new TileEntityRenderer());

        setScreen(new TitleScreen());

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            System.out.println("An error occurred: " + e.getMessage());
        });

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
                render();
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

    public int fps() {
        return frames;
    }

    public int ups() {
        return updates;
    }

    public void render() {
        Graphics graphics = window.getBufferStrategy().getDrawGraphics();
        Brush brush = new Brush(window.getInsets(), graphics);

        brush.drawRect(0, 0, window.getWidth(), window.getHeight(), 0xFFFFFF);

        if (isInGame()) {
            renderer.render(brush);
        }

        if (screen != null) screen.render(brush);

        graphics.dispose();
        window.getBufferStrategy().show();
    }

    public void update() {
        if (screen != null) screen.update();

        if (isInGame()) {
            if (!isOnline()) world.update();
            else player.update();

            if (keyHandler.isKeyDown(KeyEvent.VK_ESCAPE)) leaveGame();
        }
    }

    public int currentUpdateCount() {
        return updates;
    }

    public boolean isInGame() {
        return world != null;
    }

    public void end() {
        if (isOnline()) {
            connector.send(new Packet01Disconnect(getClientPlayer().uuid()));
        }
        running = false;
        System.out.println("Ending game");
    }

    public void playSingleplayer() {
        connector = null;

        world = new World(new NoiseWorldGenerator(new Random().nextLong()));
        player = new ClientPlayer("player" + (new Random().nextInt(899) + 100), new Location(world, 0.5, 0.5));

        world.addEntity(player);
    }

    public void playMultiplayer() {
        world = new World(new NetworkWorldGenerator());
        player = new ClientPlayer("player" + (new Random().nextInt(899) + 100), new Location(world, 0.5, 0.5));

        try {
            String address = JOptionPane.showInputDialog(null, "Address", "Enter the server address (without port)", JOptionPane.QUESTION_MESSAGE);
            int port = Integer.parseInt(JOptionPane.showInputDialog(null, "Port", "Enter the server port", JOptionPane.QUESTION_MESSAGE));
            if (address == null || address.isEmpty() || port == 0) {
                System.out.println("No address provided");
                return;
            }
            connector = new ConnectorClient(InetAddress.getByName(address), port);
            connector.start();
            while (!connector.isConnected() && !connector.isEnded()) {
                Thread.sleep(100);
                System.out.println("Waiting for connection...");
            }

            if (connector.isEnded()) {
                System.out.println("Connection ended");
                leaveGame();
                return;
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            leaveGame();
            return;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        world.addEntity(player);
    }

    public void leaveGame() {
        if (isOnline()) {
            connector.send(new Packet01Disconnect(getClientPlayer().uuid()));
        }
        world = null;
        player = null;
        connector = null;
        setScreen(new TitleScreen());
    }
}
