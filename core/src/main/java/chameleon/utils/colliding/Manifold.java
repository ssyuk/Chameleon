package chameleon.utils.colliding;

import chameleon.utils.Vec2d;

public record Manifold(int contactCount, Vec2d[] contactPoints, Vec2d normal, double penetration) {
}
