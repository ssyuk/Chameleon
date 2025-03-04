package chameleon.client.renderer.entity;

import chameleon.client.ChameleonClient;
import chameleon.client.assets.entity.EntitySprite;
import chameleon.client.assets.spritesheet.AnimatedSpriteSheet;
import chameleon.client.assets.spritesheet.SingleSpriteSheet;
import chameleon.client.assets.spritesheet.SpriteSheet;
import chameleon.client.renderer.Brush;
import chameleon.client.window.Window;
import chameleon.entity.Entity;
import chameleon.entity.player.Player;
import chameleon.utils.Location;
import chameleon.world.World;

import java.awt.image.BufferedImage;

import static chameleon.client.ChameleonClient.TILE_SIZE;

public class PlayerRenderer extends EntityRenderer {
    private int lastUpdated = 0;
    private int currentFrame = 0;

    @Override
    public void render(Brush brush, World world, double viewX, double viewY, Window window, Entity entity) {
        Player player = (Player) entity;

        ChameleonClient client = ChameleonClient.getInstance();
        EntitySprite sprite = client.getAssetManager().getEntitySprite(player.id());
        SpriteSheet spriteSheet = sprite.getSpriteSheet((player.isMoving() ? "walking" : "idle") + "_" + player.getDirection().id());
        BufferedImage image;

        BufferedImage selectedImage = switch (spriteSheet) {
            case SingleSpriteSheet single -> single.image();
            case AnimatedSpriteSheet animated -> {
                image = animated.images().get(currentFrame);
                if (client.currentUpdateCount() % animated.duration() == 0 && client.currentUpdateCount() != lastUpdated) {
                    currentFrame++;
                    lastUpdated = client.currentUpdateCount();
                    if (currentFrame >= animated.images().size()) {
                        currentFrame = 0;
                    }
                }
                yield image;
            }
            default -> null;
        };

        int halfWindowWidth = window.getWidth() / 2;
        int halfWindowHeight = window.getHeight() / 2;
        double halfWindowWidthTiles = halfWindowWidth / (double) TILE_SIZE;
        double halfWindowHeightTiles = halfWindowHeight / (double) TILE_SIZE;

        Location location = player.getLocation();
        double x = location.x();
        double y = location.y();
        int drawX = (int) Math.round((x - viewX + halfWindowWidthTiles - .5) * TILE_SIZE - (double) TILE_SIZE / 4);
        int drawY = (int) Math.round((y - viewY + halfWindowHeightTiles - .5) * TILE_SIZE - (double) TILE_SIZE / 2);
        brush.drawImage(drawX, drawY, (int) (TILE_SIZE * 1.4), (int) (TILE_SIZE * 1.4), selectedImage);
    }
}
