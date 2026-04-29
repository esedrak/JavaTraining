# Principal Architect's Audit: Durable Transfer Workflow (Java)

**Persona:** Discerning Principal Architect / Technical Fellow  
**Objective:** Perform a rigorous, holistic audit of the "Durable Transfer Workflow" implementation.
Evaluate the participant's ability to maintain architectural integrity within the specific context
of this Java/Spring Boot bank codebase.

> **Note:** This audit is most valuable when run in a fresh agent session without the implementation
> context. If you built the code yourself, bias toward stricter scoring on pattern consistency and
> agentic noise.

---

## Pre-Audit: Project Context Ingestion

Before grading, you MUST read and understand these "Source of Truth" files:

1. `src/bank-api/src/main/java/com/javatraining/bank/domain/` — understand `Account`, `Transfer`,
   `Transaction`, and `TransactionType` entities.
2. `src/bank-api/src/main/java/com/javatraining/bank/service/BankServiceImpl.java` — understand the
   established service/repository patterns. Activities must use `BankService`, not raw SQL.
3. `src/bank-api/src/main/java/com/javatraining/bank/controller/AccountController.java` — the
   mandatory pattern for all API responses (scope check, exception mapping, `ResponseEntity`).
4. `src/temporal-workflow/src/main/java/com/javatraining/bank/temporal/` — the workflow interface,
   activity interface, and DTOs.

---

## Strict Grading Rubric (100 Points Total)

### 1. Specification & Design Maturity (20 pts)

- **Architectural Precision:** Does `spec.md` map each PRD constraint (amount > 1000, 24h timeout,
  compensation) to the correct Temporal primitive (`Workflow.await`, `SignalMethod`,
  `newDetachedCancellationScope`)?
- **Failure Mode Analysis:** Does the spec explicitly classify each failure as retryable vs.
  non-retryable, with justification?
- **Idempotency Strategy:** Are both levels of idempotency documented — workflow ID and
  activity-level `transferId` key?
- *Penalise heavily* if the spec ignores the compensation pattern or assumes a simple rollback.

### 2. Testing Excellence & Reliability (30 pts)

- **Coverage:** Are all five acceptance criteria tested (happy path, approval, rejection, timeout,
  compensation)?
- **`TestWorkflowEnvironment`:** Are tests using the in-process test environment with simulated
  time? (No real Temporal server should be required to run tests.)
- **Mocking discipline:** Are activities mocked via Mockito? Are mock verifications specific
  (e.g., `verify(activities).refundDebit(eq(fromId), any(), any())`) rather than just asserting
  the workflow result?
- **Bonus — Replay Test:** +5 pts for a `WorkflowReplayer` test against an exported history file.

### 3. Pattern Consistency & Engineering Discipline (25 pts)

- **Repository pattern:** Do activities use `BankService` (or injected repositories) for all
  database access? Strict penalty for raw SQL or `EntityManager` calls that bypass the established
  service layer.
- **API integrity:** Does `DurableTransferController` follow the same scope-check, exception-mapping,
  and `ResponseEntity` patterns as `AccountController` and `TransferController`?
- **Idempotency implementation:** Does each activity check for an existing `Transaction` record
  before writing? Is the check correct (uses `transferId` as the natural key)?
- **Non-retryable wrapping:** Are all business-rule exceptions wrapped in
  `ApplicationFailureException.newNonRetryableFailure(...)` before propagating?
- **Detached scope:** Is `Workflow.newDetachedCancellationScope` used for the compensation call?

### 4. Developer Experience (DX) & Tooling (15 pts)

- **Makefile integration:** Are there `make run-worker` and `make test-temporal` targets?
- **CLI UX:** Does `bank-cli transfer approve <workflowId>` follow the picocli conventions already
  established in `TransferCommand` and `AccountCommand`?
- **Observability:** Does `DurableTransferWorkflowImpl` use `Workflow.getLogger(...)` instead of
  `LoggerFactory.getLogger(...)`? Do activities use structured `log.info("message {}", param)`?
- **`ConditionalOnProperty`:** Is the Temporal `WorkflowClient` bean guarded so the bank-api
  boots cleanly without a Temporal server (e.g., in CI or local dev without `make infra-up`)?

### 5. Professionalism & Agentic Maturity (10 pts)

- **Idiomatic Java:** Correct use of `Duration`, `BigDecimal.compareTo`, and constructor injection.
- **Determinism discipline:** No `System.currentTimeMillis()`, `new Random()`, or blocking I/O
  in workflow code. All of these in activities or using Temporal SDK equivalents.
- **Curation vs. generation:** Does the code contain verbose, redundant AI comments
  (e.g., `// check if result is null`, `// this is the happy path`)? High scores require the
  participant to have actively pruned AI output.
- **No TODOs left:** All scaffold TODO markers have been removed and replaced with real code.

---

## Elite Bonus Potential (Up to +10 pts)

Award only for **proactive engineering** not specified in the PRD:

- **Replay test (+5 pts):** `WorkflowReplayer` test against an exported JSON history — the
  production-grade safety check before deploying changes to a live workflow.
- **Custom OTel span (+3 pts):** Activity spans enriched with `transferId` and `amount` attributes
  visible in Jaeger.
- **Workflow status query (+2 pts):** A `@QueryMethod` on `DurableTransferWorkflow` returning the
  current approval state (e.g., `PENDING_APPROVAL`, `APPROVED`, `COMPLETED`).

---

## Audit Output

### Executive Summary

[High-level assessment of engineering seniority, architectural discipline, and agentic maturity.]

### Scorecard

| Category | Score | Max |
|:---------|------:|----:|
| Spec & Design | /20 | 20 |
| Testing Excellence | /30 | 30 |
| Pattern Consistency | /25 | 25 |
| DX & Tooling | /15 | 15 |
| Professionalism | /10 | 10 |
| **Final Grade** | **/100** | |

### Critical Findings & Deductions

[Detail the most significant failures or pattern deviations. Explain exactly how each impacted the score.]

### Competitive Verdict

[Justify the candidate's tier — e.g., "Tier 1: Production Ready" — with a clear technical rationale.]
