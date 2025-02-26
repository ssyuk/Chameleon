package chameleon;

import chameleon.net.Connector;
import chameleon.utils.Version;
import chameleon.world.World;
import chameleon.world.biome.Biome;

public abstract class Chameleon extends Thread {
    protected static Chameleon INSTANCE;

    public Chameleon(String name) {
        super(name);
    }

    @Override
    public void run() {
        Biome.initialize();
    }

    public static Chameleon getInstance() {
        return INSTANCE;
    }

    public abstract Version getVersion();

    public abstract boolean isServer();

    public abstract boolean isRunning();

    public abstract Connector getConnector();

    public abstract World getWorld();
}
