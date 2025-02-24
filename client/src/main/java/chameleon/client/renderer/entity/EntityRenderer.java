package chameleon.client.renderer.entity;

import chameleon.client.renderer.Brush;
import chameleon.client.window.Window;
import chameleon.entity.Entity;
import chameleon.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class EntityRenderer {
    public static final Map<String, EntityRendererMaker> RENDERERS = new HashMap<>();
    public static final Map<UUID, EntityRenderer> RENDERER_CACHE = new HashMap<>();

    public static void register(String id, EntityRendererMaker renderer) {
        RENDERERS.put(id, renderer);
    }

    public abstract void render(Brush brush, World world, double viewX, double viewY, Window window, Entity entity);
}
