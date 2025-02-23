package chameleon.client.renderer.entity;

import chameleon.entity.Entity;
import chameleon.client.renderer.Brush;
import chameleon.client.window.Window;
import chameleon.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class EntityRenderer<E extends Entity> {
    protected final E entity;
    public static final Map<String, EntityRendererMaker> RENDERERS = new HashMap<>();
    public static final Map<UUID, EntityRenderer<? extends Entity>> RENDERER_CACHE = new HashMap<>();

    public static void register(String id, EntityRendererMaker renderer) {
        RENDERERS.put(id, renderer);
    }

    protected EntityRenderer(E entity) {
        this.entity = entity;
    }

    public abstract void render(Brush brush, World world, double viewX, double viewY, Window window);
}
