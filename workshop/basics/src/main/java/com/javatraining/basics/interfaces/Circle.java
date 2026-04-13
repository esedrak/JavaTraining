package com.javatraining.basics.interfaces;

/**
 * A circle — immutable record implementing Shape.
 * Records are ideal for shapes because they are naturally immutable value objects.
 */
public record Circle(double radius) implements Shape {

    public Circle {
        if (radius < 0) throw new IllegalArgumentException("Radius must be non-negative");
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }
}
