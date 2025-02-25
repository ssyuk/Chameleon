package chameleon.world.generator;

import chameleon.utils.Location;
import chameleon.world.tile.Tile;
import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.pipeline.JNoise;

public class NoiseWorldGenerator extends WorldGenerator {
    private final JNoise noisePipeline;

    public NoiseWorldGenerator(int seed) {
        noisePipeline = JNoise.newBuilder().perlin(seed, Interpolation.LINEAR, FadeFunction.QUINTIC_POLY).scale(1 / 32d)
                .addModifier(v -> (v + 1) / 2.0).clamp(0.0, 1.0).build();
    }

    @Override
    public Tile generateTileAt(Location location) {
        return Tile.GRASS;
    }

    @Override
    public int generateHeightAt(Location location) {
        double noise = noisePipeline.evaluateNoise(location.x(), location.y());
        return (int) (noise * 10);
    }
}
