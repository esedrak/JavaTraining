package com.javatraining.bank.dto;

import com.javatraining.bank.domain.Transfer;
import com.javatraining.bank.domain.TransferStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransferResponse(
    UUID id,
    UUID fromAccountId,
    UUID toAccountId,
    BigDecimal amount,
    TransferStatus status,
    Instant createdAt,
    Instant updatedAt) {

  public static TransferResponse from(Transfer transfer) {
    return new TransferResponse(
        transfer.getId(),
        transfer.getFromAccountId(),
        transfer.getToAccountId(),
        transfer.getAmount(),
        transfer.getStatus(),
        transfer.getCreatedAt(),
        transfer.getUpdatedAt());
  }
}
