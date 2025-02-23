package chameleon.client.renderer;

import chameleon.client.ChameleonClient;
import chameleon.client.window.Window;

import java.awt.*;

public class MasterRenderer {
    private final GameRenderer gameRenderer = new GameRenderer();

    public void render() {
        Window window = ChameleonClient.getInstance().getWindow();
        Graphics graphics = window.getBufferStrategy().getDrawGraphics();
        Brush brush = new Brush(window.getInsets(), graphics);

        brush.drawRect(0, 0, window.getWidth(), window.getHeight(), 0xFFFFFF);

        gameRenderer.render(brush);

        graphics.dispose();
        window.getBufferStrategy().show();
    }

    public GameRenderer getGameRenderer() {
        return gameRenderer;
    }
}
