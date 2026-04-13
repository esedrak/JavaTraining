package com.javatraining.bank.domain.exception;

import java.util.UUID;

/** Thrown when an account cannot be found by its ID. */
public class AccountNotFoundException extends RuntimeException {

  private final UUID accountId;

  public AccountNotFoundException(UUID accountId) {
    super(String.format("Account '%s' not found.", accountId));
    this.accountId = accountId;
  }

  public UUID getAccountId() {
    return accountId;
  }
}
