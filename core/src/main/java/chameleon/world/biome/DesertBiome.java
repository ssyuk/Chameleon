package chameleon.world.biome;

import java.awt.*;

public class DesertBiome extends Biome {
    public DesertBiome() {
        super(0.5, 0.8, 0.2); // 이상적인 값 (예시)
    }

    @Override
    public Color tile() {
        return new Color(0xf0e68c);
    }
}
