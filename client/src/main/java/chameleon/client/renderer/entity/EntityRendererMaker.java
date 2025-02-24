package chameleon.client.renderer.entity;

import chameleon.entity.Entity;

public interface EntityRendererMaker {
    EntityRenderer make(Entity entity);
}
