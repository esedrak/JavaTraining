package com.javatraining.basics.context;

/**
 * Demonstrates {@link InheritableThreadLocal} for propagating user context to child threads.
 *
 * <p>Unlike {@link ThreadLocal}, an {@code InheritableThreadLocal} automatically copies its value
 * to any child thread created from the owning thread. This is useful for passing request-scoped
 * identity (e.g. authenticated user ID) into child threads spawned during request processing.
 *
 * <p><strong>Warning:</strong> {@code InheritableThreadLocal} does NOT propagate values to threads
 * obtained from a thread pool (since those are pre-existing threads, not newly created children).
 * For thread-pool scenarios, prefer structured concurrency or explicit context propagation.
 */
public class RequestContext {

  private static final InheritableThreadLocal<String> userId = new InheritableThreadLocal<>();

  /** Sets the user ID for the current thread and its future child threads. */
  public static void setUser(String id) {
    userId.set(id);
  }

  /** Returns the user ID visible to the current thread, or {@code null} if none is set. */
  public static String getUser() {
    return userId.get();
  }

  /** Removes the user ID from the current thread. */
  public static void clear() {
    userId.remove();
  }
}
