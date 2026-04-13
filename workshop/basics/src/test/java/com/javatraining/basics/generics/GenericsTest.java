package com.javatraining.basics.generics;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class GenericsTest {

  // --- GenericBox ---

  @Test
  void genericBox_of_mapsStringToLength() {
    GenericBox<Integer> result = GenericBox.of("hello").map(String::length);
    assertThat(result.getValue()).isEqualTo(5);
  }

  @Test
  void genericBox_empty_isEmpty() {
    assertThat(GenericBox.empty().isEmpty()).isTrue();
  }

  @Test
  void genericBox_of_isNotEmpty() {
    assertThat(GenericBox.of("value").isEmpty()).isFalse();
  }

  @Test
  void genericBox_map_onEmpty_returnsEmpty() {
    GenericBox<Integer> result = GenericBox.<String>empty().map(String::length);
    assertThat(result.isEmpty()).isTrue();
  }

  @Test
  void genericBox_map_chain() {
    GenericBox<String> result = GenericBox.of(42).map(n -> n * 2).map(Object::toString);
    assertThat(result.getValue()).isEqualTo("84");
  }

  // --- Pair ---

  @Test
  void pair_of_createsCorrectPair() {
    Pair<Integer, String> pair = Pair.of(1, "one");
    assertThat(pair.first()).isEqualTo(1);
    assertThat(pair.second()).isEqualTo("one");
  }

  @Test
  void pair_swap_reversesElements() {
    Pair<String, Integer> swapped = Pair.of(1, "one").swap();
    assertThat(swapped.first()).isEqualTo("one");
    assertThat(swapped.second()).isEqualTo(1);
  }

  @Test
  void pair_record_equality() {
    Pair<Integer, String> p1 = Pair.of(1, "one");
    Pair<Integer, String> p2 = Pair.of(1, "one");
    assertThat(p1).isEqualTo(p2);
  }

  // --- BoundedGenerics ---

  @Test
  void max_integers_returnsLarger() {
    assertThat(BoundedGenerics.max(3, 7)).isEqualTo(7);
  }

  @Test
  void max_integers_whenFirstLarger_returnsFirst() {
    assertThat(BoundedGenerics.max(10, 2)).isEqualTo(10);
  }

  @Test
  void max_strings_returnsLexicographicallyLarger() {
    assertThat(BoundedGenerics.max("apple", "banana")).isEqualTo("banana");
  }

  @Test
  void sum_integers_returnsDoubleSum() {
    assertThat(BoundedGenerics.sum(List.of(1, 2, 3))).isEqualTo(6.0);
  }

  @Test
  void sum_doubles_returnsSumAsDouble() {
    assertThat(BoundedGenerics.sum(List.of(1.5, 2.5))).isEqualTo(4.0);
  }

  @Test
  void copy_copiesElementsFromSrcToDest() {
    List<Number> dest = new ArrayList<>();
    List<Integer> src = List.of(1, 2, 3);
    BoundedGenerics.copy(dest, src);
    assertThat(dest).containsExactly(1, 2, 3);
  }

  // --- TypeErasureDemo ---

  @Test
  void typeErasure_arrayListsHaveSameClass() {
    assertThat(TypeErasureDemo.arrayListsHaveSameClass()).isTrue();
  }

  @Test
  void typeErasure_rawClassEquality() {
    // Direct demonstration of erasure
    boolean sameClass = new ArrayList<String>().getClass() == new ArrayList<Integer>().getClass();
    assertThat(sameClass).isTrue();
  }

  @Test
  void typeErasure_recoverFieldGenericType_returnsStringClassName() throws NoSuchFieldException {
    String typeName = TypeErasureDemo.recoverFieldGenericType();
    assertThat(typeName).isEqualTo("java.lang.String");
  }
}
