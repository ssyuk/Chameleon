package chameleon.world.generator;

import chameleon.Chameleon;
import chameleon.net.Connector;
import chameleon.net.packet.Packet06TileInfoRequest;
import chameleon.utils.Location;
import chameleon.world.tile.Tile;

public class NetworkWorldGenerator extends WorldGenerator {
    @Override
    public Tile generateTileAt(Location location) {
        Connector connector = Chameleon.getInstance().getConnector();
        connector.send(new Packet06TileInfoRequest(location));
        return Tile.VOID;
    }

    @Override
    public int generateHeightAt(Location location) {
        Connector connector = Chameleon.getInstance().getConnector();
        connector.send(new Packet06TileInfoRequest(location));
        return 0;
    }
}
