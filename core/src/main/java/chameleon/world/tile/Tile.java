package chameleon.world.tile;

import java.awt.*;

public class Tile {
    public static final Tile GRASS = new Tile(new Color(0x66C556));
    public static final Tile DIRT = new Tile(new Color(0x8B4513));

    private final Color color;

    public Tile(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
