package com.javatraining.basics.benchmarking;

import java.util.ArrayList;
import java.util.List;

/**
 * Educational class about performance measurement with JMH (Java Microbenchmark Harness).
 *
 * <h2>JMH Overview</h2>
 *
 * <p>JMH benchmarks live in a separate source set ({@code src/jmh/java}) to keep them isolated from
 * production code. Each benchmark class carries {@code @BenchmarkMode}, {@code @OutputTimeUnit} and
 * {@code @Warmup} / {@code @Measurement} annotations. The JMH Gradle plugin ({@code
 * me.champeau.jmh}) compiles that source set and produces a self-contained jar via {@code ./gradlew
 * jmh}.
 *
 * <h2>Why naive string concatenation is slow</h2>
 *
 * <p>Using {@code +} inside a loop causes the JVM to allocate a new {@code String} object on every
 * iteration. {@link StringBuilder} accumulates characters in a mutable buffer and produces only one
 * {@code String} at the end.
 *
 * <h2>Methods below</h2>
 *
 * <p>These methods demonstrate the implementation patterns that would normally be annotated with
 * {@code @Benchmark} in the JMH source set.
 */
public class BenchmarkingDemo {

  /**
   * Concatenates integers 0..iterations-1 using the {@code +} operator.
   *
   * <p>This is the slow pattern: every iteration creates a new {@code String} object.
   *
   * @param iterations number of values to concatenate
   * @return concatenated string like "01234"
   */
  public String stringConcatLoop(int iterations) {
    String result = "";
    for (int i = 0; i < iterations; i++) {
      result += i;
    }
    return result;
  }

  /**
   * Concatenates integers 0..iterations-1 using {@link StringBuilder}.
   *
   * <p>This is the fast pattern: the buffer grows in-place and only one {@code String} is created.
   *
   * @param iterations number of values to concatenate
   * @return concatenated string like "01234"
   */
  public String stringBuilderLoop(int iterations) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < iterations; i++) {
      sb.append(i);
    }
    return sb.toString();
  }

  /**
   * Creates an {@link ArrayList} and adds {@code size} integers to it.
   *
   * @param size number of elements to add
   * @return the populated list
   */
  public List<Integer> arrayListBatch(int size) {
    List<Integer> list = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      list.add(i);
    }
    return list;
  }

  /**
   * Creates an {@code int[]} of the given size, fills it with sequential values, and returns the
   * sum of all elements.
   *
   * @param size array length
   * @return sum of 0 + 1 + … + (size-1)
   */
  public int primitiveArraySum(int size) {
    int[] arr = new int[size];
    for (int i = 0; i < size; i++) {
      arr[i] = i;
    }
    int sum = 0;
    for (int v : arr) {
      sum += v;
    }
    return sum;
  }
}
