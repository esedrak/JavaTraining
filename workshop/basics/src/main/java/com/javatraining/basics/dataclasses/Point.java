package com.javatraining.basics.dataclasses;

/**
 * A 2D point record with validation.
 * Records provide equals, hashCode, and toString automatically.
 * Compact constructors validate without repeating field assignment.
 */
public record Point(int x, int y) {

    // Compact constructor: validation runs before field assignment
    public Point {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException(
                    "Coordinates must be non-negative, got: (" + x + ", " + y + ")");
        }
    }

    public double distanceTo(Point other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
