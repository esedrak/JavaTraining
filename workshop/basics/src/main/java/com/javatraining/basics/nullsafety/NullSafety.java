package com.javatraining.basics.nullsafety;

import java.util.Objects;
import java.util.Optional;

/**
 * Topic 4: Null Safety
 *
 * <p>Demonstrates idiomatic Java approaches to handling null: - Optional<T> for values that may be
 * absent - Objects.requireNonNull for guard clauses - Objects.isNull / Objects.nonNull for explicit
 * null checks
 */
public class NullSafety {

  // --- Optional creation ---

  public static Optional<String> maybeValue(String s) {
    return Optional.ofNullable(s);
  }

  public static Optional<String> definitelyEmpty() {
    return Optional.empty();
  }

  // --- Guard clause ---

  public static String requireNonNullDemo(String value) {
    // Throws NullPointerException with the given message if value is null
    Objects.requireNonNull(value, "value must not be null");
    return value.toUpperCase();
  }

  // --- Objects.isNull / nonNull ---

  public static boolean isNullCheck(Object obj) {
    return Objects.isNull(obj);
  }

  public static boolean isNonNullCheck(Object obj) {
    return Objects.nonNull(obj);
  }

  // --- Null-safe string operations ---

  /**
   * Returns Optional.empty() if userId is null, otherwise simulates a lookup and returns an email.
   */
  public static Optional<String> findUserEmail(String userId) {
    if (Objects.isNull(userId)) {
      return Optional.empty();
    }
    // Simulate a lookup
    return Optional.of(userId + "@example.com");
  }

  /** Returns the uppercase of value if non-null, otherwise returns defaultValue. */
  public static String getUpperOrDefault(String value, String defaultValue) {
    return Optional.ofNullable(value).map(String::toUpperCase).orElse(defaultValue);
  }

  // --- Optional chaining ---

  public static String chainedOptional(String input) {
    return Optional.ofNullable(input)
        .filter(s -> !s.isBlank())
        .map(String::trim)
        .map(String::toUpperCase)
        .orElseGet(() -> "BLANK_OR_NULL");
  }

  public static int optionalOrElseThrow(String number) {
    return Optional.ofNullable(number)
        .map(Integer::parseInt)
        .orElseThrow(() -> new IllegalArgumentException("No number provided"));
  }

  // --- ifPresent example ---

  public static String ifPresentDemo(String value) {
    StringBuilder result = new StringBuilder();
    Optional.ofNullable(value).ifPresent(v -> result.append("Found: ").append(v));
    return result.isEmpty() ? "absent" : result.toString();
  }
}
