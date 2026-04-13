package com.javatraining.bank.domain.exception;

import java.math.BigDecimal;
import java.util.UUID;

/** Thrown when a withdrawal or transfer would result in a negative balance. */
public class InsufficientFundsException extends RuntimeException {

  private final UUID accountId;
  private final BigDecimal balance;
  private final BigDecimal requested;

  public InsufficientFundsException(UUID accountId, BigDecimal balance, BigDecimal requested) {
    super(
        String.format(
            "Account %s has insufficient funds: balance=%.2f, requested=%.2f",
            accountId, balance, requested));
    this.accountId = accountId;
    this.balance = balance;
    this.requested = requested;
  }

  public UUID getAccountId() {
    return accountId;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public BigDecimal getRequested() {
    return requested;
  }
}
