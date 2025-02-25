package chameleon.net;

import chameleon.net.packet.Packet;

public abstract class Connector extends Thread {
    public Connector() {
        super("Connector");
    }

    public abstract void send(Packet packet);
}
