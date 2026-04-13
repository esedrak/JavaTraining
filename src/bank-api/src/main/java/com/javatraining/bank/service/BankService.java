package com.javatraining.bank.service;

import com.javatraining.bank.domain.Account;
import com.javatraining.bank.domain.Transfer;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/** Business operations for the bank domain. */
public interface BankService {

  Account createAccount(String owner, BigDecimal initialBalance);

  Account getAccount(UUID id);

  List<Account> listAccounts();

  Transfer createTransfer(UUID fromId, UUID toId, BigDecimal amount);

  Transfer getTransfer(UUID id);

  List<Transfer> listTransfers();
}
