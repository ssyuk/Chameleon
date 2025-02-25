package chameleon.client.screen.component;

import chameleon.client.renderer.Brush;

public class Button extends Component {
    private final String text;
    private final Runnable onPressed;

    public Button(int x, int y, int width, int height, String text, Runnable onPressed) {
        super(x, y, width, height);
        this.text = text;
        this.onPressed = onPressed;
    }

    @Override
    public void render(Brush brush) {
        brush.drawRect(x(), y(), width(), height(), 0x000000);
        brush.drawCenteredString(x() + width() / 2, y() + height() / 2, text, 0xffffff);
    }

    @Override
    public void onPressed() {
        onPressed.run();
    }
}
