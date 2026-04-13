package com.javatraining.basics.reactivestreams;

import java.time.Duration;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Topic 18: Reactive Streams with Project Reactor
 *
 * <p>Project Reactor provides two primary reactive types:
 *
 * <ul>
 *   <li>{@link Mono} — 0 or 1 item, then complete (or error)
 *   <li>{@link Flux} — 0 to N items, then complete (or error)
 * </ul>
 *
 * <p>Reactive streams are <em>lazy</em>: nothing happens until a subscriber subscribes. Operators
 * are chained declaratively and executed when the pipeline is subscribed to.
 */
public class ReactiveStreams {

  /** Emits integers from 1 to {@code count} (inclusive). */
  public Flux<Integer> generateNumbers(int count) {
    return Flux.range(1, count);
  }

  /**
   * Converts a list of strings to a {@link Flux}, filters out blank/empty entries, then maps each
   * remaining string to upper-case.
   */
  public Flux<String> processStrings(List<String> input) {
    return Flux.fromIterable(input)
        .filter(s -> s != null && !s.isBlank())
        .map(String::toUpperCase);
  }

  /**
   * Wraps a simple computation in a {@link Mono}.
   *
   * <p>{@code Mono.fromCallable} defers execution until subscription and runs the callable on the
   * subscribing thread (unless a scheduler is specified).
   */
  public Mono<String> fetchData(String id) {
    return Mono.fromCallable(() -> "Data for " + id);
  }

  /**
   * Demonstrates back-pressure by limiting the request rate with {@code limitRate}. Subscribers
   * can request elements in smaller batches, preventing fast producers from overwhelming slow
   * consumers.
   */
  public Flux<Integer> backpressureDemo() {
    return Flux.range(1, 100).limitRate(10);
  }

  /**
   * Demonstrates {@code zipWith} — pairs elements from two Flux streams by position.
   *
   * @return a Flux of combined strings, e.g. "A-1", "B-2"
   */
  public Flux<String> zipWithDemo() {
    Flux<String> letters = Flux.just("A", "B", "C");
    Flux<Integer> numbers = Flux.just(1, 2, 3);
    return letters.zipWith(numbers, (l, n) -> l + "-" + n);
  }

  /**
   * Demonstrates {@code merge} — interleaves two Flux streams as elements become available. The
   * order may vary if the sources are asynchronous.
   */
  public Flux<String> mergeDemo() {
    Flux<String> first = Flux.just("A", "B");
    Flux<String> second = Flux.just("C", "D");
    return Flux.merge(first, second);
  }

  /**
   * Demonstrates {@code concat} — subscribes to sources sequentially. {@code second} is not
   * subscribed until {@code first} completes.
   */
  public Flux<String> concatDemo() {
    Flux<String> first = Flux.just("A", "B");
    Flux<String> second = Flux.just("C", "D");
    return Flux.concat(first, second);
  }

  /**
   * Demonstrates error handling with {@code onErrorReturn}. When the pipeline encounters an error,
   * it emits the fallback value instead of propagating the error signal.
   */
  public Flux<String> errorHandling() {
    return Flux.just("ok-1", "ok-2")
        .concatWith(Flux.error(new RuntimeException("simulated error")))
        .onErrorReturn("fallback");
  }

  /**
   * Demonstrates {@code onErrorResume} — switches to an alternative Flux on error. This is useful
   * for providing a richer fallback (e.g. cache) rather than a single default value.
   */
  public Flux<String> errorResume() {
    return Flux.error(new RuntimeException("upstream failure"))
        .cast(String.class)
        .onErrorResume(e -> Flux.just("recovered-1", "recovered-2"));
  }

  /**
   * Demonstrates a timed Flux with a virtual-time-compatible structure.
   * Uses delayElements so tests can use StepVerifier's virtual time.
   */
  public Flux<Long> timedFlux() {
    return Flux.interval(Duration.ofSeconds(1)).take(3);
  }
}
