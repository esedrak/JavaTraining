package com.javatraining.basics.concurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Demonstrates Java 21 concurrency primitives:
 *
 * <ul>
 *   <li>Virtual threads ({@code Thread.ofVirtual()}) — lightweight threads managed by the JVM
 *   <li>{@link CompletableFuture} — asynchronous pipeline composition
 *   <li>{@link AtomicInteger} — non-blocking thread-safe counter
 *   <li>{@link ReentrantLock} — explicit locking
 *   <li>{@link CountDownLatch} — coordinate completion across threads
 * </ul>
 */
public class ConcurrencyDemo {

  // ── SafeCounter ──────────────────────────────────────────────────────────────

  /**
   * Thread-safe counter backed by an {@link AtomicInteger}.
   *
   * <p>Uses compare-and-swap (CAS) operations internally — no blocking, no synchronized keyword.
   */
  public static class SafeCounter {

    private final AtomicInteger count = new AtomicInteger(0);

    /** Increments the counter by one. */
    public void increment() {
      count.incrementAndGet();
    }

    /** Adds {@code n} to the counter. */
    public void add(int n) {
      count.addAndGet(n);
    }

    /** Returns the current counter value. */
    public int get() {
      return count.get();
    }
  }

  // ── parallelProcess ──────────────────────────────────────────────────────────

  /**
   * Processes each item in the list using a virtual thread per item and returns the doubled values.
   *
   * <p>Virtual threads (Java 21) are extremely cheap to create — millions can be active
   * simultaneously. They are suitable for I/O-bound work where classic platform threads would be
   * too heavy.
   *
   * @param items input list
   * @return list of doubled values (order may vary)
   */
  public List<Integer> parallelProcess(List<Integer> items) {
    List<Integer> results = Collections.synchronizedList(new ArrayList<>());
    CountDownLatch latch = new CountDownLatch(items.size());

    for (int item : items) {
      final int value = item;
      Thread.ofVirtual()
          .start(
              () -> {
                try {
                  results.add(value * 2);
                } finally {
                  latch.countDown();
                }
              });
    }

    try {
      latch.await(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("parallelProcess interrupted", e);
    }

    return results;
  }

  // ── pipeline ────────────────────────────────────────────────────────────────

  /**
   * Returns a {@link CompletableFuture} that trims, uppercases, and prefixes the input string.
   *
   * <p>The pipeline demonstrates:
   *
   * <ul>
   *   <li>{@code supplyAsync} — start the async computation
   *   <li>{@code thenApply} — transform the value synchronously
   *   <li>{@code exceptionally} — recover from errors
   * </ul>
   *
   * @param input input string (may have surrounding whitespace)
   * @return future completing with "Result: {TRIMMED_UPPER}"
   */
  public CompletableFuture<String> pipeline(String input) {
    return CompletableFuture.supplyAsync(() -> input.trim())
        .thenApply(String::toUpperCase)
        .thenApply(s -> "Result: " + s)
        .exceptionally(e -> "Error: " + e.getMessage());
  }

  // ── ReentrantLock demo ───────────────────────────────────────────────────────

  /**
   * Demonstrates {@link ReentrantLock} — always release in a {@code finally} block to avoid
   * deadlocks.
   *
   * @param value the value to "process" under the lock
   * @return value doubled
   */
  public int processWithLock(int value) {
    ReentrantLock lock = new ReentrantLock();
    lock.lock();
    try {
      return value * 2;
    } finally {
      lock.unlock();
    }
  }

  // ── Virtual thread stress demo ───────────────────────────────────────────────

  /**
   * Spawns {@code count} virtual threads, each incrementing a {@link SafeCounter}, and waits for
   * all of them to finish.
   *
   * @param count number of virtual threads to create
   * @return the final counter value (should equal {@code count})
   */
  public int virtualThreadCounter(int count) throws InterruptedException {
    SafeCounter counter = new SafeCounter();
    CountDownLatch latch = new CountDownLatch(count);
    Executor virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();

    for (int i = 0; i < count; i++) {
      virtualExecutor.execute(
          () -> {
            counter.increment();
            latch.countDown();
          });
    }

    latch.await(30, TimeUnit.SECONDS);
    return counter.get();
  }
}
