package chameleon.world.tile;

public class Tile {
    public static final Tile VOID = new Tile("void");
    public static final Tile GRASS = new Tile("grass");

    private final String id;

    public Tile(String id) {
        this.id = id;
    }

    public static Tile fromId(String id) {
        switch (id) { // TODO
            case "grass":
                return GRASS;
            default:
                throw new IllegalArgumentException("Unknown tile id: " + id);
        }
    }

    public String id() {
        return id;
    }
}
