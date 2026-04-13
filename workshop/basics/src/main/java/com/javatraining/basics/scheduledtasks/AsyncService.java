package com.javatraining.basics.scheduledtasks;

import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Demonstrates Spring's {@code @Async} support.
 *
 * <p>{@code @Async} causes Spring to execute the method on a thread-pool thread rather than the
 * caller's thread. The method must return {@link CompletableFuture} (or {@link java.util.concurrent.Future})
 * so the caller can await the result or compose further operations.
 *
 * <p>Requires {@code @EnableAsync} on a configuration class to activate the proxy.
 */
@Service
public class AsyncService {

  /**
   * Simulates an I/O-bound operation by sleeping briefly, then returns a processed result.
   *
   * <p>In production code the sleep would be replaced by a real network or database call.
   *
   * @param input the raw input string
   * @return a {@link CompletableFuture} that completes with {@code "Processed: " + input}
   */
  @Async
  public CompletableFuture<String> processAsync(String input) throws InterruptedException {
    Thread.sleep(100);
    return CompletableFuture.completedFuture("Processed: " + input);
  }
}
