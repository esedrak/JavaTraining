package com.javatraining.basics.context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for ThreadLocal, InheritableThreadLocal, and timeout pattern demonstrations. */
@DisplayName("Context (ThreadLocal / Timeout) Tests")
class ContextTest {

  @AfterEach
  void cleanUp() {
    CorrelationContext.clear();
    RequestContext.clear();
  }

  // ── CorrelationContext ───────────────────────────────────────────────────────

  @Test
  @DisplayName("set and get round-trip returns stored value")
  void setAndGetRoundTrip() {
    CorrelationContext.set("abc");
    assertThat(CorrelationContext.get()).isEqualTo("abc");
  }

  @Test
  @DisplayName("clear removes the stored value")
  void clearRemovesValue() {
    CorrelationContext.set("xyz");
    CorrelationContext.clear();
    assertThat(CorrelationContext.get()).isNull();
  }

  @Test
  @DisplayName("getOrGenerate produces a UUID-format string when empty")
  void getOrGenerateProducesUuid() {
    CorrelationContext.clear();
    String generated = CorrelationContext.getOrGenerate();
    assertThat(generated).isNotBlank().matches("[0-9a-f\\-]{36}");
  }

  @Test
  @DisplayName("getOrGenerate returns existing value when already set")
  void getOrGenerateReturnsExistingValue() {
    CorrelationContext.set("my-correlation-id");
    assertThat(CorrelationContext.getOrGenerate()).isEqualTo("my-correlation-id");
  }

  @Test
  @DisplayName("ThreadLocal isolates values across threads")
  void threadLocalIsolatesValues() throws InterruptedException {
    CorrelationContext.set("main-thread");
    AtomicReference<String> childValue = new AtomicReference<>();
    CountDownLatch latch = new CountDownLatch(1);

    Thread child =
        Thread.ofPlatform()
            .start(
                () -> {
                  // New platform thread — ThreadLocal should be null here
                  childValue.set(CorrelationContext.get());
                  latch.countDown();
                });

    latch.await(3, TimeUnit.SECONDS);
    assertThat(childValue.get()).isNull();
    assertThat(CorrelationContext.get()).isEqualTo("main-thread");
  }

  // ── RequestContext (InheritableThreadLocal) ──────────────────────────────────

  @Test
  @DisplayName("InheritableThreadLocal propagates value to child thread")
  void inheritableThreadLocalPropagatesValue() throws InterruptedException {
    RequestContext.setUser("user-42");
    AtomicReference<String> childValue = new AtomicReference<>();
    CountDownLatch latch = new CountDownLatch(1);

    Thread child =
        new Thread(
            () -> {
              childValue.set(RequestContext.getUser());
              latch.countDown();
            });
    child.start();

    latch.await(3, TimeUnit.SECONDS);
    assertThat(childValue.get()).isEqualTo("user-42");
  }

  // ── TimeoutDemo ──────────────────────────────────────────────────────────────

  @Test
  @DisplayName("withTimeout completes fast task successfully")
  void withTimeoutCompletesSuccessfully() {
    TimeoutDemo td = new TimeoutDemo();
    String result = td.withTimeout(() -> "done", 1000);
    assertThat(result).isEqualTo("done");
  }

  @Test
  @DisplayName("withTimeout throws RuntimeException when task exceeds deadline")
  void withTimeoutThrowsWhenExceedsDeadline() {
    TimeoutDemo td = new TimeoutDemo();

    assertThatThrownBy(
            () ->
                td.withTimeout(
                    () -> {
                      Thread.sleep(2000);
                      return "never";
                    },
                    100))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("timed out");
  }
}
