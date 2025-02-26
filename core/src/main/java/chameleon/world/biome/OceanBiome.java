package chameleon.world.biome;

import java.awt.*;

public class OceanBiome extends Biome {
    public OceanBiome() {
        super(0.1, 0.5, 1.0);
    }

    @Override
    public Color tile() {
        return new Color(0x0000ff);
    }
}
