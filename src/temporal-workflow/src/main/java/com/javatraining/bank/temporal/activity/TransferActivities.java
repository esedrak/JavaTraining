package com.javatraining.bank.temporal.activity;

import com.javatraining.bank.temporal.dto.TransferInput;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Activity interface for the durable transfer workflow.
 *
 * <p>Each method represents a single, idempotent unit of I/O. Temporal may retry any activity
 * automatically on transient failure. All implementations must therefore be safe to call more than
 * once with the same arguments — use {@code transferId} as the natural idempotency key when writing
 * to the database.
 *
 * <h2>Retry policy (configure in the worker)</h2>
 *
 * <ul>
 *   <li>Transient failures (DB timeout, network blip) → retryable — let Temporal retry.
 *   <li>Business-rule failures (insufficient funds, account not found) → non-retryable — wrap in
 *       {@code ApplicationFailureException.newNonRetryableFailure(...)}.
 * </ul>
 */
@ActivityInterface
public interface TransferActivities {

  /**
   * Verifies that both accounts exist and that {@code fromAccountId} has sufficient funds.
   *
   * <p>Throws a non-retryable {@code ApplicationFailureException} if validation fails.
   *
   * @param input the workflow input (both account IDs and amount are read from here)
   */
  @ActivityMethod
  void validateAccounts(TransferInput input);

  /**
   * Debits {@code amount} from {@code accountId}.
   *
   * <p>Must be idempotent: if a record with {@code transferId} already exists in the ledger, return
   * without writing a duplicate entry.
   *
   * @param accountId the account to debit
   * @param amount the amount to remove
   * @param transferId idempotency key — use as the natural key in the DB write
   */
  @ActivityMethod
  void debitAccount(UUID accountId, BigDecimal amount, UUID transferId);

  /**
   * Credits {@code amount} to {@code accountId}.
   *
   * <p>Must be idempotent — same contract as {@link #debitAccount}.
   *
   * @param accountId the account to credit
   * @param amount the amount to add
   * @param transferId idempotency key
   */
  @ActivityMethod
  void creditAccount(UUID accountId, BigDecimal amount, UUID transferId);

  /**
   * Compensating transaction: reverses the debit on {@code accountId} when {@link #creditAccount}
   * has failed.
   *
   * <p>Must be idempotent — same contract as {@link #debitAccount}.
   *
   * <p><strong>Important:</strong> the workflow must call this activity using a detached
   * cancellation scope (e.g., wrap in {@code Workflow.newDetachedCancellationScope}) so that the
   * compensation runs even if the workflow execution is being cancelled.
   *
   * @param accountId the account that was debited and must be refunded
   * @param amount the amount to restore
   * @param transferId idempotency key
   */
  @ActivityMethod
  void refundDebit(UUID accountId, BigDecimal amount, UUID transferId);
}
