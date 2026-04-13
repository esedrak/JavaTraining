package com.javatraining.basics.streamsapi;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * Topic 17: Java Streams API
 *
 * <p>The Streams API (Java 8+) provides a declarative, pipeline-based way to process collections.
 * Streams are lazy: intermediate operations (filter, map, sorted, …) build a pipeline but do no
 * work until a terminal operation (collect, count, reduce, …) is invoked.
 */
public class StreamsApi {

  /** Simple value type used in stream demos. */
  public record Person(String name, int age) {}

  /** Represents a financial transaction. */
  public record Transaction(String category, double amount) {}

  // -------------------------------------------------------------------------
  // Demo methods
  // -------------------------------------------------------------------------

  /**
   * Filters a list of names to those starting with {@code prefix}, then returns them sorted
   * alphabetically.
   */
  public List<String> filterAndSort(List<String> names, String prefix) {
    return names.stream()
        .filter(name -> name.startsWith(prefix))
        .sorted()
        .collect(Collectors.toList());
  }

  /** Groups words by their character length. */
  public Map<Integer, List<String>> groupByLength(List<String> words) {
    return words.stream().collect(Collectors.groupingBy(String::length));
  }

  /**
   * Computes the average age of people older than 18. Returns an empty {@link OptionalDouble} if
   * no such person exists.
   */
  public OptionalDouble averageAge(List<Person> people) {
    return people.stream()
        .filter(p -> p.age() > 18)
        .mapToInt(Person::age)
        .average();
  }

  /**
   * Counts how many transactions belong to each category.
   *
   * @return map from category name to count
   */
  public Map<String, Long> countByCategory(List<Transaction> transactions) {
    return transactions.stream().collect(Collectors.groupingBy(Transaction::category, Collectors.counting()));
  }

  /** Flattens a list of lists into a single list while preserving order. */
  public List<String> flatMapExample(List<List<String>> nested) {
    return nested.stream().flatMap(List::stream).collect(Collectors.toList());
  }

  /** Returns {@code true} if any number in the list is strictly greater than {@code threshold}. */
  public boolean anyMatch(List<Integer> numbers, int threshold) {
    return numbers.stream().anyMatch(n -> n > threshold);
  }

  /**
   * Returns the first element (in encounter order) that starts with {@code prefix}, or empty if
   * none match.
   */
  public Optional<String> findFirst(List<String> items, String prefix) {
    return items.stream().filter(s -> s.startsWith(prefix)).findFirst();
  }

  /**
   * Computes the sum of squares of all elements.
   *
   * <p>Demonstrates {@code mapToInt} for primitive specialization and {@code reduce} for folding.
   */
  public int sumOfSquares(List<Integer> numbers) {
    return numbers.stream().mapToInt(n -> n * n).reduce(0, Integer::sum);
  }

  /**
   * Shows lazy evaluation using {@code peek()}.
   *
   * <p>{@code peek} is an intermediate operation that lets you observe elements as they pass
   * through the pipeline without consuming them.
   */
  public List<Integer> peekDemo(List<Integer> numbers) {
    return numbers.stream()
        .peek(n -> System.out.println("Before filter: " + n))
        .filter(n -> n % 2 == 0)
        .peek(n -> System.out.println("After filter: " + n))
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.toList());
  }
}
