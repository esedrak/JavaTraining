package com.javatraining.basics.benchmarking;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests that verify the correctness of the methods used in benchmarking demonstrations. */
@DisplayName("Benchmarking Demo Tests")
class BenchmarkingTest {

  private BenchmarkingDemo demo;

  @BeforeEach
  void setUp() {
    demo = new BenchmarkingDemo();
  }

  @Test
  @DisplayName("stringConcatLoop(5) produces '01234'")
  void stringConcatLoopProducesCorrectResult() {
    assertThat(demo.stringConcatLoop(5)).isEqualTo("01234");
  }

  @Test
  @DisplayName("stringBuilderLoop(5) produces '01234'")
  void stringBuilderLoopProducesCorrectResult() {
    assertThat(demo.stringBuilderLoop(5)).isEqualTo("01234");
  }

  @Test
  @DisplayName("concat and StringBuilder produce the same result for small inputs")
  void bothMethodsProduceSameResultForSmallInputs() {
    for (int n = 0; n <= 10; n++) {
      assertThat(demo.stringBuilderLoop(n)).as("n = %d", n).isEqualTo(demo.stringConcatLoop(n));
    }
  }

  @Test
  @DisplayName("arrayListBatch(5) contains elements 0..4")
  void arrayListBatchContainsCorrectElements() {
    List<Integer> result = demo.arrayListBatch(5);
    assertThat(result).hasSize(5).containsExactly(0, 1, 2, 3, 4);
  }

  @Test
  @DisplayName("primitiveArraySum(5) returns 0+1+2+3+4 = 10")
  void primitiveArraySumReturnsCorrectSum() {
    assertThat(demo.primitiveArraySum(5)).isEqualTo(10);
  }

  @Test
  @DisplayName("StringBuilder is faster than concat for large input (correctness sanity)")
  void stringBuilderProducesSameResultForLargeInput() {
    // We don't measure wall-clock time in unit tests; we just verify correctness.
    // The performance difference is demonstrated by JMH benchmarks.
    String concatResult = demo.stringConcatLoop(100);
    String sbResult = demo.stringBuilderLoop(100);
    assertThat(sbResult).isEqualTo(concatResult);
  }
}
