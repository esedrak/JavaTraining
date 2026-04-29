package com.javatraining.bank.temporal.dto;

import java.util.UUID;

/**
 * Result returned by a completed {@code DurableTransferWorkflow} execution.
 *
 * @param transferId the ID of the persisted {@code Transfer} record
 * @param status human-readable outcome, e.g. {@code "COMPLETED"} or {@code "REFUNDED"}
 */
public record TransferResult(UUID transferId, String status) {}
