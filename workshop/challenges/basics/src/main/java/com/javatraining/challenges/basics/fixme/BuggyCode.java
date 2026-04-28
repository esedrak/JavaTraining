package com.javatraining.challenges.basics.fixme;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

// ── Challenge 1: Race condition ───────────────────────────────────────────────
// BUG: Multiple threads increment _count concurrently without synchronization.
// Fix: Use AtomicInteger, synchronized, or a lock.

public class BuggyCode {

  public static class RaceCounter {
    private int count;

    public void incrementMany(int times) throws InterruptedException {
      try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
        List<CompletableFuture<Void>> futures =
            IntStream.range(0, times)
                .mapToObj(
                    i ->
                        CompletableFuture.runAsync(
                            () -> count++, // BUG: not thread-safe — data race!
                            pool))
                .toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
      }
    }

    public int getValue() {
      return count;
    }
  }

  // ── Challenge 2: Virtual thread pinning ─────────────────────────────────────
  // BUG: CompletableFuture.join() blocks the carrier thread when called inside
  //      a virtual thread, pinning it and preventing other virtual threads from
  //      being scheduled on that carrier.
  // Fix: Use CompletableFuture.thenCompose() / thenApply() chains, or await
  //      the result asynchronously without blocking (use async composition).

  public static class PinningExample {
    public String getData() {
      return CompletableFuture.supplyAsync(
              () -> {
                String inner =
                    CompletableFuture.supplyAsync(
                            () -> "data") // BUG: .join() inside a virtual thread pins the carrier!
                        .join();
                return inner;
              },
              Executors.newVirtualThreadPerTaskExecutor())
          .join();
    }
  }

  // ── Challenge 3: NullPointerException ────────────────────────────────────────
  // BUG: getFirstName doesn't handle null or empty input.
  // Fix: Add null/empty checks, or return Optional<String>.

  public static class NullBug {
    public String getFirstName(List<String> names) {
      return names.get(0).toUpperCase(); // BUG: throws NPE if names is null or empty!
    }
  }

  // ── Challenge 4: Off-by-one ───────────────────────────────────────────────────
  // BUG: The last element is never doubled — loop stops one index too early.
  // Fix: Change the condition to i < values.length (remove the - 1).

  public static class OffByOne {
    public int[] doubleAll(int[] values) {
      int[] result = new int[values.length];
      for (int i = 0; i < values.length - 1; i++) { // BUG: should be i < values.length
        result[i] = values[i] * 2;
      }
      return result;
    }
  }

  // ── Challenge 5: Resource leak ────────────────────────────────────────────────
  // BUG: FileInputStream is opened but never closed, leaking a file descriptor.
  // Fix: Wrap in try-with-resources.

  public static class ResourceLeak {
    public int readFirstByte(String path) throws IOException {
      FileInputStream fis = new FileInputStream(path); // BUG: never closed!
      return fis.read();
    }
  }

  // ── Challenge 6: Mutable object aliasing ────────────────────────────────────
  // BUG: Both instances share the same ArrayList reference. Mutations through
  //      one instance are visible (and destructive) to the other.
  // Fix: Copy the list in the constructor (defensive copy).

  public static class MutableAliasBug {
    private final List<String> items;

    public MutableAliasBug(List<String> items) {
      this.items = items; // BUG: stores the caller's reference — aliased!
    }

    public void add(String item) {
      items.add(item);
    }

    public List<String> getItems() {
      return items;
    }
  }

  // ── Challenge 7: Connection leak in a loop ────────────────────────────────────
  // BUG: A new Connection is created each iteration but never closed.
  //      In production this exhausts the JDBC connection pool.
  // Fix: Wrap each Connection in try-with-resources.

  public static class ConnectionLeakBug {
    static final List<String> closedConnections = new ArrayList<>();

    public static class FakeConnection implements AutoCloseable {
      private final String name;
      private boolean closed = false;

      public FakeConnection(String name) {
        this.name = name;
      }

      public String query() {
        return "result-" + name;
      }

      @Override
      public void close() {
        if (!closed) {
          closed = true;
          closedConnections.add(name);
        }
      }

      public boolean isClosed() {
        return closed;
      }
    }

    public List<String> queryAll(String[] names) {
      List<String> results = new ArrayList<>();
      for (String name : names) {
        FakeConnection conn = new FakeConnection(name); // BUG: never closed!
        results.add(conn.query());
      }
      return results;
    }
  }

  // ── Challenge 8: Swallowed exception ─────────────────────────────────────────
  // BUG: The catch block is empty — the exception is silently swallowed.
  //      The caller has no idea the operation failed.
  // Fix: At minimum, log the exception; better yet, rethrow or wrap it.

  public static class SwallowedExceptionBug {
    public String parseAndDouble(String input) {
      try {
        int value = Integer.parseInt(input);
        return String.valueOf(value * 2);
      } catch (NumberFormatException e) {
        // BUG: exception silently swallowed — returns null with no indication of failure!
      }
      return null;
    }
  }
}
