package chameleon.client.main;

import chameleon.client.ChameleonClient;

public class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "True");

        ChameleonClient client = ChameleonClient.getInstance();
        client.start();
    }
}
