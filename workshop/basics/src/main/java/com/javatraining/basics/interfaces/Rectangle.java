package com.javatraining.basics.interfaces;

/**
 * A rectangle — immutable record implementing Shape.
 */
public record Rectangle(double width, double height) implements Shape {

    public Rectangle {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Dimensions must be non-negative");
        }
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double perimeter() {
        return 2 * (width + height);
    }
}
