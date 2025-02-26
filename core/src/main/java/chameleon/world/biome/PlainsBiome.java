package chameleon.world.biome;

import java.awt.*;

public class PlainsBiome extends Biome {
    public PlainsBiome() {
        super(0.5, 0.5, 0.5); // 이상적인 값 (예시)
    }

    @Override
    public Color tile() {
        return new Color(0x7cfc00);
    }
}
