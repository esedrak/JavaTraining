package com.javatraining.basics.generics;

import java.util.List;

/**
 * Demonstrates bounded type parameters: - Upper bound: <T extends SomeType> - Wildcard lower bound:
 * <? super T> (for write) - Wildcard upper bound: <? extends T> (for read)
 *
 * <p>PECS mnemonic: Producer Extends, Consumer Super
 */
public class BoundedGenerics {

  /**
   * Returns the larger of two Comparable values. T must implement Comparable<T>, so we can call
   * compareTo.
   */
  public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) >= 0 ? a : b;
  }

  /**
   * Returns the sum of a list of Numbers as a double. T must extend Number, giving us access to
   * doubleValue().
   */
  public static <T extends Number> double sum(List<T> numbers) {
    double total = 0.0;
    for (T n : numbers) {
      total += n.doubleValue();
    }
    return total;
  }

  /**
   * Copies all elements from src into dest.
   *
   * <p>PECS: - src is a Producer (we read from it) -> extends T - dest is a Consumer (we write to
   * it) -> super T
   */
  public static <T> void copy(List<? super T> dest, List<? extends T> src) {
    for (T item : src) {
      dest.add(item);
    }
  }
}
