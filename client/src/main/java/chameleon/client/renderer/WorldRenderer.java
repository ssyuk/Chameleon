package chameleon.client.renderer;

import chameleon.client.ChameleonClient;
import chameleon.client.assets.SpriteSheet;
import chameleon.client.assets.tile.CliffSprite;
import chameleon.entity.Entity;
import chameleon.client.renderer.entity.EntityRenderer;
import chameleon.client.window.Window;
import chameleon.utils.Location;
import chameleon.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
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

                Color color = world.getTileAt(new Location(world, tx, ty)).getColor();
                brush.drawRect(drawX, drawY, TILE_SIZE, TILE_SIZE, color.getRGB());

                // 절벽 렌더링 추가
                int currentHeight = world.getHeightAt(new Location(world, tx, ty));
                checkNeighborsAndRenderCliff(brush, world, tx, ty, currentHeight, drawX, drawY);
            }
        }
    }

    private void checkNeighborsAndRenderCliff(Brush brush, World world, int tx, int ty, int currentHeight, int drawX, int drawY) {
        boolean[] higher = new boolean[8];
        int[][] dirs = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};
        for (int i = 0; i < dirs.length; i++) {
            int nx = tx + dirs[i][0];
            int ny = ty + dirs[i][1];
            Location neighborLoc = new Location(world, nx, ny);
            int neighborHeight = world.getHeightAt(neighborLoc);
            higher[i] = (neighborHeight == currentHeight + 1);
        }

        boolean top = higher[0];
        boolean topright = higher[1];
        boolean right = higher[2];
        boolean bottomright = higher[3];
        boolean bottom = higher[4];
        boolean bottomleft = higher[5];
        boolean left = higher[6];
        boolean topleft = higher[7];

        // 모서리 처리
        if (top && right) {
            renderCliff(brush, drawX, drawY, topright ? "inside_bottom_left" : "bottom_left");
        }
        if (top && left) {
            renderCliff(brush, drawX, drawY, topleft ? "inside_bottom_right" : "bottom_right");
        }
        if (bottom && right) {
            renderCliff(brush, drawX, drawY, bottomright ? "inside_top_left" : "top_left");
        }
        if (bottom && left) {
            renderCliff(brush, drawX, drawY, bottomleft ? "inside_top_right" : "top_right");
        }

        // 직선 방향 처리
        if (top && !right && !left) renderCliff(brush, drawX, drawY, "bottom_center");
        if (right && !top && !bottom) renderCliff(brush, drawX, drawY, "middle_left");
        if (bottom && !right && !left) renderCliff(brush, drawX, drawY, "top_center");
        if (left && !top && !bottom) renderCliff(brush, drawX, drawY, "middle_right");
    }

    private void renderCliff(Brush brush, int x, int y, String mode) {
        SpriteSheet sheet = CliffSprite.getSpriteSheet(mode);
        if (sheet != null && !sheet.sprites().isEmpty()) {
            BufferedImage img = sheet.sprites().getFirst();
            brush.drawImage(x, y, TILE_SIZE, TILE_SIZE, img);
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