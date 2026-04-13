package com.javatraining.basics.generics;

import java.util.function.Function;

/**
 * Topic 11: Generics
 *
 * <p>GenericBox<T> is a simple container demonstrating: - Generic type parameter - Bounded type
 * methods - map() for transforming the contained value - Static factory methods with type inference
 */
public class GenericBox<T> {

  private final T value;

  private GenericBox(T value) {
    this.value = value;
  }

  public static <T> GenericBox<T> of(T value) {
    return new GenericBox<>(value);
  }

  public static <T> GenericBox<T> empty() {
    return new GenericBox<>(null);
  }

  public T getValue() {
    return value;
  }

  public boolean isEmpty() {
    return value == null;
  }

  /**
   * Transforms the value inside the box using the given mapper function. Returns a new
   * GenericBox<U> without modifying the original.
   */
  public <U> GenericBox<U> map(Function<T, U> mapper) {
    if (isEmpty()) {
      return GenericBox.empty();
    }
    return GenericBox.of(mapper.apply(value));
  }

  @Override
  public String toString() {
    return isEmpty() ? "GenericBox[empty]" : "GenericBox[" + value + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GenericBox<?> other)) return false;
    if (value == null) return other.value == null;
    return value.equals(other.value);
  }

  @Override
  public int hashCode() {
    return value == null ? 0 : value.hashCode();
  }
}
