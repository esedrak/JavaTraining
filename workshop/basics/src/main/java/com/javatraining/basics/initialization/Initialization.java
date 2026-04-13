package com.javatraining.basics.initialization;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * Topic 22: Initialization in Java
 *
 * <p>Demonstrates four distinct initialization mechanisms:
 *
 * <ol>
 *   <li><strong>Static initializer block</strong> — runs once when the class is first loaded by the
 *       JVM, before any instance is created.
 *   <li><strong>Instance initializer block</strong> — runs every time a new instance is created,
 *       immediately before the constructor body.
 *   <li><strong>Lazy initialization with double-checked locking</strong> — defers object creation
 *       until first use; thread-safe via {@code volatile} + synchronized.
 *   <li><strong>{@code @PostConstruct}</strong> — Spring calls this method after the bean is fully
 *       constructed and all dependencies are injected.
 * </ol>
 */
@Component
public class Initialization {

  // -------------------------------------------------------------------------
  // @PostConstruct demo (Spring only — not exercised in unit tests)
  // -------------------------------------------------------------------------

  @PostConstruct
  public void init() {
    System.out.println("@PostConstruct called — bean is fully initialized");
  }

  // =========================================================================
  // DatabasePool — demonstrates all four init mechanisms in a single class
  // =========================================================================

  /**
   * A simplified, singleton connection pool.
   *
   * <p>Initialization order for the singleton instance:
   *
   * <ol>
   *   <li>Static initializer sets up class-level configuration.
   *   <li>Instance initializer captures the creation timestamp.
   *   <li>Constructor completes object construction.
   * </ol>
   */
  public static class DatabasePool {

    // ---- static state ----

    private static int instanceCount = 0;

    /** Configuration value set by the static initializer. */
    public static String staticConfig;

    /** Lazy singleton — volatile ensures visibility across threads. */
    private static volatile DatabasePool INSTANCE;

    // ---- static initializer — runs once at class-load time ----
    static {
      staticConfig = "jdbc:h2:mem:training";
      System.out.println("DatabasePool class loaded. Static config: " + staticConfig);
    }

    // ---- instance state ----

    private long creationTime;
    private final int id;

    // ---- instance initializer — runs before every constructor ----
    {
      creationTime = System.nanoTime();
      id = ++instanceCount;
      System.out.println("Instance initializer ran. Instance #" + id);
    }

    /** Private constructor: only accessible via {@link #getInstance()}. */
    private DatabasePool() {
      System.out.println("DatabasePool constructor called. id=" + id);
    }

    // ---- Lazy singleton with double-checked locking ----

    /**
     * Returns the singleton instance. Creates it on the first call using double-checked locking to
     * avoid unnecessary synchronization on subsequent calls.
     */
    public static DatabasePool getInstance() {
      if (INSTANCE == null) { // first check (no lock — fast path)
        synchronized (DatabasePool.class) {
          if (INSTANCE == null) { // second check (under lock — safe path)
            INSTANCE = new DatabasePool();
          }
        }
      }
      return INSTANCE;
    }

    /** Resets the singleton for test isolation — not for production use. */
    public static void resetForTesting() {
      INSTANCE = null;
    }

    public long getCreationTime() {
      return creationTime;
    }

    public int getId() {
      return id;
    }

    public static int getInstanceCount() {
      return instanceCount;
    }
  }
}
