package com.javatraining.bank.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransferRequest(UUID fromAccountId, UUID toAccountId, BigDecimal amount) {}
