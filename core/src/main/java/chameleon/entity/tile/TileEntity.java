package chameleon.entity.tile;

import chameleon.entity.Entity;
import chameleon.utils.Direction;
import chameleon.utils.Location;
import chameleon.utils.Vec2d;

import java.util.UUID;

public abstract class TileEntity extends Entity {
    public TileEntity(UUID uuid, Location location) {
        super(uuid, location);
    }
    public TileEntity(Location location) {
        super(location);
    }

    @Override
    public Direction getDirection() {
        return Direction.DOWN;
    }

    @Override
    public boolean move(Direction direction, double speedMultiplier) {
        throw new UnsupportedOperationException("Tile entities cannot move");
    }

    @Override
    public boolean move(Vec2d displacement, boolean force) {
        throw new UnsupportedOperationException("Tile entities cannot move");
    }
}
