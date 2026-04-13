package com.javatraining.basics.patternmatching;

/**
 * Topic 9: Pattern Matching (Java 21)
 *
 * <p>Demonstrates: - Switch expressions with pattern matching - Sealed interfaces for exhaustive
 * matching - Guarded patterns (case X when condition) - Record patterns (deconstruct records in
 * switch) - Handling null in switch
 */
public class PatternMatching {

  // --- Sealed shape hierarchy defined within this package ---

  public sealed interface Shape permits Circle, Rectangle, Triangle {}

  public record Circle(double r) implements Shape {}

  public record Rectangle(double w, double h) implements Shape {}

  public record Triangle(double base, double height) implements Shape {}

  // --- Area calculation via pattern matching switch ---

  public static double area(Object shape) {
    return switch (shape) {
      case Circle c -> Math.PI * c.r() * c.r();
      case Rectangle r -> r.w() * r.h();
      case Triangle t -> 0.5 * t.base() * t.height();
      default -> throw new IllegalArgumentException("Unknown shape: " + shape);
    };
  }

  // --- Guarded patterns ---

  public static String classifyShape(Object shape) {
    return switch (shape) {
      case Circle c when c.r() > 10 -> "large circle (r=" + c.r() + ")";
      case Circle c -> "small circle (r=" + c.r() + ")";
      case Rectangle r -> "rectangle " + r.w() + "x" + r.h();
      case Triangle t -> "triangle base=" + t.base();
      default -> "unknown shape";
    };
  }

  // --- General object classifier ---

  public static String classify(Object obj) {
    return switch (obj) {
      case null -> "null";
      case String s -> "String: \"" + s + "\"";
      case Integer i -> "Integer: " + i;
      case Long l -> "Long: " + l;
      case Double d -> "Double: " + d;
      default -> "Other: " + obj.getClass().getSimpleName();
    };
  }

  // --- Record patterns: deconstruct in switch ---

  public static String describeCircle(Object obj) {
    return switch (obj) {
        // Record pattern: binds component directly
      case Circle(double r) when r == 0 -> "degenerate circle (point)";
      case Circle(double r) -> "circle with radius " + r;
      default -> "not a circle";
    };
  }
}
