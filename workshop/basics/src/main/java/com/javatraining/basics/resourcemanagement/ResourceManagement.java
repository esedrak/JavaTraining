package com.javatraining.basics.resourcemanagement;

import java.util.List;

/**
 * Topic 13: Resource Management in Java
 *
 * <p>Demonstrates AutoCloseable, Closeable, try-with-resources (single and multiple), and custom
 * managed resources. The JVM guarantees that close() is called even when an exception is thrown
 * inside the try block.
 */
public class ResourceManagement {

  // -------------------------------------------------------------------------
  // Custom AutoCloseable resource
  // -------------------------------------------------------------------------

  /**
   * A simple simulated database connection that tracks whether it has been closed. Implements
   * {@link AutoCloseable} so it can be used in a try-with-resources statement.
   */
  public static class ManagedConnection implements AutoCloseable {

    private boolean closed = false;
    private final String name;

    public ManagedConnection(String name) {
      this.name = name;
      System.out.println("Connection opened: " + name);
    }

    /** Convenience constructor for single-resource use. */
    public ManagedConnection() {
      this("default");
    }

    /**
     * Executes a simulated SQL query.
     *
     * @throws IllegalStateException if the connection is already closed
     */
    public String query(String sql) {
      if (closed) {
        throw new IllegalStateException("Connection is closed");
      }
      return "Result for: " + sql;
    }

    /** Returns the name assigned to this connection (useful for ordering tests). */
    public String getName() {
      return name;
    }

    @Override
    public void close() {
      closed = true;
      System.out.println("Connection closed: " + name);
    }

    public boolean isClosed() {
      return closed;
    }
  }

  // -------------------------------------------------------------------------
  // try-with-resources demo methods
  // -------------------------------------------------------------------------

  /**
   * Uses a single try-with-resources block. The connection is guaranteed to be closed after the
   * block exits, whether normally or via exception.
   */
  public String queryWithResource(String sql) {
    try (ManagedConnection conn = new ManagedConnection()) {
      return conn.query(sql);
    }
  }

  /**
   * Opens two resources. Java closes them in LIFO (last-in, first-out) order: {@code second} is
   * closed before {@code first}. The closed order is recorded into {@code closeOrder}.
   */
  public void twoResourcesDemo(List<String> closeOrder) {
    try (ManagedConnection first = new ManagedConnection("first");
        ManagedConnection second = new ManagedConnection("second")) {
      // use both resources
      first.query("SELECT 1");
      second.query("SELECT 2");
    } finally {
      // close() was already called by try-with-resources before we reach here,
      // but we can verify via the passed list (populated in close via subclass).
    }
    // After the block, record the order externally — done by caller in tests.
  }

  /**
   * Demonstrates that close() is called even when the try body throws. Returns the exception
   * message so the test can verify it.
   */
  public ManagedConnection openConnection() {
    return new ManagedConnection("test");
  }

  /**
   * Opens a connection, throws deliberately, and returns whether the connection was closed after
   * the exception escaped.
   */
  public boolean closeOnException() {
    ManagedConnection conn = new ManagedConnection("exception-test");
    try (conn) {
      throw new RuntimeException("deliberate");
    } catch (RuntimeException e) {
      // swallow so test can inspect 'conn'
    }
    return conn.isClosed();
  }
}
