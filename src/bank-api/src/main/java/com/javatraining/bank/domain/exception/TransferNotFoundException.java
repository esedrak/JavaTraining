package com.javatraining.bank.domain.exception;

import java.util.UUID;

/** Thrown when a transfer cannot be found by its ID. */
public class TransferNotFoundException extends RuntimeException {

  private final UUID transferId;

  public TransferNotFoundException(UUID transferId) {
    super(String.format("Transfer '%s' not found.", transferId));
    this.transferId = transferId;
  }

  public UUID getTransferId() {
    return transferId;
  }
}
