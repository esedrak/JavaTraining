package com.javatraining.basics.assertj;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Comprehensive test class showcasing AssertJ's fluent assertion API. */
@DisplayName("AssertJ Feature Showcase")
class AssertJTest {

  private BankAccount account;

  @BeforeEach
  void setUp() {
    account = new BankAccount("ACC-001", "Alice", 500.0);
  }

  // ── String assertions ────────────────────────────────────────────────────────

  @Nested
  @DisplayName("String Assertions")
  class StringAssertions {

    @Test
    @DisplayName("string assertions demonstrate fluent chaining")
    void stringAssertions() {
      String value = "Hello, World!";
      assertThat(value)
          .isEqualTo("Hello, World!")
          .contains("World")
          .startsWith("Hello")
          .endsWith("!")
          .hasSize(13)
          .isNotBlank();
    }

    @Test
    @DisplayName("string case and pattern assertions")
    void stringCaseAndPattern() {
      assertThat("  Java  ".trim()).isEqualToIgnoringCase("java");
      assertThat("training").containsIgnoringCase("TRAIN");
    }
  }

  // ── Number assertions ────────────────────────────────────────────────────────

  @Nested
  @DisplayName("Number Assertions")
  class NumberAssertions {

    @Test
    @DisplayName("integer range assertions")
    void integerRangeAssertions() {
      int value = 42;
      assertThat(value).isGreaterThan(10).isLessThan(100).isPositive().isBetween(40, 50);
    }

    @Test
    @DisplayName("double assertions with offset")
    void doubleAssertionsWithOffset() {
      assertThat(account.getBalance()).isEqualTo(500.0).isPositive().isGreaterThan(0.0);
    }
  }

  // ── Collection assertions ────────────────────────────────────────────────────

  @Nested
  @DisplayName("Collection Assertions")
  class CollectionAssertions {

    @Test
    @DisplayName("list assertions demonstrate rich collection API")
    void listAssertions() {
      List<String> fruits = List.of("apple", "banana", "cherry", "date");
      assertThat(fruits)
          .hasSize(4)
          .contains("banana", "cherry")
          .containsExactly("apple", "banana", "cherry", "date")
          .doesNotContain("mango");
    }

    @Test
    @DisplayName("filteredOn narrows list elements")
    void filteredOnNarrowsListElements() {
      List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8);
      assertThat(numbers).filteredOn(n -> n % 2 == 0).containsExactly(2, 4, 6, 8).hasSize(4);
    }

    @Test
    @DisplayName("extracting maps elements for assertion")
    void extractingMapsElements() {
      List<BankAccount> accounts =
          List.of(
              new BankAccount("1", "Alice", 100),
              new BankAccount("2", "Bob", 200),
              new BankAccount("3", "Carol", 300));
      assertThat(accounts)
          .extracting(BankAccount::getOwner)
          .containsExactly("Alice", "Bob", "Carol");
    }
  }

  // ── Map assertions ───────────────────────────────────────────────────────────

  @Nested
  @DisplayName("Map Assertions")
  class MapAssertions {

    @Test
    @DisplayName("map assertions for keys and entries")
    void mapAssertions() {
      Map<String, Integer> scores = Map.of("Alice", 95, "Bob", 87, "Carol", 92);
      assertThat(scores)
          .containsKey("Alice")
          .containsEntry("Bob", 87)
          .hasSize(3)
          .doesNotContainKey("Dave");
    }
  }

  // ── Optional assertions ──────────────────────────────────────────────────────

  @Nested
  @DisplayName("Optional Assertions")
  class OptionalAssertions {

    @Test
    @DisplayName("present optional assertions")
    void presentOptionalAssertions() {
      Optional<String> opt = Optional.of("hello");
      assertThat(opt).isPresent().contains("hello");
    }

    @Test
    @DisplayName("empty optional assertion")
    void emptyOptionalAssertion() {
      Optional<String> empty = Optional.empty();
      assertThat(empty).isEmpty();
    }
  }

  // ── Exception assertions ─────────────────────────────────────────────────────

  @Nested
  @DisplayName("Exception Assertions")
  class ExceptionAssertions {

    @Test
    @DisplayName("assertThatThrownBy captures exception details")
    void assertThatThrownByCaptures() {
      assertThatThrownBy(() -> account.deposit(-1))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("amount");
    }

    @Test
    @DisplayName("assertThatCode verifies no exception is thrown")
    void assertThatCodeDoesNotThrow() {
      assertThatCode(() -> account.deposit(100)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("withdraw with insufficient funds throws")
    void withdrawInsufficientFundsThrows() {
      assertThatThrownBy(() -> account.withdraw(9999.0))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Insufficient balance");
    }
  }

  // ── BankAccount object assertions ────────────────────────────────────────────

  @Nested
  @DisplayName("Object Assertions")
  class ObjectAssertions {

    @Test
    @DisplayName("bank account state after deposit")
    void bankAccountStateAfterDeposit() {
      account.deposit(200.0);
      assertThat(account.getBalance()).isEqualTo(700.0);
      assertThat(account.isOverdrawn()).isFalse();
    }
  }

  // ── SoftAssertions ───────────────────────────────────────────────────────────

  @Test
  @DisplayName("SoftAssertions reports all failures at once")
  void softAssertionsReportAllFailures() {
    account.deposit(100.0); // balance is now 600

    assertSoftly(
        softly -> {
          softly.assertThat(account.getId()).isEqualTo("ACC-001");
          softly.assertThat(account.getOwner()).isEqualTo("Alice");
          softly.assertThat(account.getBalance()).isEqualTo(600.0);
          softly.assertThat(account.isOverdrawn()).isFalse();
        });
  }
}
