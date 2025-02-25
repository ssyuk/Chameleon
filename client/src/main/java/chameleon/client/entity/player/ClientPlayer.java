package chameleon.client.entity.player;

import chameleon.client.ChameleonClient;
import chameleon.client.utils.KeyHandler;
import chameleon.entity.player.Player;
import chameleon.entity.tile.Stairs;
import chameleon.net.packet.Packet04EntityMoveRequest;
import chameleon.utils.Direction;
import chameleon.utils.Location;
import chameleon.utils.Vec2d;

import java.awt.event.KeyEvent;
import java.util.UUID;

public class ClientPlayer extends Player {
    private boolean lastMoving = false;

    public ClientPlayer(String name, Location location) {
        super(name, UUID.randomUUID(), location);
    }

    @Override
    public void update() {
        ChameleonClient client = ChameleonClient.getInstance();
        KeyHandler keyHandler = client.getKeyHandler();
        lastMoving = moving;
        moving = false;
        Vec2d displacement = new Vec2d(0, 0);
        if (keyHandler.isKeyDown(KeyEvent.VK_W)) {
            moving = true;
            if (move(Direction.UP, SPEED))
                displacement = displacement.add(Direction.UP.dx() * SPEED, Direction.UP.dy() * SPEED);
        }
        if (keyHandler.isKeyDown(KeyEvent.VK_S)) {
            moving = true;
            if (move(Direction.DOWN, SPEED))
                displacement = displacement.add(Direction.DOWN.dx() * SPEED, Direction.DOWN.dy() * SPEED);
        }
        if (keyHandler.isKeyDown(KeyEvent.VK_A)) {
            moving = true;
            if (move(Direction.LEFT, SPEED))
                displacement = displacement.add(Direction.LEFT.dx() * SPEED, Direction.LEFT.dy() * SPEED);
        }
        if (keyHandler.isKeyDown(KeyEvent.VK_D)) {
            moving = true;
            if (move(Direction.RIGHT, SPEED))
                displacement = displacement.add(Direction.RIGHT.dx() * SPEED, Direction.RIGHT.dy() * SPEED);
        }

        if (client.isOnline()) {
            if (moving) {
                client.getConnector().send(new Packet04EntityMoveRequest(uuid(), displacement, moving));
            }
            if (!moving && lastMoving) {
                client.getConnector().send(new Packet04EntityMoveRequest(uuid(), new Vec2d(0, 0), false)); // stop moving
            }
        }

        if (client.getMouseHandler().isLeftPressed()) {
            client.getMouseHandler().setLeftPressed(false);
            location.world().addEntity(new Stairs(client.getMouseHandler().getTargetTile()));
        }
    }
}
