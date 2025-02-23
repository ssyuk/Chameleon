package chameleon.client.assets;

import com.google.gson.JsonArray;
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
        } catch (IOException e) {
            throw new RuntimeException("Could not load config file for asset directory: " + path);
        }

        Map<String, SpriteSheet> result = new HashMap<>();

        for (Map.Entry<String, JsonElement> stringJsonElementEntry : config.entrySet()) {
            String key = stringJsonElementEntry.getKey();
            JsonObject object = stringJsonElementEntry.getValue().getAsJsonObject();
            try {
                Map<String, SpriteSheet> sprites = readSprite(spriteDir, object);
                sprites.forEach((s, bufferedImage) -> result.put(key + "_" + s, bufferedImage));
            } catch (IOException e) {
                throw new RuntimeException("Could not load '" + key + "' sprites for asset directory: " + path);
            }
        }

        return result;
    }

    private Map<String, SpriteSheet> readSprite(Path spriteDir, JsonObject object) throws IOException {
        String mode = object.get("mode").getAsString();
        return switch (mode) {
            case "single_sprite" -> readSingleSprite(spriteDir, object);
            case "animated" -> readAnimatedSprite(spriteDir, object);
            case "multiple_texture" -> readMultipleTexture(spriteDir, object);
            default -> throw new RuntimeException("Invalid mode: " + mode);
        };
    }

    private Map<String, SpriteSheet> readSingleSprite(Path spriteDir, JsonObject object) throws IOException {
        Map<String, SpriteSheet> result = new HashMap<>();
        JsonObject sprites = object.getAsJsonObject("sprites");
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : sprites.entrySet()) {
            String id = stringJsonElementEntry.getKey();
            String path = stringJsonElementEntry.getValue().getAsString();

            // load image
            BufferedImage image = ImageIO.read(spriteDir.resolve(path).toFile());
            result.put(id, SpriteSheet.singleSprite(image));
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
            String path = stringJsonElementEntry.getValue().getAsString();

            // load image
            BufferedImage image = ImageIO.read(spriteDir.resolve(path).toFile());

            List<BufferedImage> sprites = new ArrayList<>();
            for (int i = 0; i < frame_count; i++) {
                BufferedImage sprite = image.getSubimage(0, i * size, size, size);
                sprites.add(sprite);
            }
            result.put(id, SpriteSheet.animatedSprite(sprites, duration));
        }
        return result;
    }

    private Map<String, SpriteSheet> readMultipleTexture(Path spriteDir, JsonObject object) throws IOException {
        Map<String, SpriteSheet> result = new HashMap<>();
        JsonObject spritesJson = object.getAsJsonObject("sprites");
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : spritesJson.entrySet()) {
            String id = stringJsonElementEntry.getKey();
            JsonArray paths = stringJsonElementEntry.getValue().getAsJsonArray();

            List<BufferedImage> sprites = new ArrayList<>();
            for (JsonElement jsonElement : paths) {
                String path = jsonElement.getAsString();
                BufferedImage image = ImageIO.read(spriteDir.resolve(path).toFile());
                sprites.add(image);
            }
            result.put(id, new SpriteSheet(sprites, -1, SpriteSheet.SpriteSelection.RANDOM_BY_ENTITY));
        }
        return result;
    }
}
