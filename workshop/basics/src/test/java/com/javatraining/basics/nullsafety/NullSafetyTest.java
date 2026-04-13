package com.javatraining.basics.nullsafety;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class NullSafetyTest {

  @Test
  void optional_of_withValue_isPresent() {
    Optional<String> opt = Optional.of("hello");
    assertThat(opt.isPresent()).isTrue();
    assertThat(opt.get()).isEqualTo("hello");
  }

  @Test
  void optional_empty_isNotPresent() {
    Optional<String> opt = Optional.empty();
    assertThat(opt.isPresent()).isFalse();
    assertThat(opt.isEmpty()).isTrue();
  }

  @Test
  void optional_ofNullable_withNull_isNotPresent() {
    Optional<String> opt = Optional.ofNullable(null);
    assertThat(opt.isPresent()).isFalse();
  }

  @Test
  void requireNonNull_withNull_throwsNPEWithMessage() {
    assertThatThrownBy(() -> Objects.requireNonNull(null, "msg"))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("msg");
  }

  @Test
  void requireNonNull_withValue_returnsValue() {
    String result = NullSafety.requireNonNullDemo("hello");
    assertThat(result).isEqualTo("HELLO");
  }

  @Test
  void requireNonNull_withNull_throwsNPE() {
    assertThatThrownBy(() -> NullSafety.requireNonNullDemo(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("value must not be null");
  }

  @Test
  void findUserEmail_withNull_returnsEmpty() {
    Optional<String> result = NullSafety.findUserEmail(null);
    assertThat(result).isEmpty();
  }

  @Test
  void findUserEmail_withValidId_returnsEmail() {
    Optional<String> result = NullSafety.findUserEmail("user42");
    assertThat(result).isPresent();
    assertThat(result.get()).contains("user42");
  }

  @Test
  void getUpperOrDefault_withNull_returnsDefault() {
    String result = NullSafety.getUpperOrDefault(null, "default");
    assertThat(result).isEqualTo("default");
  }

  @Test
  void getUpperOrDefault_withValue_returnsUpperCase() {
    String result = NullSafety.getUpperOrDefault("hello", "default");
    assertThat(result).isEqualTo("HELLO");
  }

  @Test
  void isNullCheck_withNull_returnsTrue() {
    assertThat(NullSafety.isNullCheck(null)).isTrue();
  }

  @Test
  void isNullCheck_withValue_returnsFalse() {
    assertThat(NullSafety.isNullCheck("something")).isFalse();
  }

  @Test
  void isNonNullCheck_withValue_returnsTrue() {
    assertThat(NullSafety.isNonNullCheck("value")).isTrue();
  }

  @Test
  void chainedOptional_withBlank_returnsFallback() {
    assertThat(NullSafety.chainedOptional("   ")).isEqualTo("BLANK_OR_NULL");
    assertThat(NullSafety.chainedOptional(null)).isEqualTo("BLANK_OR_NULL");
  }

  @Test
  void chainedOptional_withValue_returnsUpperCase() {
    assertThat(NullSafety.chainedOptional("  hello  ")).isEqualTo("HELLO");
  }
}
