package com.javatraining.basics.context;

import java.util.UUID;

/**
 * Demonstrates {@link ThreadLocal} for per-thread correlation IDs.
 *
 * <p>Each thread has its own isolated copy of the correlation ID. Storing a value in one thread
 * does not affect any other thread. Always call {@link #clear()} at the end of a request to prevent
 * memory leaks in thread-pool environments.
 */
public class CorrelationContext {

  private static final ThreadLocal<String> correlationId = new ThreadLocal<>();

  /** Sets the correlation ID for the current thread. */
  public static void set(String id) {
    correlationId.set(id);
  }

  /** Returns the correlation ID for the current thread, or {@code null} if none is set. */
  public static String get() {
    return correlationId.get();
  }

  /** Removes the correlation ID for the current thread (prevents memory leaks in thread pools). */
  public static void clear() {
    correlationId.remove();
  }

  /**
   * Returns the existing correlation ID, or generates and stores a new UUID if none is set.
   *
   * @return non-null correlation ID
   */
  public static String getOrGenerate() {
    String existing = correlationId.get();
    if (existing != null) {
      return existing;
    }
    String generated = UUID.randomUUID().toString();
    correlationId.set(generated);
    return generated;
  }
}
