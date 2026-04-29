package com.javatraining.bank.temporal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.javatraining.bank.temporal.activity.TransferActivities;
import com.javatraining.bank.temporal.dto.TransferInput;
import com.javatraining.bank.temporal.dto.TransferResult;
import com.javatraining.bank.temporal.impl.DurableTransferWorkflowImpl;
import com.javatraining.bank.temporal.workflow.DurableTransferWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowFailedException;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.failure.ApplicationFailure;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Durable Transfer Quest — workflow unit tests using {@link TestWorkflowEnvironment}.
 *
 * <p>{@code TestWorkflowEnvironment} runs workflows in-process with simulated time. No running
 * Temporal server is required.
 *
 * <h2>Quest 3</h2>
 *
 * Read each {@code @Disabled} test and understand what it asserts before writing any code.
 * Confirm the tests compile and are skipped:
 *
 * <pre>./gradlew :src:temporal-worker:test --tests "*.DurableTransferWorkflowTest"
 * # Expected: 5 skipped, 0 failures</pre>
 *
 * <h2>Quest 4</h2>
 *
 * Implement {@link DurableTransferWorkflowImpl}, then remove {@code @Disabled} from each test
 * one by one as you implement each execution path.
 */
@DisplayName("DurableTransferWorkflow")
class DurableTransferWorkflowTest {

  // ── Test infrastructure ──────────────────────────────────────────────────

  private TestWorkflowEnvironment testEnv;
  private WorkflowClient client;
  private TransferActivities activities; // mocked — no DB needed

  @BeforeEach
  void setUp() {
    testEnv = TestWorkflowEnvironment.newInstance();
    client = testEnv.getWorkflowClient();

    // Mock the activities — no database touch in these tests
    activities = mock(TransferActivities.class);

    Worker worker = testEnv.newWorker(DurableTransferWorkflow.TASK_QUEUE);
    worker.registerWorkflowImplementationTypes(DurableTransferWorkflowImpl.class);
    worker.registerActivitiesImplementations(activities);

    testEnv.start();
  }

  @AfterEach
  void tearDown() {
    testEnv.close();
  }

  // ── Helpers ──────────────────────────────────────────────────────────────

  /** Create a typed workflow stub with a deterministic workflow ID. */
  private DurableTransferWorkflow newStub(UUID transferId) {
    return client.newWorkflowStub(
        DurableTransferWorkflow.class,
        WorkflowOptions.newBuilder()
            .setWorkflowId("transfer-" + transferId)
            .setTaskQueue(DurableTransferWorkflow.TASK_QUEUE)
            .build());
  }

  private TransferInput smallTransfer() {
    return new TransferInput(
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        new BigDecimal("200.00"), // below APPROVAL_THRESHOLD — no signal needed
        "test-reference");
  }

  private TransferInput largeTransfer() {
    return new TransferInput(
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        new BigDecimal("5000.00"), // above APPROVAL_THRESHOLD — requires approval signal
        "test-reference");
  }

  // ── Quest 4 tests ─────────────────────────────────────────────────────────

  /**
   * Quest 4: remove {@code @Disabled} once the workflow completes for small transfers.
   *
   * <p>Expected: all four activities are called in sequence; result status is {@code "COMPLETED"}.
   */
  @Test
  @Disabled("Quest 4 — remove @Disabled once the workflow is implemented")
  @DisplayName("happy path: small transfer completes without approval")
  void happyPath_smallTransfer_completesAutomatically() {
    var input = smallTransfer();
    var stub = newStub(input.transferId());

    // execute() is synchronous in tests — TestWorkflowEnvironment runs the full workflow
    TransferResult result = stub.execute(input);

    assertThat(result.transferId()).isEqualTo(input.transferId());
    assertThat(result.status()).isEqualTo("COMPLETED");

    verify(activities).validateAccounts(input);
    verify(activities).debitAccount(input.fromAccountId(), input.amount(), input.transferId());
    verify(activities).creditAccount(input.toAccountId(), input.amount(), input.transferId());
    verify(activities, never()).refundDebit(any(), any(), any());
  }

  /**
   * Quest 4: remove {@code @Disabled} once signal handling works.
   *
   * <p>Send an {@code approve()} signal after the workflow reaches the approval gate. Expected:
   * workflow unblocks and completes successfully.
   *
   * <p>Tip: {@code testEnv.sleep(Duration.ofSeconds(1))} fast-forwards simulated time so the
   * workflow reaches {@code Workflow.await()} before the signal arrives.
   */
  @Test
  @Disabled("Quest 4 — remove @Disabled once signal handling is implemented")
  @DisplayName("approval path: large transfer completes after approve() signal")
  void approvalPath_largeTransfer_completesAfterApproval() throws Exception {
    var input = largeTransfer();
    var stub = newStub(input.transferId());

    // Start asynchronously so we can send a signal before the workflow times out
    WorkflowClient.start(stub::execute, input);

    // Let the workflow reach the Workflow.await() gate
    testEnv.sleep(Duration.ofSeconds(1));

    // Send the approval signal
    stub.approve();

    // Collect the result — WorkflowStub.fromTyped wraps the typed stub for getResult()
    TransferResult result =
        WorkflowStub.fromTyped(stub).getResult(TransferResult.class);

    assertThat(result.status()).isEqualTo("COMPLETED");
    verify(activities).debitAccount(input.fromAccountId(), input.amount(), input.transferId());
    verify(activities).creditAccount(input.toAccountId(), input.amount(), input.transferId());
    verify(activities, never()).refundDebit(any(), any(), any());
  }

  /**
   * Quest 4: remove {@code @Disabled} once rejection signal handling works.
   *
   * <p>Send a {@code reject()} signal. Expected: workflow throws {@link WorkflowFailedException}
   * whose cause is an {@link ApplicationFailure}.
   */
  @Test
  @Disabled("Quest 4 — remove @Disabled once signal handling is implemented")
  @DisplayName("rejection path: large transfer fails immediately after reject() signal")
  void rejectionPath_largeTransfer_failsOnRejectSignal() throws Exception {
    var input = largeTransfer();
    var stub = newStub(input.transferId());

    WorkflowClient.start(stub::execute, input);
    testEnv.sleep(Duration.ofSeconds(1));
    stub.reject();

    assertThatThrownBy(() -> WorkflowStub.fromTyped(stub).getResult(TransferResult.class))
        .isInstanceOf(WorkflowFailedException.class)
        .hasCauseInstanceOf(ApplicationFailure.class);

    // No funds should have moved
    verify(activities, never()).debitAccount(any(), any(), any());
    verify(activities, never()).creditAccount(any(), any(), any());
  }

  /**
   * Quest 4: remove {@code @Disabled} once the approval-gate timeout is implemented.
   *
   * <p>Skip time past the 24-hour window without sending any signal. Expected: workflow fails with
   * {@link WorkflowFailedException}.
   *
   * <p>Tip: {@code testEnv.sleep(Duration.ofHours(25))} fast-forwards 25 hours of simulated time
   * instantly — {@code TestWorkflowEnvironment} does not wall-clock wait.
   */
  @Test
  @Disabled("Quest 4 — remove @Disabled once the approval-gate timeout is implemented")
  @DisplayName("timeout path: large transfer fails when no signal received within 24h")
  void timeoutPath_largeTransfer_failsAfterNoSignal() throws Exception {
    var input = largeTransfer();
    var stub = newStub(input.transferId());

    WorkflowClient.start(stub::execute, input);

    // Skip past the 24-hour approval window
    testEnv.sleep(Duration.ofHours(25));

    assertThatThrownBy(() -> WorkflowStub.fromTyped(stub).getResult(TransferResult.class))
        .isInstanceOf(WorkflowFailedException.class)
        .hasCauseInstanceOf(ApplicationFailure.class);
  }

  /**
   * Quest 4: remove {@code @Disabled} once the compensation path is implemented.
   *
   * <p>Make {@code creditAccount} throw so the workflow enters the compensation branch. Expected:
   * {@code refundDebit} is called and the workflow fails with {@link WorkflowFailedException}.
   */
  @Test
  @Disabled("Quest 4 — remove @Disabled once compensation logic is implemented")
  @DisplayName("compensation path: refundDebit is called when creditAccount fails")
  void compensationPath_creditFails_refundIsApplied() {
    var input = smallTransfer();
    var stub = newStub(input.transferId());

    // Simulate credit failure (e.g., destination account closed mid-flight)
    doThrow(new RuntimeException("destination account closed"))
        .when(activities)
        .creditAccount(eq(input.toAccountId()), any(), any());

    assertThatThrownBy(() -> stub.execute(input))
        .isInstanceOf(WorkflowFailedException.class)
        .hasCauseInstanceOf(ApplicationFailure.class);

    // The debit must be reversed
    verify(activities).debitAccount(input.fromAccountId(), input.amount(), input.transferId());
    verify(activities).refundDebit(input.fromAccountId(), input.amount(), input.transferId());
    // Credit was attempted and failed — it must NOT have been called a second time
    verify(activities).creditAccount(input.toAccountId(), input.amount(), input.transferId());
  }
}
