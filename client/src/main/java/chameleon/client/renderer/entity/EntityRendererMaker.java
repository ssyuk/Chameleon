package chameleon.client.renderer.entity;

import chameleon.entity.Entity;

public interface EntityRendererMaker {
    EntityRenderer<? extends Entity> make(Entity entity);
}
