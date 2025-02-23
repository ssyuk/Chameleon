package chameleon.client.renderer.entity;

import chameleon.client.ChameleonClient;
import chameleon.client.assets.SpriteSheet;
import chameleon.client.assets.entity.EntitySprite;
import chameleon.entity.tile.TileEntity;
import chameleon.client.renderer.Brush;
import chameleon.utils.Location;
import chameleon.client.window.Window;
import chameleon.world.World;

import java.awt.image.BufferedImage;
import java.util.Random;

import static chameleon.client.ChameleonClient.TILE_SIZE;

public class TileEntityRenderer extends EntityRenderer<TileEntity> {
    public TileEntityRenderer(TileEntity entity) {
        super(entity);
    }

    @Override
    public void render(Brush brush, World world, double viewX, double viewY, Window window) {
        ChameleonClient client = ChameleonClient.getInstance();
        EntitySprite sprite = client.getAssetManager().getEntitySprite(entity.id());
        SpriteSheet spriteSheet = sprite.getSpriteSheet(entity);
        BufferedImage image;
        if (spriteSheet.selection() == SpriteSheet.SpriteSelection.RANDOM_BY_ENTITY)
            image = spriteSheet.sprites().get(new Random(entity.uuid().hashCode()).nextInt(spriteSheet.sprites().size()));
        else image = spriteSheet.sprites().getFirst();

        int halfWindowWidth = window.getWidth() / 2;
        int halfWindowHeight = window.getHeight() / 2;
        double halfWindowWidthTiles = halfWindowWidth / (double) TILE_SIZE;
        double halfWindowHeightTiles = halfWindowHeight / (double) TILE_SIZE;

        Location location = entity.getLocation();
        double x = location.x();
        double y = location.y();
        int drawX = (int) ((x - viewX + halfWindowWidthTiles - .5) * TILE_SIZE);
        int drawY = (int) ((y - viewY + halfWindowHeightTiles - .5) * TILE_SIZE);
        brush.drawImage(drawX, drawY, TILE_SIZE, TILE_SIZE, image);
    }
}
