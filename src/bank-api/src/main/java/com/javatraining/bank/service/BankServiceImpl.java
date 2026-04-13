package com.javatraining.bank.service;

import com.javatraining.bank.domain.Account;
import com.javatraining.bank.domain.Transfer;
import com.javatraining.bank.domain.TransferStatus;
import com.javatraining.bank.domain.exception.AccountNotFoundException;
import com.javatraining.bank.domain.exception.InsufficientFundsException;
import com.javatraining.bank.domain.exception.TransferNotFoundException;
import com.javatraining.bank.repository.AccountRepository;
import com.javatraining.bank.repository.TransferRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankServiceImpl implements BankService {

  private static final Logger log = LoggerFactory.getLogger(BankServiceImpl.class);

  private final AccountRepository accountRepository;
  private final TransferRepository transferRepository;

  public BankServiceImpl(
      AccountRepository accountRepository, TransferRepository transferRepository) {
    this.accountRepository = accountRepository;
    this.transferRepository = transferRepository;
  }

  @Override
  @Transactional
  public Account createAccount(String owner, BigDecimal initialBalance) {
    if (owner == null || owner.isBlank()) {
      throw new IllegalArgumentException("Owner must not be blank");
    }
    if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Initial balance must not be negative");
    }

    var account = new Account();
    account.setOwner(owner);
    account.setBalance(initialBalance);
    var saved = accountRepository.save(account);
    log.info("Created account {} for owner '{}'", saved.getId(), owner);
    return saved;
  }

  @Override
  public Account getAccount(UUID id) {
    return accountRepository
        .findById(id)
        .orElseThrow(
            () -> {
              log.warn("Account {} not found", id);
              return new AccountNotFoundException(id);
            });
  }

  @Override
  public List<Account> listAccounts() {
    return accountRepository.findAllByOrderByCreatedAtAsc();
  }

  @Override
  @Transactional
  public Transfer createTransfer(UUID fromId, UUID toId, BigDecimal amount) {
    var from = getAccount(fromId);
    var to = getAccount(toId);

    // Business rule: balance check lives in the service layer (anemic domain model)
    if (from.getBalance().compareTo(amount) < 0) {
      throw new InsufficientFundsException(from.getId(), from.getBalance(), amount);
    }

    // Update balances — JPA dirty-checking will auto-save the managed entities on commit
    from.setBalance(from.getBalance().subtract(amount));
    to.setBalance(to.getBalance().add(amount));

    // Record the transfer
    var transfer = new Transfer();
    transfer.setFromAccountId(from.getId());
    transfer.setToAccountId(to.getId());
    transfer.setAmount(amount);
    transfer.setStatus(TransferStatus.COMPLETED);

    // @Transactional: if an exception is thrown before commit, all changes roll back automatically
    var saved = transferRepository.save(transfer);
    log.info("Transfer {} completed: {} → {} for amount {}", saved.getId(), fromId, toId, amount);
    return saved;
  }

  @Override
  public Transfer getTransfer(UUID id) {
    return transferRepository
        .findById(id)
        .orElseThrow(
            () -> {
              log.warn("Transfer {} not found", id);
              return new TransferNotFoundException(id);
            });
  }

  @Override
  public List<Transfer> listTransfers() {
    return transferRepository.findAllByOrderByCreatedAtDesc();
  }
}
