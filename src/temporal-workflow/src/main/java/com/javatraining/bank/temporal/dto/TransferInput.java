package com.javatraining.bank.temporal.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Immutable input to {@code DurableTransferWorkflow}.
 *
 * <p>All fields are required. Temporal serializes this to JSON when scheduling the workflow, so
 * every field must be Jackson-serializable.
 *
 * <p>Use {@link #transferId()} as the deterministic Workflow ID prefix:
 *
 * <pre>
 * workflowOptions = WorkflowOptions.newBuilder()
 *     .setWorkflowId("transfer-" + input.transferId())
 *     .setTaskQueue(TASK_QUEUE)
 *     .build();
 * </pre>
 */
public record TransferInput(
    UUID transferId,
    UUID fromAccountId,
    UUID toAccountId,
    BigDecimal amount,
    String reference) {}
