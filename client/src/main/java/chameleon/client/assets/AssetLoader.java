package chameleon.client.assets;

import chameleon.client.assets.spritesheet.AnimatedSpriteSheet;
import chameleon.client.assets.spritesheet.DirectionalSpriteSheet;
import chameleon.client.assets.spritesheet.SingleSpriteSheet;
import chameleon.client.assets.spritesheet.SpriteSheet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetLoader {
    public Map<String, SpriteSheet> loadModel(String path) {
        JsonObject config;
        try {
            config = JsonParser.parseReader(new InputStreamReader(getResourceAsStream("assets/" + path + "/config.json"))).getAsJsonObject();
        } catch (Exception e) {
            throw new RuntimeException("Could not load config file for asset directory: " + path);
        }

        try {
            return readSprite("assets/" + path, config);
        } catch (IOException e) {
            throw new RuntimeException("Could not load sprites for asset directory: " + path);
        }
    }

    private Map<String, SpriteSheet> readSprite(String path, JsonObject object) throws IOException {
        try {
            String mode = object.get("mode").getAsString();
            return switch (mode) {
                case "single_sprite" -> readSingleSprite(path, object);
                case "multiple_sprite" -> readMultipleSprite(path, object);
                case "animated" -> readAnimatedSprite(path, object);
                case "multiple_model" -> readMultipleModel(path, object);
                case "directional" -> readDirectional(path, object);
                default -> throw new RuntimeException("Invalid mode: " + mode);
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, SpriteSheet> readSingleSprite(String path, JsonObject object) throws IOException {
        Map<String, SpriteSheet> result = new HashMap<>();
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : object.entrySet()) {
            String id = stringJsonElementEntry.getKey();
            if (id.equalsIgnoreCase("mode")) continue;

            String subpath = stringJsonElementEntry.getValue().getAsString();

            // load image
            BufferedImage image = ImageIO.read(getResource(path + "/" + subpath));
            result.put(id, new SingleSpriteSheet(image));
        }
        return result;
    }

    private Map<String, SpriteSheet> readMultipleSprite(String path, JsonObject object) throws IOException {
        Map<String, SpriteSheet> result = new HashMap<>();

        JsonObject sprites = object.getAsJsonObject("sprites");
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : sprites.entrySet()) {
            String id = stringJsonElementEntry.getKey();

            String subpath = stringJsonElementEntry.getValue().getAsString();

            // load image
            BufferedImage image = ImageIO.read(getResource(path + "/" + subpath));
            result.put(id, new SingleSpriteSheet(image));
        }
        return result;
    }

    private Map<String, SpriteSheet> readAnimatedSprite(String path, JsonObject object) throws IOException {
        Map<String, SpriteSheet> result = new HashMap<>();
        int frame_count = object.get("frame_count").getAsInt();
        int size = object.get("size").getAsInt();
        int duration = object.get("animated_duration").getAsInt();
        JsonObject spritesheet = object.getAsJsonObject("spritesheet");
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : spritesheet.entrySet()) {
            String id = stringJsonElementEntry.getKey();
            if (id.equalsIgnoreCase("mode")) continue;

            String subpath = stringJsonElementEntry.getValue().getAsString();

            // load image
            BufferedImage image = ImageIO.read(getResource(path + "/" + subpath));

            List<BufferedImage> sprites = new ArrayList<>();
            for (int i = 0; i < frame_count; i++) {
                BufferedImage sprite = image.getSubimage(0, i * size, size, size);
                sprites.add(sprite);
            }
            result.put(id, new AnimatedSpriteSheet(sprites, duration));
        }
        return result;
    }

    private Map<String, SpriteSheet> readMultipleModel(String path, JsonObject object) throws IOException {
        Map<String, SpriteSheet> result = new HashMap<>();

        for (Map.Entry<String, JsonElement> stringJsonElementEntry : object.entrySet()) {
            String key = stringJsonElementEntry.getKey();
            if (key.equalsIgnoreCase("mode")) continue;

            JsonObject subobject = stringJsonElementEntry.getValue().getAsJsonObject();
            Map<String, SpriteSheet> sprites = readSprite(path, subobject);
            sprites.forEach((s, bufferedImage) -> result.put(key + "_" + s, bufferedImage));
        }
        return result;
    }

    private Map<String, SpriteSheet> readDirectional(String path, JsonObject object) throws IOException {
        Map<String, SpriteSheet> result = new HashMap<>();
        String directional_method = object.get("directional_method").getAsString();

        BufferedImage left = ImageIO.read(getResource(path + "/" + object.get("left").getAsString())),
                right = ImageIO.read(getResource(path + "/" + object.get("right").getAsString())),
                up = ImageIO.read(getResource(path + "/" + object.get("up").getAsString())),
                down = ImageIO.read(getResource(path + "/" + object.get("down").getAsString()));

        result.put("sprite", new DirectionalSpriteSheet(left, right, up, down, DirectionalSpriteSheet.DirectionalMethod.valueOf(directional_method.toUpperCase())));
        return result;
    }

    private @NotNull URL getResource(String path) {
        URL url = getClass().getClassLoader().getResource(path);
        if (url == null) throw new RuntimeException("Could not find asset directory: " + path);
        return url;
    }

    private @NotNull InputStream getResourceAsStream(String path) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        if (stream == null) throw new RuntimeException("Could not find asset directory: " + path);
        return stream;
    }
}
