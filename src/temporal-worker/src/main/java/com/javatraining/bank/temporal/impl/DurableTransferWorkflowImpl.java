package com.javatraining.bank.temporal.impl;

import com.javatraining.bank.temporal.activity.TransferActivities;
import com.javatraining.bank.temporal.dto.TransferInput;
import com.javatraining.bank.temporal.dto.TransferResult;
import com.javatraining.bank.temporal.workflow.DurableTransferWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link DurableTransferWorkflow}.
 *
 * <h2>Quest 4 — implement this class</h2>
 *
 * Follow the five TODO markers below in order. Each one maps directly to a section of the PRD.
 *
 * <h3>Key Temporal primitives you will need</h3>
 *
 * <ul>
 *   <li>{@link Workflow#newActivityStub} — creates a typed proxy for activity calls.
 *   <li>{@link Workflow#await(Duration, java.util.function.Supplier)} — blocks the workflow until a
 *       condition is true or a timeout elapses. Use this for the approval gate.
 *   <li>{@link Workflow#newDetachedCancellationScope} — runs compensation even when the workflow is
 *       being cancelled. Wrap {@code activities.refundDebit(...)} inside one.
 *   <li>{@link io.temporal.failure.ApplicationFailureException#newNonRetryableFailure} — signal a
 *       business-rule violation that must not be retried.
 *   <li>{@link Workflow#getLogger} — use instead of SLF4J directly; suppresses replay logs.
 * </ul>
 *
 * <h3>Determinism rule</h3>
 *
 * Workflow code <strong>must</strong> be deterministic. Never use:
 *
 * <ul>
 *   <li>{@code System.currentTimeMillis()} — use {@link Workflow#currentTimeMillis()} instead.
 *   <li>{@code new Random()} — use {@link Workflow#newRandom()} instead.
 *   <li>Blocking I/O or thread sleeps — all I/O happens inside activities.
 * </ul>
 */
@Component
public class DurableTransferWorkflowImpl implements DurableTransferWorkflow {

  // Workflow logger: replays are suppressed automatically, keeping production logs clean.
  private static final Logger log = Workflow.getLogger(DurableTransferWorkflowImpl.class);

  // Signal state — mutated by approve() / reject() signal handlers
  private boolean approved = false;
  private boolean rejected = false;

  // Activity stub — Temporal intercepts calls and schedules them on the worker.
  // TODO 1 (Quest 4): configure ActivityOptions with a scheduleToCloseTimeout and a RetryOptions
  //   that excludes non-retryable failure types (e.g. ApplicationFailureException).
  //   Then call Workflow.newActivityStub(TransferActivities.class, options).
  private final TransferActivities activities = null; // replace null with the stub

  @Override
  public TransferResult execute(TransferInput input) {
    log.info("Starting durable transfer {}", input.transferId());

    // TODO 2 (Quest 4): Validate accounts
    //   activities.validateAccounts(input);

    // TODO 3 (Quest 4): Approval gate — only for high-value transfers
    //   if (input.amount().longValue() > APPROVAL_THRESHOLD) {
    //     boolean signalReceived = Workflow.await(Duration.ofHours(24),
    //                                             () -> approved || rejected);
    //     if (!signalReceived || rejected) {
    //       throw ApplicationFailureException.newNonRetryableFailure(
    //           "Transfer rejected or timed out", "REJECTED");
    //     }
    //   }

    // TODO 4 (Quest 4): Debit stage
    //   activities.debitAccount(input.fromAccountId(), input.amount(), input.transferId());

    // TODO 5 (Quest 4): Credit stage with compensation
    //   try {
    //     activities.creditAccount(input.toAccountId(), input.amount(), input.transferId());
    //   } catch (ActivityFailure e) {
    //     // Compensation: refund runs in a detached scope so cancellation cannot abort it.
    //     Workflow.newDetachedCancellationScope(
    //         () -> activities.refundDebit(
    //                   input.fromAccountId(), input.amount(), input.transferId()))
    //         .run();
    //     throw ApplicationFailureException.newNonRetryableFailure(
    //         "Credit failed; debit refunded: " + e.getMessage(), "COMPENSATED");
    //   }

    throw new UnsupportedOperationException("Implement me! — see the TODO comments above");
  }

  @Override
  public void approve() {
    // TODO (Quest 4): set approved = true
    log.info("Approval signal received");
    approved = true;
  }

  @Override
  public void reject() {
    // TODO (Quest 4): set rejected = true
    log.info("Rejection signal received");
    rejected = true;
  }
}
