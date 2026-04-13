package com.javatraining.basics.interfaces;

/** A triangle — immutable record implementing Shape. Area calculated using Heron's formula. */
public record Triangle(double a, double b, double c) implements Shape {

  public Triangle {
    if (a <= 0 || b <= 0 || c <= 0) {
      throw new IllegalArgumentException("Side lengths must be positive");
    }
    if (a + b <= c || b + c <= a || a + c <= b) {
      throw new IllegalArgumentException("Invalid triangle sides");
    }
  }

  @Override
  public double area() {
    // Heron's formula: A = sqrt(s(s-a)(s-b)(s-c)), where s = (a+b+c)/2
    double s = (a + b + c) / 2.0;
    return Math.sqrt(s * (s - a) * (s - b) * (s - c));
  }

  @Override
  public double perimeter() {
    return a + b + c;
  }
}
