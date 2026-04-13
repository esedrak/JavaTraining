package com.javatraining.basics.context;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Demonstrates timeout patterns using {@link ExecutorService} and {@link Future#get(long,
 * TimeUnit)}.
 *
 * <p>A common pattern in Java is to submit work to an executor and then wait for the result with a
 * deadline. If the deadline is exceeded, the {@link Future} is cancelled and a {@link
 * RuntimeException} is thrown.
 */
public class TimeoutDemo {

  /**
   * Runs {@code task} and returns its result, or throws if it does not complete within {@code
   * timeoutMs} milliseconds.
   *
   * @param task the work to perform
   * @param timeoutMs maximum allowed duration in milliseconds
   * @param <T> return type of the task
   * @return the task's result
   * @throws RuntimeException if the timeout is exceeded or the task throws
   */
  public <T> T withTimeout(Callable<T> task, long timeoutMs) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<T> future = executor.submit(task);
    executor.shutdown();
    try {
      return future.get(timeoutMs, TimeUnit.MILLISECONDS);
    } catch (TimeoutException e) {
      future.cancel(true);
      throw new RuntimeException("Task timed out after " + timeoutMs + " ms", e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Task interrupted", e);
    } catch (ExecutionException e) {
      throw new RuntimeException("Task threw an exception", e.getCause());
    }
  }
}
