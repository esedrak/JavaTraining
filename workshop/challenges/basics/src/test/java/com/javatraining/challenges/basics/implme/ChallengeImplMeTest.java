package com.javatraining.challenges.basics.implme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.javatraining.challenges.basics.implme.Stubs.AsyncPipeline;
import com.javatraining.challenges.basics.implme.Stubs.BankTransaction;
import com.javatraining.challenges.basics.implme.Stubs.CorrelationIdFilter;
import com.javatraining.challenges.basics.implme.Stubs.StreamsChallenge;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the ImplMe challenges.
 *
 * <p>All tests are {@link Disabled} until you implement the stubs. Remove the {@code @Disabled}
 * annotation from each test (or its enclosing class) once you have completed the implementation.
 *
 * <p>Run just these tests:
 *
 * <pre>./gradlew :workshop:challenges:basics:test --tests "*.ChallengeImplMeTest"</pre>
 */
@DisplayName("ImplMe Challenges")
class ChallengeImplMeTest {

  // ── Challenge 1: Async pipeline ───────────────────────────────────────────────

  @Nested
  @DisplayName("Challenge 1 – AsyncPipeline")
  @Disabled("Not yet implemented — remove @Disabled when complete")
  class AsyncPipelineTests {

    @Test
    @DisplayName("processConcurrently returns results in input order")
    void processConcurrently_resultsInOrder() {
      List<Integer> items = List.of(1, 2, 3, 4, 5);

      List<Integer> results =
          AsyncPipeline.processConcurrently(
              items, i -> CompletableFuture.completedFuture(i * 10), 2);

      assertThat(results).containsExactly(10, 20, 30, 40, 50);
    }

    @Test
    @DisplayName("processConcurrently never exceeds maxConcurrency in-flight")
    void processConcurrently_respectsConcurrencyLimit() throws InterruptedException {
      int maxConcurrency = 2;
      AtomicInteger inFlight = new AtomicInteger();
      AtomicInteger peakInFlight = new AtomicInteger();

      List<Integer> items = List.of(1, 2, 3, 4, 5, 6);
      AsyncPipeline.processConcurrently(
          items,
          i ->
              CompletableFuture.supplyAsync(
                  () -> {
                    int current = inFlight.incrementAndGet();
                    peakInFlight.updateAndGet(peak -> Math.max(peak, current));
                    try {
                      Thread.sleep(20);
                    } catch (InterruptedException e) {
                      Thread.currentThread().interrupt();
                    }
                    inFlight.decrementAndGet();
                    return i;
                  }),
          maxConcurrency);

      assertThat(peakInFlight.get())
          .as("peak in-flight tasks must not exceed maxConcurrency")
          .isLessThanOrEqualTo(maxConcurrency);
    }

    @Test
    @DisplayName("retry succeeds on the first attempt")
    void retry_succeedsOnFirstAttempt() throws Exception {
      String result = AsyncPipeline.retry(() -> "ok", 3, 10);

      assertThat(result).isEqualTo("ok");
    }

    @Test
    @DisplayName("retry succeeds after transient failures")
    void retry_succeedsAfterTransientFailures() throws Exception {
      AtomicInteger attempts = new AtomicInteger();

      String result =
          AsyncPipeline.retry(
              () -> {
                if (attempts.incrementAndGet() < 3) {
                  throw new RuntimeException("transient failure");
                }
                return "recovered";
              },
              5,
              1); // 1 ms delay keeps the test fast

      assertThat(result).isEqualTo("recovered");
      assertThat(attempts.get()).isEqualTo(3);
    }

    @Test
    @DisplayName("retry rethrows after exhausting all attempts")
    void retry_rethrowsAfterAllAttempts() {
      assertThatThrownBy(
              () ->
                  AsyncPipeline.retry(
                      () -> {
                        throw new IllegalStateException("always fails");
                      },
                      3,
                      1))
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("always fails");
    }
  }

  // ── Challenge 2: Correlation-ID filter ───────────────────────────────────────

  @Nested
  @DisplayName("Challenge 2 – CorrelationIdFilter")
  @Disabled("Not yet implemented — remove @Disabled when complete")
  class CorrelationIdFilterTests {

    @Test
    @DisplayName("propagates existing correlation ID from request to response")
    void propagatesExistingCorrelationId() throws Exception {
      CorrelationIdFilter filter = new CorrelationIdFilter();
      String existingId = UUID.randomUUID().toString();

      HttpServletRequest request = mock(HttpServletRequest.class);
      HttpServletResponse response = mock(HttpServletResponse.class);
      FilterChain chain = mock(FilterChain.class);

      when(request.getHeader(CorrelationIdFilter.HEADER_NAME)).thenReturn(existingId);

      filter.doFilterInternal(request, response, chain);

      verify(response).setHeader(CorrelationIdFilter.HEADER_NAME, existingId);
      verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("generates a new correlation ID when none is present")
    void generatesNewCorrelationIdWhenAbsent() throws Exception {
      CorrelationIdFilter filter = new CorrelationIdFilter();

      HttpServletRequest request = mock(HttpServletRequest.class);
      HttpServletResponse response = mock(HttpServletResponse.class);
      FilterChain chain = mock(FilterChain.class);

      when(request.getHeader(CorrelationIdFilter.HEADER_NAME)).thenReturn(null);

      filter.doFilterInternal(request, response, chain);

      // Capture what was actually set and assert it is a valid UUID
      org.mockito.ArgumentCaptor<String> captor =
          org.mockito.ArgumentCaptor.forClass(String.class);
      verify(response).setHeader(org.mockito.ArgumentMatchers.eq(CorrelationIdFilter.HEADER_NAME), captor.capture());

      String generatedId = captor.getValue();
      assertThat(generatedId).isNotBlank();
      // Must be a parseable UUID
      assertThat(UUID.fromString(generatedId)).isNotNull();

      verify(chain).doFilter(request, response);
    }
  }

  // ── Challenge 3: Streams banking queries ─────────────────────────────────────

  @Nested
  @DisplayName("Challenge 3 – StreamsChallenge")
  @Disabled("Not yet implemented — remove @Disabled when complete")
  class StreamsChallengeTests {

    private final List<BankTransaction> transactions =
        List.of(
            new BankTransaction("Alice", 500.0, "Deposit"),
            new BankTransaction("Bob", -200.0, "Withdrawal"),
            new BankTransaction("Alice", -100.0, "Withdrawal"),
            new BankTransaction("Charlie", 1000.0, "Deposit"),
            new BankTransaction("Bob", 300.0, "Deposit"));

    @Test
    @DisplayName("getWithdrawals returns only negative-amount transactions")
    void getWithdrawals_returnsNegativeAmounts() {
      List<BankTransaction> withdrawals = StreamsChallenge.getWithdrawals(transactions);

      assertThat(withdrawals).hasSize(2);
      assertThat(withdrawals).allMatch(t -> t.amount() < 0);
    }

    @Test
    @DisplayName("totalByOwner groups and sums by owner")
    void totalByOwner_groupsAndSums() {
      Map<String, Double> totals = StreamsChallenge.totalByOwner(transactions);

      assertThat(totals).hasSize(3);
      assertThat(totals.get("Alice")).isEqualTo(400.0);
      assertThat(totals.get("Bob")).isEqualTo(100.0);
      assertThat(totals.get("Charlie")).isEqualTo(1000.0);
    }

    @Test
    @DisplayName("largestByAbsoluteAmount finds the transaction with the greatest absolute value")
    void largestByAbsoluteAmount_findsMax() {
      BankTransaction largest = StreamsChallenge.largestByAbsoluteAmount(transactions);

      assertThat(largest).isNotNull();
      assertThat(largest.owner()).isEqualTo("Charlie");
      assertThat(largest.amount()).isEqualTo(1000.0);
    }

    @Test
    @DisplayName("largestByAbsoluteAmount returns null for an empty list")
    void largestByAbsoluteAmount_returnsNullWhenEmpty() {
      assertThat(StreamsChallenge.largestByAbsoluteAmount(List.of())).isNull();
    }
  }
}
