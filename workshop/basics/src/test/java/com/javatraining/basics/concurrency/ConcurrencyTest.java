package com.javatraining.basics.concurrency;

import static org.assertj.core.api.Assertions.assertThat;

import com.javatraining.basics.concurrency.ConcurrencyDemo.SafeCounter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/** Tests for Java 21 concurrency features demonstrated in {@link ConcurrencyDemo}. */
@DisplayName("Concurrency Tests")
class ConcurrencyTest {

  private ConcurrencyDemo demo;

  @BeforeEach
  void setUp() {
    demo = new ConcurrencyDemo();
  }

  @Test
  @DisplayName("SafeCounter incremented 10000 times from 10 threads reaches 10000")
  @Timeout(10)
  void safeCounterIs10000AfterConcurrentIncrements() throws InterruptedException {
    SafeCounter counter = new SafeCounter();
    int threadCount = 10;
    int incrementsPerThread = 1000;
    CountDownLatch latch = new CountDownLatch(threadCount);
    ExecutorService pool = Executors.newFixedThreadPool(threadCount);

    for (int t = 0; t < threadCount; t++) {
      pool.submit(
          () -> {
            try {
              for (int i = 0; i < incrementsPerThread; i++) {
                counter.increment();
              }
            } finally {
              latch.countDown();
            }
          });
    }

    latch.await(5, TimeUnit.SECONDS);
    pool.shutdown();

    assertThat(counter.get()).isEqualTo(10000);
  }

  @Test
  @DisplayName("pipeline(\"  hello  \") returns \"Result: HELLO\"")
  @Timeout(5)
  void pipelineTrimsAndUppercases() throws Exception {
    String result = demo.pipeline("  hello  ").get(3, TimeUnit.SECONDS);
    assertThat(result).isEqualTo("Result: HELLO");
  }

  @Test
  @DisplayName("parallelProcess(1,2,3) contains 2, 4, 6")
  @Timeout(10)
  void parallelProcessDoublesEachElement() {
    List<Integer> result = demo.parallelProcess(List.of(1, 2, 3));

    assertThat(result).hasSize(3).contains(2, 4, 6);
  }

  @Test
  @DisplayName("Thread.ofVirtual().start() creates a virtual thread")
  @Timeout(5)
  void virtualThreadIsVirtual() throws InterruptedException {
    List<Boolean> results = new ArrayList<>();
    CountDownLatch latch = new CountDownLatch(1);

    Thread vt =
        Thread.ofVirtual()
            .start(
                () -> {
                  results.add(Thread.currentThread().isVirtual());
                  latch.countDown();
                });

    latch.await(3, TimeUnit.SECONDS);
    assertThat(vt.isVirtual()).isTrue();
    assertThat(results).containsExactly(true);
  }

  @Test
  @DisplayName("virtualThreadCounter counts correctly using many virtual threads")
  @Timeout(15)
  void virtualThreadCounterIsAccurate() throws InterruptedException {
    int result = demo.virtualThreadCounter(500);
    assertThat(result).isEqualTo(500);
  }

  @Test
  @DisplayName("SafeCounter.add adds the given amount")
  void safeCounterAddWorksCorrectly() {
    SafeCounter counter = new SafeCounter();
    counter.add(5);
    counter.add(3);
    assertThat(counter.get()).isEqualTo(8);
  }

  @Test
  @DisplayName("processWithLock doubles the value under a lock")
  void processWithLockDoublesValue() {
    assertThat(demo.processWithLock(7)).isEqualTo(14);
  }
}
