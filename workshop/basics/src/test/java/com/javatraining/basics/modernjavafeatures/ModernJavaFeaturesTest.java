package com.javatraining.basics.modernjavafeatures;

import static org.assertj.core.api.Assertions.assertThat;

import com.javatraining.basics.modernjavafeatures.ModernJavaFeatures.Coordinate;
import com.javatraining.basics.modernjavafeatures.ModernJavaFeatures.Failure;
import com.javatraining.basics.modernjavafeatures.ModernJavaFeatures.Result;
import com.javatraining.basics.modernjavafeatures.ModernJavaFeatures.Success;
import java.time.DayOfWeek;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModernJavaFeaturesTest {

  private ModernJavaFeatures features;

  @BeforeEach
  void setUp() {
    features = new ModernJavaFeatures();
  }

  // ---- Records ----

  @Test
  void coordinate_equalityBasedOnValues() {
    Coordinate c1 = new Coordinate(37.7749, -122.4194);
    Coordinate c2 = new Coordinate(37.7749, -122.4194);
    assertThat(c1).isEqualTo(c2);
  }

  @Test
  void coordinate_accessorsReturnValues() {
    Coordinate c = new Coordinate(51.5074, -0.1278);
    assertThat(c.lat()).isEqualTo(51.5074);
    assertThat(c.lon()).isEqualTo(-0.1278);
  }

  // ---- Sealed classes + switch ----

  @Test
  void describe_success_returnsSuccessString() {
    Result<String> result = new Success<>("all good");
    assertThat(features.describe(result)).isEqualTo("Success: all good");
  }

  @Test
  void describe_failure_returnsFailureString() {
    Result<String> result = new Failure<>("something went wrong");
    assertThat(features.describe(result)).isEqualTo("Failure: something went wrong");
  }

  @Test
  void successIsInstanceOfResult() {
    assertThat(new Success<>(42)).isInstanceOf(Result.class);
  }

  @Test
  void failureIsInstanceOfResult() {
    assertThat(new Failure<>("err")).isInstanceOf(Result.class);
  }

  // ---- Switch expressions ----

  @Test
  void dayType_weekday_returnsWeekday() {
    assertThat(features.dayType(DayOfWeek.MONDAY)).isEqualTo("Weekday");
    assertThat(features.dayType(DayOfWeek.FRIDAY)).isEqualTo("Weekday");
  }

  @Test
  void dayType_weekend_returnsWeekend() {
    assertThat(features.dayType(DayOfWeek.SATURDAY)).isEqualTo("Weekend");
    assertThat(features.dayType(DayOfWeek.SUNDAY)).isEqualTo("Weekend");
  }

  // ---- Text blocks ----

  @Test
  void jsonTextBlock_containsExpectedContent() {
    String json = features.jsonTextBlock();
    assertThat(json).contains("JavaTraining").contains("version").contains("21");
  }

  @Test
  void sqlTextBlock_containsExpectedContent() {
    String sql = features.sqlTextBlock();
    assertThat(sql).contains("SELECT").contains("FROM users");
  }

  // ---- Pattern matching switch ----

  @Test
  void format_string_containsStringLabel() {
    assertThat(features.format("hello")).contains("String");
  }

  @Test
  void format_integer_containsValue() {
    assertThat(features.format(42)).contains("42");
  }

  @Test
  void format_double_containsDoubleLabel() {
    assertThat(features.format(3.14)).contains("Double");
  }

  @Test
  void format_null_returnsNullString() {
    assertThat(features.format(null)).isEqualTo("null");
  }

  // ---- var demo ----

  @Test
  void varDemo_returnsExpectedString() {
    assertThat(features.varDemo()).isEqualTo("Hello-42-1");
  }

  // ---- Unnamed variable / catch _ ----

  @Test
  void ignoredExceptionDemo_validNumber_returnsString() {
    assertThat(features.ignoredExceptionDemo("123")).isEqualTo("123");
  }

  @Test
  void ignoredExceptionDemo_invalidNumber_returnsNotANumber() {
    assertThat(features.ignoredExceptionDemo("abc")).isEqualTo("not-a-number");
  }
}
