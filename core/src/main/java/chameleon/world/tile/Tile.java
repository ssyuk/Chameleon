package chameleon.world.tile;

public class Tile {
    public static final Tile GRASS = new Tile("grass");
    public static final Tile SLOPE = new Tile("slope", true);

    private final String id;
    private final boolean slope;

    public Tile(String id, boolean slope) {
        this.id = id;
        this.slope = slope;
    }

    public Tile(String id) {
        this(id, false);
    }

    public String id() {
        return id;
    }

    public boolean isSlope() {
        return slope;
    }
}
