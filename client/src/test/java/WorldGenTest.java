import chameleon.client.ChameleonClient;
import chameleon.client.renderer.Brush;
import chameleon.client.renderer.WorldRenderer;
import chameleon.world.World;
import chameleon.world.biome.Biome;
import chameleon.world.generator.NoiseWorldGenerator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.Random;

public class WorldGenTest {
    public static final int WIDTH = 1920*3, HEIGHT = 1080*3;
    public static void main(String[] args) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        Brush brush = new Brush(new Insets(0, 0, 0, 0), graphics);
        ChameleonClient client = ChameleonClient.getInstance();
        Biome.initialize();

        client.setWorld(new World(new NoiseWorldGenerator(new Random().nextLong())));
        WorldRenderer renderer = new WorldRenderer();
        System.out.println("Rendering world...");
        renderer.render(brush, 0, 0, WIDTH, HEIGHT);
        graphics.dispose();

        // Save the image to a file
        try {
            ImageIO.write(image, "png", new java.io.File("world.png"));
            System.out.println("Image saved to world.png");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
