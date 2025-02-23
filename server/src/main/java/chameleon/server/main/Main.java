package chameleon.server.main;

import chameleon.server.ChameleonServer;

public class Main {
    public static void main(String[] args) {
        ChameleonServer server = ChameleonServer.getInstance();
        server.start();
    }
}
