package chameleon.utils.colliding;

import chameleon.utils.Vec2d;

public class CollisionDetection {
    public static boolean isColliding(AABB a, AABB b) {
        Vec2d centerA = a.min().add(a.max()).divide(2);
        Vec2d centerB = b.min().add(b.max()).divide(2);
        Vec2d n = centerB.subtract(centerA);

        Vec2d extentA = a.max().subtract(a.min()).divide(2);
        Vec2d extentB = b.max().subtract(b.min()).divide(2);

        double xOverlap = extentA.x() + extentB.x() - Math.abs(n.x());
        double yOverlap = extentA.y() + extentB.y() - Math.abs(n.y());

        if (xOverlap < 0 || yOverlap < 0) return false;

        int contactCount;
        Vec2d[] contactPoints = new Vec2d[2];
        Vec2d normal;
        double penetration;
        if (xOverlap < yOverlap) {
            normal = (n.x() < 0) ? new Vec2d(-1, 0) : new Vec2d(1, 0);
            penetration = xOverlap;
            contactCount = 2;
            contactPoints[0] = new Vec2d((n.x() < 0 ? a.min().x() : a.max().x()), Math.max(a.min().y(), b.min().y()));
            contactPoints[1] = new Vec2d(contactPoints[0].x(), Math.min(a.max().y(), b.max().y()));
        } else {
            normal = (n.y() < 0) ? new Vec2d(0, -1) : new Vec2d(0, 1);
            penetration = yOverlap;
            contactCount = 2;
            contactPoints[0] = new Vec2d(Math.max(a.min().x(), b.min().x()), (n.y() < 0 ? a.min().y() : a.max().y()));
            contactPoints[1] = new Vec2d(Math.min(a.max().x(), b.max().x()), contactPoints[0].y());
        }
        Manifold manifold = new Manifold(contactCount, contactPoints, normal, penetration);

        return true;
    }
}
