package chameleon.world.tile;

import java.awt.*;

public class Tile {
    public static final Tile VOID = new Tile("void", Color.WHITE);
    public static final Tile GRASS = new Tile("grass", new Color(0x00FF00));
    public static final Tile SAND = new Tile("sand", new Color(0xF1F1AE));
    public static final Tile WATER = new Tile("water" , new Color(0x0000FF));
    public static final Tile SNOW = new Tile("snow", new Color(0xFFFFFF));

    private final String id;
    private final Color color;

    public Tile(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public static Tile fromId(String id) {
        switch (id) { // TODO
            case "grass":
                return GRASS;
            case "sand":
                return SAND;
            default:
                throw new IllegalArgumentException("Unknown tile id: " + id);
        }
    }

    public String id() {
        return id;
    }

    public Color color() {
        return color;
    }
}
