package chameleon.client.window;

import chameleon.client.ChameleonClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

public class Window extends JFrame {

    public Window(ChameleonClient client) {
        super("Chameleon");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                ChameleonClient.getInstance().end();
            }
        });

        setLocationRelativeTo(null);

        addKeyListener(client.getKeyHandler());
        addMouseListener(client.getMouseHandler());
        addMouseMotionListener(client.getMouseHandler());
        addMouseWheelListener(client.getMouseHandler());

        setVisible(true);
    }

    public BufferStrategy getBufferStrategy() {
        if (super.getBufferStrategy() == null) {
            createBufferStrategy(3);
        }

        return super.getBufferStrategy();
    }

    @Override
    public int getWidth() {
        Insets insets = getInsets();
        return super.getWidth();
    }

    @Override
    public int getHeight() {
        Insets insets = getInsets();
        return super.getHeight();
    }
}
