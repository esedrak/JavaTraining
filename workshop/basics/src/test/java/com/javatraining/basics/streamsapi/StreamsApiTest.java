package com.javatraining.basics.streamsapi;

import static org.assertj.core.api.Assertions.assertThat;

import com.javatraining.basics.streamsapi.StreamsApi.Person;
import com.javatraining.basics.streamsapi.StreamsApi.Transaction;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StreamsApiTest {

  private StreamsApi streamsApi;

  @BeforeEach
  void setUp() {
    streamsApi = new StreamsApi();
  }

  @Test
  void filterAndSort_filtersAndSortsCorrectly() {
    List<String> names = List.of("Alice", "Bob", "Anna", "Charlie", "Andrew");
    List<String> result = streamsApi.filterAndSort(names, "A");
    assertThat(result).containsExactly("Alice", "Andrew", "Anna");
  }

  @Test
  void filterAndSort_noMatch_returnsEmptyList() {
    List<String> result = streamsApi.filterAndSort(List.of("Bob", "Carol"), "Z");
    assertThat(result).isEmpty();
  }

  @Test
  void groupByLength_groupsCorrectly() {
    List<String> words = List.of("hi", "hey", "hello");
    Map<Integer, List<String>> result = streamsApi.groupByLength(words);

    assertThat(result).containsKey(2);
    assertThat(result.get(2)).containsExactly("hi");

    assertThat(result).containsKey(3);
    assertThat(result.get(3)).containsExactly("hey");

    assertThat(result).containsKey(5);
    assertThat(result.get(5)).containsExactly("hello");
  }

  @Test
  void averageAge_skipsUnder18() {
    List<Person> people =
        List.of(new Person("Alice", 25), new Person("Bob", 16), new Person("Carol", 30));
    OptionalDouble avg = streamsApi.averageAge(people);
    assertThat(avg).isPresent();
    // (25 + 30) / 2 = 27.5
    assertThat(avg.getAsDouble()).isEqualTo(27.5);
  }

  @Test
  void averageAge_allUnder18_returnsEmpty() {
    List<Person> people = List.of(new Person("Kid", 10), new Person("Teen", 15));
    assertThat(streamsApi.averageAge(people)).isEmpty();
  }

  @Test
  void countByCategory_countsCorrectly() {
    List<Transaction> txns =
        List.of(
            new Transaction("FOOD", 10.0),
            new Transaction("FOOD", 20.0),
            new Transaction("TRAVEL", 100.0));
    Map<String, Long> counts = streamsApi.countByCategory(txns);
    assertThat(counts).containsEntry("FOOD", 2L).containsEntry("TRAVEL", 1L);
  }

  @Test
  void flatMapExample_flattensNested() {
    List<List<String>> nested = List.of(List.of("a", "b"), List.of("c"), List.of("d", "e"));
    List<String> result = streamsApi.flatMapExample(nested);
    assertThat(result).containsExactly("a", "b", "c", "d", "e");
  }

  @Test
  void anyMatch_returnsTrueWhenThresholdExceeded() {
    assertThat(streamsApi.anyMatch(List.of(1, 2, 5, 3), 4)).isTrue();
  }

  @Test
  void anyMatch_returnsFalseWhenNoneExceedThreshold() {
    assertThat(streamsApi.anyMatch(List.of(1, 2, 3), 10)).isFalse();
  }

  @Test
  void findFirst_returnsMatchingElement() {
    Optional<String> result =
        streamsApi.findFirst(List.of("apple", "banana", "avocado"), "a");
    assertThat(result).isPresent().hasValue("apple");
  }

  @Test
  void findFirst_noMatch_returnsEmpty() {
    Optional<String> result = streamsApi.findFirst(List.of("banana", "cherry"), "z");
    assertThat(result).isEmpty();
  }

  @Test
  void sumOfSquares_computesCorrectly() {
    // 1^2 + 2^2 + 3^2 = 1 + 4 + 9 = 14
    assertThat(streamsApi.sumOfSquares(List.of(1, 2, 3))).isEqualTo(14);
  }

  @Test
  void sumOfSquares_emptyList_returnsZero() {
    assertThat(streamsApi.sumOfSquares(List.of())).isZero();
  }
}
