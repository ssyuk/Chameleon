package chameleon.client.screen;

import chameleon.client.ChameleonClient;
import chameleon.client.screen.component.Button;
import chameleon.client.window.Window;

public class TitleScreen extends Screen {

    public TitleScreen() {
        ChameleonClient client = ChameleonClient.getInstance();
        Window window = client.getWindow();
        int width = window.getWidth();

        int buttonWidth = width/2;

        addComponent(new Button(width/2-buttonWidth/2, 100, buttonWidth, 60, "Singleplayer", () -> {
            closeScreen();
            client.playSingleplayer();
        }));
        addComponent(new Button(width/2-buttonWidth/2, 200, buttonWidth, 50, "Multiplayer",() -> {
            closeScreen();
            client.playMultiplayer();
        }));
        addComponent(new Button(width/2-buttonWidth/2, 300, buttonWidth, 50, "Options", () -> System.out.println("Options")));
        addComponent(new Button(width/2-buttonWidth/2, 400, buttonWidth, 50, "Quit", client::end));
    }
}
