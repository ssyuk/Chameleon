package chameleon.world.generator;

import chameleon.utils.Location;
import chameleon.world.biome.Biome;
import chameleon.world.tile.Tile;
import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.pipeline.JNoise;

public class NoiseWorldGenerator extends WorldGenerator {
    // 높이, 온도, 습도
    private final JNoise heightNoise, temperatureNoise, humidityNoise;

    public NoiseWorldGenerator(long seed) {
        heightNoise = JNoise.newBuilder().perlin(seed, Interpolation.LINEAR, FadeFunction.QUINTIC_POLY).scale(1 / 512d)
                .clamp(0.0, 1.0).build();
        temperatureNoise = JNoise.newBuilder().perlin(seed+1, Interpolation.LINEAR, FadeFunction.QUINTIC_POLY).scale(1 / 1024d)
               .clamp(0.0, 1.0).build();
        humidityNoise = JNoise.newBuilder().perlin(seed+2, Interpolation.LINEAR, FadeFunction.QUINTIC_POLY).scale(1 / 1024d)
               .clamp(0.0, 1.0).build();
    }

    @Override
    public Tile generateTileAt(Location location) {
        return getBiomeAt(location);
    }

    @Override
    public int generateHeightAt(Location location) {
        return 0;
    }

    private double getHeightAt(Location location) {
        return heightNoise.evaluateNoise(location.x(), location.y());
    }

    private double getTemperatureAt(Location location) {
        return temperatureNoise.evaluateNoise(location.x(), location.y());
    }

    private double getHumidityAt(Location location) {
        return humidityNoise.evaluateNoise(location.x(), location.y());
    }

    private Tile getBiomeAt(Location location) {
        double height = getHeightAt(location);
        double temperature = getTemperatureAt(location);
        double humidity = getHumidityAt(location);
        return new Tile("", Biome.getBiome(height, temperature, humidity).tile());
    }
}
