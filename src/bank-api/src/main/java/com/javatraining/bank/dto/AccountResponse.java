package com.javatraining.bank.dto;

import com.javatraining.bank.domain.Account;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AccountResponse(
    UUID id, String owner, BigDecimal balance, Instant createdAt, Instant updatedAt) {

  public static AccountResponse from(Account account) {
    return new AccountResponse(
        account.getId(),
        account.getOwner(),
        account.getBalance(),
        account.getCreatedAt(),
        account.getUpdatedAt());
  }
}
