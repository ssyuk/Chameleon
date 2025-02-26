package chameleon.world.biome;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Biome {
    private double idealHeight;      // 이상적인 높이
    private double idealTemperature; // 이상적인 온도
    private double idealHumidity;    // 이상적인 습도

    public Biome(double idealHeight, double idealTemperature, double idealHumidity) {
        this.idealHeight = idealHeight;
        this.idealTemperature = idealTemperature;
        this.idealHumidity = idealHumidity;
    }

    public static void initialize() {
        Biome.registerBiome(new PlainsBiome());
        Biome.registerBiome(new DesertBiome());
        Biome.registerBiome(new OceanBiome());
    }

    public double getIdealHeight() {
        return idealHeight;
    }

    public double getIdealTemperature() {
        return idealTemperature;
    }

    public double getIdealHumidity() {
        return idealHumidity;
    }

    public abstract Color tile();

    // 바이옴을 저장할 리스트
    private static List<Biome> registeredBiomes = new ArrayList<>();

    // 바이옴 등록 메서드
    public static void registerBiome(Biome biome) {
        registeredBiomes.add(biome);
    }

    // 가장 가까운 바이옴 찾기
    public static Biome getBiome(double height, double temperature, double humidity) {
        Biome closestBiome = null;
        double minDistance = Double.MAX_VALUE;

        for (Biome biome : registeredBiomes) {
            double distance = calculateDistance(height, temperature, humidity,
                    biome.getIdealHeight(), biome.getIdealTemperature(), biome.getIdealHumidity());
            if (distance < minDistance) {
                minDistance = distance;
                closestBiome = biome;
            }
        }
        return closestBiome;
    }

    // 유클리드 거리 계산
    private static double calculateDistance(double h1, double t1, double u1, double h2, double t2, double u2) {
        double weightHeight = 2.0; // 높이에 더 큰 가중치
        double weightTemp = 1.0;
        double weightHumidity = 1.0;
        return Math.sqrt(weightHeight * (h1 - h2) * (h1 - h2) +
                weightTemp * (t1 - t2) * (t1 - t2) +
                weightHumidity * (u1 - u2) * (u1 - u2));
    }
}