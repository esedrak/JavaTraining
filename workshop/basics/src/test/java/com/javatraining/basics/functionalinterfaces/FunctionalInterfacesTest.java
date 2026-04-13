package com.javatraining.basics.functionalinterfaces;

import static org.assertj.core.api.Assertions.assertThat;

import com.javatraining.basics.functionalinterfaces.FunctionalInterfaces.Converter;
import com.javatraining.basics.functionalinterfaces.FunctionalInterfaces.Transformer;
import com.javatraining.basics.functionalinterfaces.FunctionalInterfaces.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Tests for the higher-order utility methods and custom functional interfaces. */
@DisplayName("Functional Interfaces Tests")
class FunctionalInterfacesTest {

  private FunctionalInterfaces fi;

  @BeforeEach
  void setUp() {
    fi = new FunctionalInterfaces();
  }

  // ── filter ───────────────────────────────────────────────────────────────────

  @Nested
  @DisplayName("filter()")
  class FilterTests {

    @Test
    @DisplayName("filter even numbers returns [2, 4]")
    void filterEvenNumbers() {
      List<Integer> result = fi.filter(List.of(1, 2, 3, 4, 5), n -> n % 2 == 0);
      assertThat(result).containsExactly(2, 4);
    }

    @Test
    @DisplayName("filter with always-false predicate returns empty list")
    void filterAlwaysFalseReturnsEmpty() {
      List<Integer> result = fi.filter(List.of(1, 2, 3), n -> n > 100);
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("filter strings longer than 4 characters")
    void filterLongStrings() {
      List<String> result =
          fi.filter(List.of("hi", "hello", "hey", "world"), s -> s.length() > 4);
      assertThat(result).containsExactly("hello", "world");
    }
  }

  // ── transform ────────────────────────────────────────────────────────────────

  @Nested
  @DisplayName("transform()")
  class TransformTests {

    @Test
    @DisplayName("transform strings to uppercase using method reference")
    void transformToUppercase() {
      List<String> result = fi.transform(List.of("hello", "world"), String::toUpperCase);
      assertThat(result).containsExactly("HELLO", "WORLD");
    }

    @Test
    @DisplayName("transform integers by squaring them")
    void transformBySquaring() {
      List<Integer> result = fi.transform(List.of(1, 2, 3, 4), n -> n * n);
      assertThat(result).containsExactly(1, 4, 9, 16);
    }
  }

  // ── reduce ───────────────────────────────────────────────────────────────────

  @Nested
  @DisplayName("reduce()")
  class ReduceTests {

    @Test
    @DisplayName("reduce sum of 1..5 = 15")
    void reduceSumOf1To5() {
      int result = fi.reduce(List.of(1, 2, 3, 4, 5), 0, Integer::sum);
      assertThat(result).isEqualTo(15);
    }

    @Test
    @DisplayName("reduce string concatenation")
    void reduceStringConcat() {
      String result = fi.reduce(List.of("a", "b", "c"), "", (acc, s) -> acc + s);
      assertThat(result).isEqualTo("abc");
    }

    @Test
    @DisplayName("reduce with empty list returns identity")
    void reduceEmptyListReturnsIdentity() {
      int result = fi.reduce(List.of(), 42, Integer::sum);
      assertThat(result).isEqualTo(42);
    }
  }

  // ── forEach ──────────────────────────────────────────────────────────────────

  @Test
  @DisplayName("forEach calls consumer for every element")
  void forEachCallsConsumer() {
    List<Integer> collected = new ArrayList<>();
    fi.forEach(List.of(10, 20, 30), collected::add);
    assertThat(collected).containsExactly(10, 20, 30);
  }

  // ── Predicate composition ────────────────────────────────────────────────────

  @Nested
  @DisplayName("Predicate Composition")
  class PredicateCompositionTests {

    @Test
    @DisplayName("isPositive.and(isEven) filters correctly")
    void andCompositionFiltersCorrectly() {
      Predicate<Integer> isPositive = n -> n > 0;
      Predicate<Integer> isEven = n -> n % 2 == 0;

      List<Integer> result =
          fi.filter(List.of(-4, -1, 0, 1, 2, 3, 4, 5), isPositive.and(isEven));

      assertThat(result).containsExactly(2, 4);
    }

    @Test
    @DisplayName("predicate.negate() inverts the condition")
    void negateInvertsCondition() {
      Predicate<Integer> isEven = n -> n % 2 == 0;
      List<Integer> result = fi.filter(List.of(1, 2, 3, 4, 5), isEven.negate());
      assertThat(result).containsExactly(1, 3, 5);
    }

    @Test
    @DisplayName("predicate.or() accepts elements matching either condition")
    void orAcceptsEitherCondition() {
      Predicate<Integer> isZero = n -> n == 0;
      Predicate<Integer> isNegative = n -> n < 0;
      List<Integer> result =
          fi.filter(List.of(-3, -1, 0, 1, 2, 3), isZero.or(isNegative));
      assertThat(result).containsExactly(-3, -1, 0);
    }
  }

  // ── Function composition ─────────────────────────────────────────────────────

  @Nested
  @DisplayName("Function Composition")
  class FunctionCompositionTests {

    @Test
    @DisplayName("addOne.andThen(double) applies both transformations")
    void andThenAppliesBothTransformations() {
      Function<Integer, Integer> addOne = n -> n + 1;
      Function<Integer, Integer> doubled = n -> n * 2;
      Function<Integer, Integer> addOneThenDouble = addOne.andThen(doubled);

      assertThat(addOneThenDouble.apply(3)).isEqualTo(8); // (3+1)*2
    }

    @Test
    @DisplayName("function.compose applies in reverse order")
    void composeAppliesInReverseOrder() {
      Function<Integer, Integer> addOne = n -> n + 1;
      Function<Integer, Integer> doubled = n -> n * 2;
      // compose: doubled first, then addOne -> double(3)+1 = 7
      Function<Integer, Integer> doubleThenAddOne = addOne.compose(doubled);

      assertThat(doubleThenAddOne.apply(3)).isEqualTo(7);
    }
  }

  // ── Custom functional interfaces ─────────────────────────────────────────────

  @Nested
  @DisplayName("Custom Functional Interfaces")
  class CustomFunctionalInterfaceTests {

    @Test
    @DisplayName("Transformer<String> trims and uppercases")
    void transformerTrimsAndUppercases() {
      Transformer<String> trimAndUpper = s -> s.trim().toUpperCase();
      assertThat(trimAndUpper.transform("  hello  ")).isEqualTo("HELLO");
    }

    @Test
    @DisplayName("Validator<String> composition: not-blank AND length > 3")
    void validatorCompositionNotBlankAndLengthGt3() {
      Validator<String> notBlank = s -> s != null && !s.isBlank();
      Validator<String> longEnough = s -> s != null && s.length() > 3;
      // Compose using a lambda that ANDs both
      Validator<String> combined = s -> notBlank.validate(s) && longEnough.validate(s);

      assertThat(combined.validate("hi")).isFalse();
      assertThat(combined.validate("")).isFalse();
      assertThat(combined.validate("hello")).isTrue();
    }

    @Test
    @DisplayName("Converter<String, Integer> parses string to integer")
    void converterParsesStringToInteger() {
      Converter<String, Integer> toInt = Integer::parseInt;
      assertThat(toInt.convert("42")).isEqualTo(42);
    }

    @Test
    @DisplayName("Converter can use constructor reference")
    void converterUsesConstructorReference() {
      Converter<String, StringBuilder> toBuilder = StringBuilder::new;
      StringBuilder sb = toBuilder.convert("hello");
      assertThat(sb.toString()).isEqualTo("hello");
    }
  }
}
