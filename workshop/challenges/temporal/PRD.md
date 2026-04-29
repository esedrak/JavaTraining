# PRD: High-Value Durable Transfer Workflow

## 1. Overview

The current `POST /v1/transfers` endpoint is a synchronous database transaction. It works for basic
cases but cannot handle:

- **Human-in-the-loop:** Manual approval for large amounts.
- **Distributed reliability:** Ensuring the transfer completes even if the worker crashes between
  the debit and credit steps.
- **Compensation:** Automatically reversing a debit when the credit stage fails.

This project introduces a **Temporal Workflow** to orchestrate a "High-Value Durable Transfer".

## 2. Goals

- Implement a durable transfer process using the Temporal Java SDK.
- Demonstrate the **Compensation Pattern** for distributed transactions.
- Implement **Signal Handling** for manual approvals.
- Ensure **Idempotency** across retries at both the workflow and activity level.

## 3. Requirements

### 3.1 Workflow: `DurableTransferWorkflow`

- **Input:** `TransferInput(transferId, fromAccountId, toAccountId, amount, reference)`
- **Logic:**
    1. **Validation:** Call `validateAccounts` activity — check both accounts exist and funds are sufficient.
    2. **Approval gate:** If `amount > 1000`, block until an `approve()` or `reject()` signal arrives.
       - If no signal within **24 hours**: fail with a non-retryable `ApplicationFailureException`.
       - If `reject()` received: fail immediately with a non-retryable `ApplicationFailureException`.
    3. **Debit:** Call `debitAccount` activity to remove funds from `fromAccountId`.
    4. **Credit:** Call `creditAccount` activity to add funds to `toAccountId`.
    5. **Compensation:** If `creditAccount` fails, call `refundDebit` in a **detached cancellation scope** to reverse the debit — even if the workflow is being cancelled.

### 3.2 Activities (`TransferActivities`)

| Activity | Description |
|----------|-------------|
| `validateAccounts(input)` | Verify both accounts exist; check `amount > 0`. Non-retryable on business errors. |
| `debitAccount(accountId, amount, transferId)` | Remove funds. Must be idempotent via `transferId`. |
| `creditAccount(accountId, amount, transferId)` | Add funds. Must be idempotent via `transferId`. |
| `refundDebit(accountId, amount, transferId)` | Reverse a debit. Compensation activity. Must be idempotent. |

### 3.3 Technical Constraints

- **Determinism:** Workflow code must be strictly deterministic. Use `Workflow.currentTimeMillis()`,
  `Workflow.newRandom()`, and `Workflow.getLogger()` — never the `java.*` equivalents.
- **Timeouts:** Configure `ActivityOptions` with `scheduleToCloseTimeout`. Configure workflow
  execution timeout for the approval window.
- **Retries:** Transient failures → retryable. Business-rule failures (insufficient funds, account
  not found) → wrap in `ApplicationFailureException.newNonRetryableFailure(...)`.
- **Idempotency (two levels):**
  1. API starts the workflow with a deterministic ID: `"transfer-" + transferId`. A second API call
     with the same body returns the existing execution, not a duplicate.
  2. Each activity checks whether a `Transaction` with the given `transferId` already exists before
     writing — prevents duplicate ledger entries on Temporal activity retries.

### 3.4 New API Endpoint

`POST /v1/durable-transfers` — authenticates with JWT (`transfers:write` scope), starts the
workflow, and returns `202 Accepted` with the `workflowId`.

### 3.5 New CLI Command

`bank-cli transfer approve <workflowId>` — sends the approval signal via a new API endpoint
`POST /v1/durable-transfers/{workflowId}/signal/approve`.

## 4. Acceptance Criteria

| # | Scenario | Expected |
|---|----------|----------|
| 1 | **Happy Path** | Transfer < $1,000 completes automatically |
| 2 | **Approval Path** | Transfer > $1,000 waits, then completes on `approve()` |
| 3 | **Rejection Path** | Transfer > $1,000 fails immediately on `reject()` |
| 4 | **Timeout Path** | Transfer > $1,000 fails if no signal received in 24 h |
| 5 | **Compensation Path** | `creditAccount` failure triggers `refundDebit` |
| 6 | **Idempotency** | Restarting the worker or replaying the same workflow ID produces no duplicate debits |
