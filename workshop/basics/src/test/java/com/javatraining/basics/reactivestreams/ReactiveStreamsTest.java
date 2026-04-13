package com.javatraining.basics.reactivestreams;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class ReactiveStreamsTest {

  private ReactiveStreams reactiveStreams;

  @BeforeEach
  void setUp() {
    reactiveStreams = new ReactiveStreams();
  }

  @Test
  void generateNumbers_emitsExpectedSequenceAndCompletes() {
    StepVerifier.create(reactiveStreams.generateNumbers(3))
        .expectNext(1, 2, 3)
        .verifyComplete();
  }

  @Test
  void processStrings_filtersBlankAndUpperCases() {
    StepVerifier.create(reactiveStreams.processStrings(List.of("hello", "", "world")))
        .expectNext("HELLO", "WORLD")
        .verifyComplete();
  }

  @Test
  void processStrings_allBlank_completesEmpty() {
    StepVerifier.create(reactiveStreams.processStrings(List.of("", " ", "")))
        .verifyComplete();
  }

  @Test
  void fetchData_emitsExpectedStringAndCompletes() {
    StepVerifier.create(reactiveStreams.fetchData("123"))
        .expectNext("Data for 123")
        .verifyComplete();
  }

  @Test
  void errorHandling_emitsItemsThenFallbackOnError() {
    StepVerifier.create(reactiveStreams.errorHandling())
        .expectNext("ok-1", "ok-2")
        .expectNext("fallback")
        .verifyComplete();
  }

  @Test
  void errorResume_switchesToAlternativeFlux() {
    StepVerifier.create(reactiveStreams.errorResume())
        .expectNext("recovered-1", "recovered-2")
        .verifyComplete();
  }

  @Test
  void zipWithDemo_pairsCorrectly() {
    StepVerifier.create(reactiveStreams.zipWithDemo())
        .expectNext("A-1", "B-2", "C-3")
        .verifyComplete();
  }

  @Test
  void concatDemo_appendsSecondAfterFirst() {
    StepVerifier.create(reactiveStreams.concatDemo())
        .expectNext("A", "B", "C", "D")
        .verifyComplete();
  }

  @Test
  void backpressureDemo_emitsAllHundredElements() {
    StepVerifier.create(reactiveStreams.backpressureDemo())
        .expectNextCount(100)
        .verifyComplete();
  }

  @Test
  void generateNumbers_zero_completesImmediately() {
    StepVerifier.create(reactiveStreams.generateNumbers(0))
        .verifyComplete();
  }

  @Test
  void mergeDemo_containsAllElements() {
    List<String> items = reactiveStreams.mergeDemo().collectList().block();
    assertThat(items).containsExactlyInAnyOrder("A", "B", "C", "D");
  }
}
