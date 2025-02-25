package chameleon.client.screen;

import chameleon.client.ChameleonClient;
import chameleon.client.renderer.Brush;
import chameleon.client.screen.component.Component;
import chameleon.client.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class Screen {
    private final List<Component> components = new ArrayList<>();

    protected void addComponent(Component component) {
        components.add(component);
    }

    private void renderBackground(Brush brush) {
        ChameleonClient client = ChameleonClient.getInstance();
        Window window = client.getWindow();
        brush.drawRect(0, 0, window.getWidth(), window.getHeight(), 0xFFFFFF);
    }

    public void render(Brush brush) {
        renderBackground(brush);

        for (Component component : components) component.render(brush);
    }

    public void update() {
        ChameleonClient client = ChameleonClient.getInstance();
        int mouseX = client.getMouseHandler().getMouseX();
        int mouseY = client.getMouseHandler().getMouseY();
        for (Component component : components) {
            if (mouseX >= component.x() && mouseX <= component.x() + component.width() &&
                    mouseY >= component.y() && mouseY <= component.y() + component.height()) {
                if (client.getMouseHandler().isLeftPressed()) component.onPressed();
            }
        }
    }

    public void closeScreen() {
        ChameleonClient client = ChameleonClient.getInstance();
        client.setScreen(null);
    }
}
