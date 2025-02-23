package chameleon.client.renderer;

import chameleon.client.ChameleonClient;
import chameleon.entity.Entity;
import chameleon.client.renderer.entity.EntityRenderer;
import chameleon.utils.TileLocation;
import chameleon.client.window.Window;
import chameleon.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static chameleon.client.ChameleonClient.TILE_SIZE;

public class WorldRenderer {
    public List<Entity> visibleEntities = new ArrayList<>();

    public void render(Brush brush, double viewX, double viewY) {
        ChameleonClient client = ChameleonClient.getInstance();
        World world = client.getWorld();
        Window window = client.getWindow();
        renderTile(brush, world, viewX, viewY, window);
        renderEntities(brush, world, viewX, viewY, window);
    }

    private void renderTile(Brush brush, World world, double viewX, double viewY, Window window) {
        int halfWindowWidth = window.getWidth() / 2;
        int halfWindowHeight = window.getHeight() / 2;
        double halfWindowWidthTiles = (double) halfWindowWidth / TILE_SIZE;
        double halfWindowHeightTiles = (double) halfWindowHeight / TILE_SIZE;

        int startX = (int) (viewX - halfWindowWidthTiles) - 1;
        int endX = (int) (viewX + halfWindowWidthTiles) + 1;
        int startY = (int) (viewY - halfWindowHeightTiles) - 1;
        int endY = (int) (viewY + halfWindowHeightTiles) + 1;

        for (int tx = startX; tx < endX; tx++) {
            for (int ty = startY; ty < endY; ty++) {
                int drawX = (int) ((tx - viewX + halfWindowWidthTiles) * TILE_SIZE);
                int drawY = (int) ((ty - viewY + halfWindowHeightTiles) * TILE_SIZE);

                Color color = world.getTileAt(new TileLocation(world, tx, ty)).getColor();
                for (int i = 0; i < world.getHeightAt(new TileLocation(world, tx, ty)); i++) {
                    color = color.darker();
                }
                brush.drawRect(drawX, drawY, TILE_SIZE, TILE_SIZE, color.getRGB());
            }
        }
    }

    private void renderEntities(Brush brush, World world, double viewX, double viewY, Window window) {
        int halfWindowWidth = window.getWidth() / 2;
        int halfWindowHeight = window.getHeight() / 2;
        double halfWindowWidthTiles = halfWindowWidth / (double) TILE_SIZE;
        double halfWindowHeightTiles = halfWindowHeight / (double) TILE_SIZE;

        visibleEntities = world.getEntities().stream()
                .filter(entity -> {
                    int drawX = (int) ((entity.getLocation().x() - viewX + halfWindowWidthTiles - .5) * TILE_SIZE);
                    int drawY = (int) ((entity.getLocation().y() - viewY + halfWindowHeightTiles - .5) * TILE_SIZE);
                    return drawX >= -TILE_SIZE && drawX <= window.getWidth() && drawY >= -TILE_SIZE && drawY <= window.getHeight();
                })
                .sorted(Comparator.comparingDouble(entity -> entity.getLocation().y())).toList();

        for (Entity entity : visibleEntities) {
            // if entity is not within view, continue (entity location, view x/y is in tile coordinates)
            if (entity.getLocation().x() < viewX - (double) window.getWidth() / TILE_SIZE || entity.getLocation().x() > viewX + (double) window.getWidth() / TILE_SIZE ||
                    entity.getLocation().y() < viewY - (double) window.getHeight() / TILE_SIZE || entity.getLocation().y() > viewY + (double) window.getHeight() / TILE_SIZE) {
                continue;
            }

            EntityRenderer.RENDERER_CACHE.putIfAbsent(entity.uuid(), EntityRenderer.RENDERERS.get(entity.id()).make(entity));
            EntityRenderer<? extends Entity> renderer = EntityRenderer.RENDERER_CACHE.get(entity.uuid());
            renderer.render(brush, world, viewX, viewY, window);
        }

        brush.drawString("entities: " + world.getEntities().size(), 10, 50, 0x000000);
        brush.drawString("visible(rendered) entities: " + visibleEntities.size(), 10, 65, 0x000000);
    }

    public List<Entity> getVisibleEntities() {
        return visibleEntities;
    }
}