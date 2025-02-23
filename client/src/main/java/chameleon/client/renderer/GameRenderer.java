package chameleon.client.renderer;

import chameleon.client.ChameleonClient;
import chameleon.entity.player.Player;
import chameleon.utils.Location;

public class GameRenderer {
    private final WorldRenderer worldRenderer = new WorldRenderer();

    public void render(Brush brush) {
        ChameleonClient client = ChameleonClient.getInstance();
        Player player = client.getClientPlayer();
        Location view = player.getLocation();
        worldRenderer.render(brush, view.x(), view.y());
        brush.drawString("x: " + String.format("%.3f", player.getLocation().x()), 10, 20, 0x000000);
        brush.drawString("y: " + String.format("%.3f", player.getLocation().y()), 10, 35, 0x000000);
        brush.drawString("target tile: " + client.getMouseHandler().getTargetTile(), 10, 80, 0x000000);
        brush.drawString("target entity: " + client.getMouseHandler().getTargetEntity(), 10, 95, 0x000000);
    }

    public WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }
}
