package chameleon.utils.colliding;

import chameleon.utils.Location;
import chameleon.utils.Vec2d;

/**
 * The AABB (Axis-Aligned Bounding Box) class is used for collision detection.
 * It represents a rectangular bounding box aligned with the coordinate axes.
 */
public record AABB(Vec2d min, Vec2d max) {
    /**
     * Creates an AABB from the center location and a uniform size.
     *
     * @param centerLocation the center location of the AABB
     * @param size           the size of the AABB (both width and height)
     * @return the created AABB
     */
    public static AABB fromCenterAndSize(Location centerLocation, double size) {
        return fromCenterAndSize(centerLocation, size, size);
    }

    /**
     * Creates an AABB from the center location and specified width and height.
     *
     * @param centerLocation the center location of the AABB
     * @param width          the width of the AABB
     * @param height         the height of the AABB
     * @return the created AABB
     */
    public static AABB fromCenterAndSize(Location centerLocation, double width, double height) {
        Vec2d center = Vec2d.fromLocation(centerLocation);
        return new AABB(center.subtract(width / 2, height / 2), center.add(width / 2, height / 2));
    }

    /**
     * Creates an AABB from the center and the distances to the left, bottom, right, and top edges.
     *
     * @param centerLocation the center location of the AABB
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

    public AABB larger(double left, double right, double top, double bottom) {
        return new AABB(min.subtract(left, top), max.add(right, bottom));
    }

    public AABB larger(double amount) {
        return larger(amount, amount, amount, amount);
    }
}