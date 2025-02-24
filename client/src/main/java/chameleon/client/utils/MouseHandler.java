package chameleon.client.utils;

import chameleon.client.ChameleonClient;
import chameleon.entity.Entity;
import chameleon.entity.player.Player;
import chameleon.client.renderer.WorldRenderer;
import chameleon.utils.Location;
import chameleon.utils.colliding.AABB;
import chameleon.utils.colliding.CollisionDetection;
import chameleon.client.window.Window;
import chameleon.world.World;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static chameleon.client.ChameleonClient.TILE_SIZE;

public class MouseHandler extends MouseAdapter {
    private int mouseX = 0, mouseY = 0;
    private boolean leftPressed = false, rightPressed = false;

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftPressed = true;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightPressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftPressed = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightPressed = false;
        }
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public Location getTargetTile() {
        ChameleonClient client = ChameleonClient.getInstance();
        Window window = client.getWindow();
        Player player = client.getClientPlayer();
        double viewX = player.getLocation().x(), viewY = player.getLocation().y(); // center of the screen

        int halfWindowWidth = window.getWidth() / 2;
        int halfWindowHeight = window.getHeight() / 2;
        double halfWindowWidthTiles = halfWindowWidth / (double) TILE_SIZE;
        double halfWindowHeightTiles = halfWindowHeight / (double) TILE_SIZE;

        double targetX = (double) mouseX / TILE_SIZE + viewX - halfWindowWidthTiles;
        double targetY = (double) mouseY / TILE_SIZE + viewY - halfWindowHeightTiles - .5;
        return new Location(client.getWorld(), targetX, targetY).toTileLocation();
    }

    public @Nullable Entity getTargetEntity() {
        ChameleonClient client = ChameleonClient.getInstance();
        World world = client.getWorld();
        Window window = client.getWindow();
        WorldRenderer worldRenderer = client.getRenderer().getGameRenderer().getWorldRenderer();
        Player player = client.getClientPlayer();
        double viewX = player.getLocation().x(), viewY = player.getLocation().y(); // center of the screen

        int halfWindowWidth = window.getWidth() / 2;
        int halfWindowHeight = window.getHeight() / 2;
        double halfWindowWidthTiles = halfWindowWidth / (double) TILE_SIZE;
        double halfWindowHeightTiles = halfWindowHeight / (double) TILE_SIZE;

        double targetX = (double) mouseX / TILE_SIZE + viewX - halfWindowWidthTiles;
        double targetY = (double) mouseY / TILE_SIZE + viewY - halfWindowHeightTiles - .5;
        Location location = new Location(client.getWorld(), targetX, targetY);
        AABB mouseBoundingBox = AABB.fromCenterAndSize(location, .1);

        for (Entity entity : worldRenderer.getVisibleEntities()) {
            if (CollisionDetection.isColliding(mouseBoundingBox, entity.getInteractiveBoundingBox())) {
                return entity;
            }
        }
        return null;
    }
}
