package com.javatraining.basics.modernjavafeatures;

import java.time.DayOfWeek;

/**
 * Topic 19: Modern Java Features (Java 10–21)
 *
 * <p>Showcases language features introduced across recent Java versions: records, sealed classes,
 * text blocks, switch expressions, {@code var}, pattern matching in switch, and unnamed variables.
 */
public class ModernJavaFeatures {

  // -------------------------------------------------------------------------
  // Records (Java 16) — compact, immutable data carriers
  // -------------------------------------------------------------------------

  /**
   * Represents a geographic coordinate.
   *
   * <p>The compiler automatically generates: constructor, accessors, {@code equals}, {@code
   * hashCode}, and {@code toString}.
   */
  public record Coordinate(double lat, double lon) {}

  // -------------------------------------------------------------------------
  // Sealed classes (Java 17) — restrict which classes may implement an interface
  // -------------------------------------------------------------------------

  /**
   * A discriminated-union result type. Only {@link Success} and {@link Failure} may implement it
   * because of the {@code permits} clause.
   */
  public sealed interface Result<T> permits Success, Failure {}

  /** Wraps a successful value. */
  public record Success<T>(T value) implements Result<T> {}

  /** Wraps an error description. */
  public record Failure<T>(String error) implements Result<T> {}

  /**
   * Describes a {@link Result} using a switch expression with sealed-class exhaustiveness. The
   * compiler can verify that all permitted subtypes are handled.
   */
  public <T> String describe(Result<T> result) {
    return switch (result) {
      case Success<T> s -> "Success: " + s.value();
      case Failure<T> f -> "Failure: " + f.error();
    };
  }

  // -------------------------------------------------------------------------
  // Text blocks (Java 15) — multiline string literals
  // -------------------------------------------------------------------------

  /** Returns a JSON snippet using a text block. */
  public String jsonTextBlock() {
    return """
        {
          "name": "JavaTraining",
          "version": "21"
        }
        """;
  }

  /** Returns a SQL snippet using a text block. */
  public String sqlTextBlock() {
    return """
        SELECT id, name, email
          FROM users
         WHERE active = true
         ORDER BY name;
        """;
  }

  // -------------------------------------------------------------------------
  // Switch expressions (Java 14) — expressions, not statements
  // -------------------------------------------------------------------------

  /**
   * Classifies a day of the week as "Weekday" or "Weekend" using an arrow-form switch expression.
   */
  public String dayType(DayOfWeek day) {
    return switch (day) {
      case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
      case SATURDAY, SUNDAY -> "Weekend";
    };
  }

  // -------------------------------------------------------------------------
  // var (Java 10) — local variable type inference
  // -------------------------------------------------------------------------

  /** Demonstrates {@code var} — the compiler infers the type from the right-hand side. */
  public String varDemo() {
    var greeting = "Hello"; // inferred as String
    var count = 42; // inferred as int (boxed to Integer in collections)
    var items = new java.util.ArrayList<String>(); // inferred as ArrayList<String>
    items.add(greeting);
    return greeting + "-" + count + "-" + items.size();
  }

  // -------------------------------------------------------------------------
  // Pattern matching in switch (Java 21)
  // -------------------------------------------------------------------------

  /**
   * Formats an object based on its runtime type using Java 21 pattern matching in switch. The
   * {@code null} case is handled explicitly to avoid a {@link NullPointerException}.
   */
  public String format(Object obj) {
    return switch (obj) {
      case null -> "null";
      case Integer i -> "Integer: " + i;
      case Double d -> "Double: " + d;
      case String s -> "String: " + s;
      case int[] arr -> "int[]: length=" + arr.length;
      default -> "Other: " + obj.getClass().getSimpleName();
    };
  }

  // -------------------------------------------------------------------------
  // Unnamed variables _ (Java 21 preview / 22 final) — catch block demo
  // -------------------------------------------------------------------------

  /**
   * Demonstrates an unnamed catch variable {@code _} (Java 21+). When the exception object itself
   * is not needed, {@code _} signals intent to the reader.
   */
  public String ignoredExceptionDemo(String input) {
    try {
      return String.valueOf(Integer.parseInt(input));
    } catch (NumberFormatException ignored) {
      // exception object not needed — use _ to document that intentionally
      return "not-a-number";
    }
  }
}
