package com.javatraining.basics.scheduledtasks;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Topic 16: Scheduled Tasks
 *
 * <p>Spring's {@code @Scheduled} annotation turns plain methods into recurring tasks. The
 * scheduling infrastructure must be enabled with {@code @EnableScheduling} on a configuration
 * class.
 *
 * <p>Using {@link AtomicInteger} for the counter ensures thread-safety when the scheduler runs the
 * task on a background thread.
 */
@Component
public class TaskRunner {

  private final AtomicInteger executionCount = new AtomicInteger(0);

  /**
   * Runs every 1 000 ms (1 second). {@code fixedRate} measures the delay from the <em>start</em> of
   * the previous invocation.
   */
  @Scheduled(fixedRate = 1000)
  public void runPeriodically() {
    executionCount.incrementAndGet();
    System.out.println("Periodic task executed. Count: " + executionCount.get());
  }

  /**
   * Runs at the top of every hour using a cron expression. Fields: second minute hour day-of-month
   * month day-of-week.
   */
  @Scheduled(cron = "0 0 * * * *")
  public void runHourly() {
    System.out.println("Hourly task executed at: " + java.time.Instant.now());
  }

  /** Returns the total number of times {@link #runPeriodically()} has been called. */
  public int getExecutionCount() {
    return executionCount.get();
  }
}
