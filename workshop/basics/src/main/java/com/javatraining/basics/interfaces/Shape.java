package com.javatraining.basics.interfaces;

/**
 * Topic 6: Interfaces
 *
 * A sealed interface restricts which classes can implement it.
 * Combined with records and pattern matching, sealed interfaces
 * enable exhaustive switch expressions (no default needed).
 */
public sealed interface Shape permits Circle, Rectangle, Triangle {

    double area();

    double perimeter();

    default String describe() {
        return "Shape with area %.2f".formatted(area());
    }

    /**
     * Static factory method — returns a Circle without the caller
     * needing to know the concrete type.
     */
    static Shape circle(double radius) {
        return new Circle(radius);
    }

    static Shape rectangle(double width, double height) {
        return new Rectangle(width, height);
    }
}
