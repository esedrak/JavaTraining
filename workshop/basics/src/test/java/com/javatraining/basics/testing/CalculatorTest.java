package com.javatraining.basics.testing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/** Comprehensive JUnit 5 test class demonstrating core testing features. */
@DisplayName("Calculator Tests")
class CalculatorTest {

  private Calculator calculator;
  private MathUtils mathUtils;

  @BeforeEach
  void setUp() {
    calculator = new Calculator();
    mathUtils = new MathUtils();
  }

  @AfterEach
  void tearDown() {
    // Lifecycle hook — resources would be cleaned up here
    calculator = null;
    mathUtils = null;
  }

  // ── Basic @Test ──────────────────────────────────────────────────────────────

  @Test
  @DisplayName("add returns correct sum")
  void addReturnsCorrectSum() {
    assertThat(calculator.add(2, 3)).isEqualTo(5);
  }

  @Test
  @DisplayName("subtract returns correct difference")
  void subtractReturnsCorrectDifference() {
    assertThat(calculator.subtract(10, 4)).isEqualTo(6);
  }

  // ── @CsvSource parameterized add ────────────────────────────────────────────

  @ParameterizedTest(name = "{0} + {1} = {2}")
  @CsvSource({"1,1,2", "2,3,5", "10,20,30"})
  @DisplayName("add with CSV source")
  void addWithCsvSource(int a, int b, int expected) {
    assertThat(calculator.add(a, b)).isEqualTo(expected);
  }

  // ── @Nested division tests ───────────────────────────────────────────────────

  @Nested
  @DisplayName("Division Tests")
  class DivisionTests {

    @Test
    @DisplayName("divide returns correct quotient")
    void divideReturnsCorrectQuotient() {
      assertThat(calculator.divide(10.0, 2.0)).isEqualTo(5.0);
    }

    @Test
    @DisplayName("divide by zero throws ArithmeticException")
    void divideByZeroThrowsArithmeticException() {
      assertThrows(ArithmeticException.class, () -> calculator.divide(1, 0));
    }

    @Test
    @DisplayName("divide negative numbers")
    void divideNegativeNumbers() {
      assertThat(calculator.divide(-9.0, 3.0)).isEqualTo(-3.0);
    }
  }

  // ── @ValueSource prime tests ─────────────────────────────────────────────────

  @ParameterizedTest(name = "{0} is prime")
  @ValueSource(ints = {2, 3, 5, 7, 11})
  @DisplayName("isPrime returns true for known primes")
  void isPrimeReturnsTrueForKnownPrimes(int prime) {
    assertThat(calculator.isPrime(prime)).isTrue();
  }

  @ParameterizedTest(name = "{0} is not prime")
  @ValueSource(ints = {0, 1, 4, 6, 9, 15})
  @DisplayName("isPrime returns false for non-primes")
  void isPrimeReturnsFalseForNonPrimes(int nonPrime) {
    assertThat(calculator.isPrime(nonPrime)).isFalse();
  }

  // ── @MethodSource Fibonacci ──────────────────────────────────────────────────

  static Stream<org.junit.jupiter.params.provider.Arguments> fibonacciTestData() {
    return Stream.of(
        org.junit.jupiter.params.provider.Arguments.of(0, List.of()),
        org.junit.jupiter.params.provider.Arguments.of(1, List.of(0)),
        org.junit.jupiter.params.provider.Arguments.of(5, List.of(0, 1, 1, 2, 3)),
        org.junit.jupiter.params.provider.Arguments.of(7, List.of(0, 1, 1, 2, 3, 5, 8)));
  }

  @ParameterizedTest(name = "fibonacciSequence({0}) = {1}")
  @MethodSource("fibonacciTestData")
  @DisplayName("fibonacciSequence returns correct sequence")
  void fibonacciSequenceReturnsCorrectSequence(int n, List<Integer> expected) {
    assertThat(calculator.fibonacciSequence(n)).isEqualTo(expected);
  }

  // ── assertAll multiple assertions ────────────────────────────────────────────

  @Test
  @DisplayName("assertAll groups multiple assertions")
  void assertAllGroupsMultipleAssertions() {
    assertAll(
        "calculator operations",
        () -> assertThat(calculator.add(1, 1)).isEqualTo(2),
        () -> assertThat(calculator.subtract(5, 3)).isEqualTo(2),
        () -> assertThat(calculator.divide(8.0, 4.0)).isEqualTo(2.0),
        () -> assertThat(calculator.isPrime(7)).isTrue());
  }

  // ── MathUtils tests ──────────────────────────────────────────────────────────

  @Nested
  @DisplayName("MathUtils Tests")
  class MathUtilsTests {

    @Test
    @DisplayName("factorial of 5 is 120")
    void factorialOf5Is120() {
      assertThat(mathUtils.factorial(5)).isEqualTo(120);
    }

    @Test
    @DisplayName("factorial of 0 is 1")
    void factorialOf0Is1() {
      assertThat(mathUtils.factorial(0)).isEqualTo(1);
    }

    @Test
    @DisplayName("factorial of negative throws IllegalArgumentException")
    void factorialOfNegativeThrowsIllegalArgumentException() {
      assertThrows(IllegalArgumentException.class, () -> mathUtils.factorial(-1));
    }

    @ParameterizedTest(name = "\"{0}\" is a palindrome")
    @ValueSource(strings = {"racecar", "Madam", "level", "A"})
    @DisplayName("isPalindrome returns true for palindromes")
    void isPalindromeReturnsTrueForPalindromes(String s) {
      assertThat(mathUtils.isPalindrome(s)).isTrue();
    }

    @ParameterizedTest(name = "\"{0}\" is not a palindrome")
    @ValueSource(strings = {"hello", "world", "Java"})
    @DisplayName("isPalindrome returns false for non-palindromes")
    void isPalindromeReturnsFalseForNonPalindromes(String s) {
      assertThat(mathUtils.isPalindrome(s)).isFalse();
    }
  }
}
