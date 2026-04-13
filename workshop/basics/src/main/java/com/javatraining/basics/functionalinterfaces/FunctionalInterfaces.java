package com.javatraining.basics.functionalinterfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Comprehensive demonstration of functional interfaces in Java.
 *
 * <h2>Custom functional interfaces</h2>
 *
 * <p>Java allows any interface with exactly one abstract method to be used as a lambda target. The
 * {@link FunctionalInterface} annotation is optional but documents intent and causes a compile-time
 * error if the contract is violated.
 *
 * <h2>Built-in functional interfaces (java.util.function)</h2>
 *
 * <ul>
 *   <li>{@link Predicate}{@code <T>} — {@code boolean test(T t)}
 *   <li>{@link Function}{@code <T,R>} — {@code R apply(T t)}
 *   <li>{@link Consumer}{@code <T>} — {@code void accept(T t)}
 *   <li>{@link java.util.function.Supplier}{@code <T>} — {@code T get()}
 *   <li>{@link java.util.function.BiFunction}{@code <T,U,R>} — {@code R apply(T t, U u)}
 *   <li>{@link BinaryOperator}{@code <T>} — {@code T apply(T t1, T t2)}
 * </ul>
 *
 * <h2>Method reference forms</h2>
 *
 * <ul>
 *   <li>Static: {@code ClassName::staticMethod}
 *   <li>Instance (bound): {@code instance::method}
 *   <li>Instance (unbound): {@code ClassName::instanceMethod}
 *   <li>Constructor: {@code ClassName::new}
 * </ul>
 */
public class FunctionalInterfaces {

  // ── Custom functional interfaces ─────────────────────────────────────────────

  /** Transforms an input value of type {@code T} into another value of the same type. */
  @FunctionalInterface
  public interface Transformer<T> {
    T transform(T input);
  }

  /** Validates an input value of type {@code T}, returning {@code true} if valid. */
  @FunctionalInterface
  public interface Validator<T> {
    boolean validate(T input);
  }

  /** Converts an input of type {@code A} to a result of type {@code B}. */
  @FunctionalInterface
  public interface Converter<A, B> {
    B convert(A input);
  }

  // ── Higher-order utility methods ─────────────────────────────────────────────

  /**
   * Returns a new list containing only the elements from {@code items} for which {@code predicate}
   * returns {@code true}.
   *
   * @param items source list
   * @param predicate filter criterion
   * @param <T> element type
   * @return filtered list (preserves order)
   */
  public <T> List<T> filter(List<T> items, Predicate<T> predicate) {
    List<T> result = new ArrayList<>();
    for (T item : items) {
      if (predicate.test(item)) {
        result.add(item);
      }
    }
    return result;
  }

  /**
   * Applies {@code mapper} to every element of {@code items} and returns the mapped values.
   *
   * @param items source list
   * @param mapper transformation function
   * @param <T> input element type
   * @param <R> output element type
   * @return transformed list (preserves order)
   */
  public <T, R> List<R> transform(List<T> items, Function<T, R> mapper) {
    List<R> result = new ArrayList<>();
    for (T item : items) {
      result.add(mapper.apply(item));
    }
    return result;
  }

  /**
   * Folds {@code items} from left to right using {@code accumulator}, starting from {@code
   * identity}.
   *
   * @param items source list
   * @param identity initial accumulator value
   * @param accumulator combining function
   * @param <T> element type
   * @return accumulated result
   */
  public <T> T reduce(List<T> items, T identity, BinaryOperator<T> accumulator) {
    T result = identity;
    for (T item : items) {
      result = accumulator.apply(result, item);
    }
    return result;
  }

  /**
   * Calls {@code action} for every element of {@code items}.
   *
   * @param items source list
   * @param action side-effecting consumer
   * @param <T> element type
   */
  public <T> void forEach(List<T> items, Consumer<T> action) {
    for (T item : items) {
      action.accept(item);
    }
  }
}
