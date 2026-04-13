package com.javatraining.basics.scheduledtasks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;

/** Pure unit tests — no Spring context. The @Scheduled and @Async annotations are ignored here. */
class ScheduledTasksTest {

  @Test
  void taskRunner_runPeriodically_calledThreeTimes_countIsThree() {
    TaskRunner runner = new TaskRunner();
    runner.runPeriodically();
    runner.runPeriodically();
    runner.runPeriodically();
    assertThat(runner.getExecutionCount()).isEqualTo(3);
  }

  @Test
  void taskRunner_initialCount_isZero() {
    TaskRunner runner = new TaskRunner();
    assertThat(runner.getExecutionCount()).isZero();
  }

  @Test
  void asyncService_processAsync_returnsProcessedString() throws Exception {
    AsyncService service = new AsyncService();
    // Without Spring, @Async is not active — the method runs synchronously
    CompletableFuture<String> future = service.processAsync("test");
    assertThat(future.get()).isEqualTo("Processed: test");
  }

  @Test
  void commandLineRunnerDemo_run_doesNotThrow() {
    CommandLineRunnerDemo runner = new CommandLineRunnerDemo();
    assertThatCode(() -> runner.run("arg1", "arg2")).doesNotThrowAnyException();
  }

  @Test
  void commandLineRunnerDemo_run_withNoArgs_doesNotThrow() {
    CommandLineRunnerDemo runner = new CommandLineRunnerDemo();
    assertThatCode(() -> runner.run()).doesNotThrowAnyException();
  }
}
