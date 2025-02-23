package chameleon.utils.colliding;

import chameleon.utils.Location;
import chameleon.utils.Vec2d;

/**
 * The AABB (Axis-Aligned Bounding Box) class is used for collision detection.
 * It represents a rectangular bounding box aligned with the coordinate axes.
 */
public record AABB(Vec2d min, Vec2d max) {
    /**
     * Creates an AABB from the center and size of the box.
     *
     * @param center the center of the box
     * @param size   the size of the box
     * @return the AABB
     */
    public static AABB fromCenterAndSize(Location centerLocation, double size) {
        Vec2d center = Vec2d.fromLocation(centerLocation);
        return new AABB(center.subtract(size / 2, size / 2), center.add(size / 2, size / 2));
    }

    /**
     * Creates an AABB from the center and size of the box.
     *
     * @param center the center of the box
     * @param width  the width of the box
     * @param height the height of the box
     * @return the AABB
     */
    public static AABB fromCenterAndSize(Location centerLocation, double width, double height) {
        Vec2d center = Vec2d.fromLocation(centerLocation);
        return new AABB(center.subtract(width / 2, height / 2), center.add(width / 2, height / 2));
    }

    /**
     * Creates an AABB from the center and the distances to the left, bottom, right, and top edges.
     *
     * @param center the center of the box
     * @param left   the distance from the center to the left edge
     * @param right  the distance from the center to the right edge
     * @param top    the distance from the center to the top edge
     * @param bottom the distance from the center to the bottom edge
     * @return the AABB
     */
    public static AABB fromLRTB(Location centerLocation, double left, double right, double top, double bottom) {
        Vec2d center = Vec2d.fromLocation(centerLocation);
        return new AABB(center.subtract(left, top), center.add(right, bottom));
    }
}