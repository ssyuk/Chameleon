package chameleon.client.renderer.entity;

import chameleon.client.ChameleonClient;
import chameleon.client.assets.entity.EntitySprite;
import chameleon.client.assets.spritesheet.DirectionalSpriteSheet;
import chameleon.client.assets.spritesheet.SingleSpriteSheet;
import chameleon.client.assets.spritesheet.SpriteSheet;
import chameleon.client.renderer.Brush;
import chameleon.entity.Entity;
import chameleon.utils.Location;
import chameleon.world.World;

import java.awt.image.BufferedImage;

import static chameleon.client.ChameleonClient.TILE_SIZE;

public class TileEntityRenderer extends EntityRenderer {
    @Override
    public void render(Brush brush, World world, double viewX, double viewY, int width, int height, Entity entity) {
        ChameleonClient client = ChameleonClient.getInstance();
        EntitySprite sprite = client.getAssetManager().getEntitySprite(entity.id());
        SpriteSheet spriteSheet = sprite.getSpriteSheet("sprite");

        BufferedImage image = switch (spriteSheet) {
            case SingleSpriteSheet single -> single.image();
            case DirectionalSpriteSheet directional -> directional.image(world, entity.getLocation());
            default -> null;
        };

        int halfWindowWidth = width / 2;
        int halfWindowHeight = height / 2;
        double halfWindowWidthTiles = halfWindowWidth / (double) TILE_SIZE;
        double halfWindowHeightTiles = halfWindowHeight / (double) TILE_SIZE;

        Location location = entity.getLocation();
        double x = location.x();
        double y = location.y();
        int drawX = (int) Math.round((x - viewX + halfWindowWidthTiles - .5) * TILE_SIZE);
        int drawY = (int) Math.round((y - viewY + halfWindowHeightTiles - .5) * TILE_SIZE);
        brush.drawImage(drawX, drawY, TILE_SIZE, TILE_SIZE, image);
    }
}
