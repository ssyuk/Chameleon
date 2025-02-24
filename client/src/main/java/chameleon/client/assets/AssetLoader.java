package chameleon.client.assets;

import chameleon.client.assets.spritesheet.AnimatedSpriteSheet;
import chameleon.client.assets.spritesheet.DirectionalSpriteSheet;
import chameleon.client.assets.spritesheet.SingleSpriteSheet;
import chameleon.client.assets.spritesheet.SpriteSheet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetLoader {
    public Map<String, SpriteSheet> loadModel(String path) {
        URL url = getClass().getClassLoader().getResource("assets/" + path);
        if (url == null) throw new RuntimeException("Could not find asset directory: " + path);
        Path spriteDir = Paths.get(url.getPath());
        JsonObject config;
        try {
            config = JsonParser.parseReader(Files.newBufferedReader(spriteDir.resolve("config.json"))).getAsJsonObject();
        } catch (Exception e) {
            throw new RuntimeException("Could not load config file for asset directory: " + path);
        }

        try {
            return readSprite(spriteDir, config);
        } catch (IOException e) {
            throw new RuntimeException("Could not load sprites for asset directory: " + path);
        }
    }

    private Map<String, SpriteSheet> readSprite(Path spriteDir, JsonObject object) throws IOException {
        try {
            String mode = object.get("mode").getAsString();
            return switch (mode) {
                case "single_sprite" -> readSingleSprite(spriteDir, object);
                case "multiple_sprite" -> readMultipleSprite(spriteDir, object);
                case "animated" -> readAnimatedSprite(spriteDir, object);
                case "multiple_model" -> readMultipleModel(spriteDir, object);
                case "directional" -> readDirectional(spriteDir, object);
                default -> throw new RuntimeException("Invalid mode: " + mode);
            };
        } catch (Exception e) {
            System.out.println(spriteDir);
            throw new RuntimeException(e);
        }
    }

    private Map<String, SpriteSheet> readSingleSprite(Path spriteDir, JsonObject object) throws IOException {
        Map<String, SpriteSheet> result = new HashMap<>();
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : object.entrySet()) {
            String id = stringJsonElementEntry.getKey();
            if (id.equalsIgnoreCase("mode")) continue;

            String path = stringJsonElementEntry.getValue().getAsString();

            // load image
            BufferedImage image = ImageIO.read(spriteDir.resolve(path).toFile());
            result.put(id, new SingleSpriteSheet(image));
        }
        return result;
    }

    private Map<String, SpriteSheet> readMultipleSprite(Path spriteDir, JsonObject object) throws IOException {
        Map<String, SpriteSheet> result = new HashMap<>();

        JsonObject sprites = object.getAsJsonObject("sprites");
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : sprites.entrySet()) {
            String id = stringJsonElementEntry.getKey();

            String path = stringJsonElementEntry.getValue().getAsString();

            // load image
            BufferedImage image = ImageIO.read(spriteDir.resolve(path).toFile());

            result.put(id, new SingleSpriteSheet(image));
        }
        return result;
    }

    private Map<String, SpriteSheet> readAnimatedSprite(Path spriteDir, JsonObject object) throws IOException {
        Map<String, SpriteSheet> result = new HashMap<>();
        int frame_count = object.get("frame_count").getAsInt();
        int size = object.get("size").getAsInt();
        int duration = object.get("animated_duration").getAsInt();
        JsonObject spritesheet = object.getAsJsonObject("spritesheet");
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : spritesheet.entrySet()) {
            String id = stringJsonElementEntry.getKey();
            if (id.equalsIgnoreCase("mode")) continue;

            String path = stringJsonElementEntry.getValue().getAsString();

            // load image
            BufferedImage image = ImageIO.read(spriteDir.resolve(path).toFile());

            List<BufferedImage> sprites = new ArrayList<>();
            for (int i = 0; i < frame_count; i++) {
                BufferedImage sprite = image.getSubimage(0, i * size, size, size);
                sprites.add(sprite);
            }
            result.put(id, new AnimatedSpriteSheet(sprites, duration));
        }
        return result;
    }

    private Map<String, SpriteSheet> readMultipleModel(Path spriteDir, JsonObject object) throws IOException {
        Map<String, SpriteSheet> result = new HashMap<>();

        for (Map.Entry<String, JsonElement> stringJsonElementEntry : object.entrySet()) {
            String key = stringJsonElementEntry.getKey();
            if (key.equalsIgnoreCase("mode")) continue;

            JsonObject subobject = stringJsonElementEntry.getValue().getAsJsonObject();
            Map<String, SpriteSheet> sprites = readSprite(spriteDir, subobject);
            sprites.forEach((s, bufferedImage) -> result.put(key + "_" + s, bufferedImage));
        }
        return result;
    }

    private Map<String, SpriteSheet> readDirectional(Path spriteDir, JsonObject object) throws IOException {
        Map<String, SpriteSheet> result = new HashMap<>();
        String directional_method = object.get("directional_method").getAsString();

        BufferedImage left = ImageIO.read(spriteDir.resolve(object.get("left").getAsString()).toFile()),
                right = ImageIO.read(spriteDir.resolve(object.get("right").getAsString()).toFile()),
                up = ImageIO.read(spriteDir.resolve(object.get("up").getAsString()).toFile()),
                down = ImageIO.read(spriteDir.resolve(object.get("down").getAsString()).toFile());

        result.put("sprite", new DirectionalSpriteSheet(left, right, up, down, DirectionalSpriteSheet.DirectionalMethod.valueOf(directional_method.toUpperCase())));
        return result;
    }
}
