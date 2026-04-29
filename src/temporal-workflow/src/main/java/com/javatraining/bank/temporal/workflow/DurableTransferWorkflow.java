package com.javatraining.bank.temporal.workflow;

import com.javatraining.bank.temporal.dto.TransferInput;
import com.javatraining.bank.temporal.dto.TransferResult;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

/**
 * Durable, fault-tolerant bank transfer orchestrated by Temporal.
 *
 * <h2>Workflow contract</h2>
 *
 * <ol>
 *   <li>Validate accounts (Activity).
 *   <li>If {@code amount > 1000}, wait up to 24 h for an {@link #approve()} or {@link #reject()}
 *       signal before proceeding.
 *   <li>Debit the source account (Activity).
 *   <li>Credit the destination account (Activity). If this fails, call the refund activity as a
 *       compensating transaction.
 * </ol>
 *
 * <h2>Signals</h2>
 *
 * <ul>
 *   <li>{@link #approve()} — unblocks the approval gate for high-value transfers.
 *   <li>{@link #reject()} — immediately fails the workflow with a non-retryable error.
 * </ul>
 *
 * <h2>Task queue</h2>
 *
 * Use {@link #TASK_QUEUE} as the task-queue name everywhere (worker registration, workflow options,
 * tests).
 */
@WorkflowInterface
public interface DurableTransferWorkflow {

  String TASK_QUEUE = "durable-transfer";

  /** Approval threshold: transfers above this amount require a manual {@link #approve()} signal. */
  long APPROVAL_THRESHOLD = 1_000L;

  /**
   * Executes the durable transfer and returns the result.
   *
   * <p>This is the only {@link WorkflowMethod} on this interface.
   */
  @WorkflowMethod
  TransferResult execute(TransferInput input);

  /**
   * Approves a high-value transfer that is waiting at the approval gate.
   *
   * <p>Sending this signal on a transfer that does not require approval, or that has already been
   * approved, is a no-op.
   */
  @SignalMethod
  void approve();

  /**
   * Rejects a high-value transfer that is waiting at the approval gate.
   *
   * <p>The workflow will fail immediately with a non-retryable {@code
   * ApplicationFailureException}.
   */
  @SignalMethod
  void reject();
}
