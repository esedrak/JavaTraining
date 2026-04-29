package com.javatraining.bank.temporal.activities;

import com.javatraining.bank.temporal.activity.TransferActivities;
import com.javatraining.bank.temporal.dto.TransferInput;
import java.math.BigDecimal;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementations of the four {@link TransferActivities}.
 *
 * <h2>Quest 5 — implement this class</h2>
 *
 * Each method has a {@code TODO} marker. Implement them in order. Use {@link
 * com.javatraining.bank.service.BankService} (injected via constructor) for all database access —
 * do not write SQL directly.
 *
 * <h3>Idempotency contract</h3>
 *
 * Temporal may retry an activity if it times out or the worker crashes mid-execution. Your
 * implementation must be safe to call multiple times with the same {@code transferId}:
 *
 * <ul>
 *   <li>Before writing, check whether a {@code Transaction} record with the given {@code
 *       transferId} already exists.
 *   <li>If it does, return early — do <strong>not</strong> apply the balance change again.
 * </ul>
 *
 * <h3>Non-retryable failures</h3>
 *
 * Wrap business-rule violations in a non-retryable exception so Temporal stops retrying:
 *
 * <pre>
 * throw ApplicationFailureException.newNonRetryableFailure(
 *     ex.getMessage(), "INSUFFICIENT_FUNDS");
 * </pre>
 *
 * Let all other exceptions propagate as-is — Temporal will retry them according to the policy
 * configured in {@code DurableTransferWorkflowImpl}.
 */
@Component
public class TransferActivitiesImpl implements TransferActivities {

  private static final Logger log = LoggerFactory.getLogger(TransferActivitiesImpl.class);

  // TODO (Quest 5): inject BankService (and optionally TransactionRepository for idempotency checks)
  //   via constructor injection

  @Override
  public void validateAccounts(TransferInput input) {
    // TODO (Quest 5): use bankService.getAccount() to verify both accounts exist.
    //   Check that input.amount() is positive.
    //   Wrap AccountNotFoundException / IllegalArgumentException in
    //   ApplicationFailureException.newNonRetryableFailure so Temporal does not retry.
    //
    //   Pattern:
    //     try {
    //       bankService.getAccount(input.fromAccountId());
    //       bankService.getAccount(input.toAccountId());
    //     } catch (AccountNotFoundException ex) {
    //       throw ApplicationFailureException.newNonRetryableFailure(
    //           ex.getMessage(), "ACCOUNT_NOT_FOUND");
    //     }

    throw new UnsupportedOperationException("Implement me! — Quest 5");
  }

  @Override
  public void debitAccount(UUID accountId, BigDecimal amount, UUID transferId) {
    // TODO (Quest 5): idempotency check — return early if transferId already debited.
    //   Then debit the account and record a Transaction with type DEBIT.
    //   Wrap InsufficientFundsException as non-retryable.
    //
    //   Pattern:
    //     if (transactionRepository.existsByTransferIdAndType(transferId, TransactionType.DEBIT)) {
    //       log.info("Debit {} already applied — skipping", transferId);
    //       return;
    //     }
    //     var account = bankService.getAccount(accountId);
    //     if (account.getBalance().compareTo(amount) < 0) {
    //       throw ApplicationFailureException.newNonRetryableFailure(..., "INSUFFICIENT_FUNDS");
    //     }
    //     account.setBalance(account.getBalance().subtract(amount));
    //     // save transaction record...

    throw new UnsupportedOperationException("Implement me! — Quest 5");
  }

  @Override
  public void creditAccount(UUID accountId, BigDecimal amount, UUID transferId) {
    // TODO (Quest 5): idempotency check — return early if transferId already credited.
    //   Then credit the account and record a Transaction with type CREDIT.
    //
    //   Pattern mirrors debitAccount — use TransactionType.CREDIT.

    throw new UnsupportedOperationException("Implement me! — Quest 5");
  }

  @Override
  public void refundDebit(UUID accountId, BigDecimal amount, UUID transferId) {
    // TODO (Quest 5): idempotency check — return early if transferId already refunded.
    //   Restore the account balance and record a Transaction with type REFUND (or CREDIT).
    //
    //   This is the compensating transaction. It MUST be idempotent because the workflow
    //   may retry it after a worker crash.

    throw new UnsupportedOperationException("Implement me! — Quest 5");
  }
}
