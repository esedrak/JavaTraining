package com.javatraining.basics.primitivesandobjects;

import java.util.Optional;

/**
 * Topic 1: Primitives and Objects
 *
 * <p>Demonstrates: - Primitives vs wrapper types - Autoboxing and unboxing - == vs .equals()
 * (identity vs equality) - Pass-by-value semantics - Optional<T> - String immutability
 */
public class PrimitivesAndObjects {

  // --- Autoboxing / Unboxing ---

  public static Integer boxInt(int value) {
    // Autoboxing: int -> Integer (JVM does this automatically)
    Integer boxed = value;
    return boxed;
  }

  public static int unboxInteger(Integer value) {
    // Unboxing: Integer -> int (JVM does this automatically)
    int primitive = value;
    return primitive;
  }

  // --- Integer Cache Pitfall ---

  /**
   * Demonstrates the Integer cache trap. Integers in the range -128..127 are cached by the JVM.
   * Outside that range, == compares references (not values).
   */
  public static void demonstrateBoxingPitfall() {
    // Within cache range: same reference
    Integer a = 127;
    Integer b = 127;
    System.out.println("127 == 127 (cached): " + (a == b)); // true
    System.out.println("127 equals 127: " + a.equals(b)); // true

    // Outside cache range: different references
    Integer x = 200;
    Integer y = 200;
    System.out.println("200 == 200 (not cached): " + (x == y)); // false
    System.out.println("200 equals 200: " + x.equals(y)); // true
  }

  public static boolean identityCheck(Integer a, Integer b) {
    return a == b; // reference equality
  }

  public static boolean equalityCheck(Integer a, Integer b) {
    return a.equals(b); // value equality
  }

  // --- Pass-by-Value ---

  /**
   * Primitives are passed by value: changes inside this method do NOT affect the caller's variable.
   */
  public static void tryToModifyPrimitive(int value) {
    value = value + 100; // only modifies local copy
  }

  /**
   * Object references are also passed by value. Reassigning the reference inside this method does
   * NOT affect the caller's reference.
   */
  public static void tryToModifyReference(StringBuilder sb) {
    sb = new StringBuilder("replaced"); // reassigns local copy only
  }

  /**
   * Mutating the object the reference points to DOES affect the caller, because both caller and
   * callee reference the same object.
   */
  public static void mutateObject(StringBuilder sb) {
    sb.append(" appended"); // mutates the shared object
  }

  // --- Optional<T> ---

  public static String optionalDemo(String input) {
    return Optional.ofNullable(input).map(String::toUpperCase).orElse("empty");
  }

  public static Optional<Integer> parseIntSafe(String s) {
    try {
      return Optional.of(Integer.parseInt(s));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  public static String flatMapDemo(Optional<String> outer) {
    return outer.flatMap(s -> s.isEmpty() ? Optional.empty() : Optional.of(s)).orElse("nothing");
  }

  // --- String Immutability ---

  /**
   * String + creates a new String object every time. For concatenation in loops, use StringBuilder.
   */
  public static String buildWithConcat(int count) {
    String result = "";
    for (int i = 0; i < count; i++) {
      result = result + i; // creates a new String each iteration
    }
    return result;
  }

  public static String buildWithStringBuilder(int count) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < count; i++) {
      sb.append(i);
    }
    return sb.toString();
  }

  public static boolean stringConcatCreatesNewObject(String original) {
    String concatenated = original + "";
    // Even concatenating empty string may create a new object (impl-dependent),
    // but adding non-empty content definitely creates a new object.
    String withContent = original + "x";
    return withContent != original; // always true
  }
}
