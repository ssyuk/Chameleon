package chameleon.world.tile;

public class Tile {
    public static final Tile GRASS = new Tile("grass");

    private final String id;

    public Tile(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
