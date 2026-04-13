package com.javatraining.bank.dto;

import java.math.BigDecimal;

public record CreateAccountRequest(String owner, BigDecimal initialBalance) {

  public CreateAccountRequest {
    if (initialBalance == null) {
      initialBalance = BigDecimal.ZERO;
    }
  }
}
