package com.javatraining.bank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.javatraining.bank.domain.Account;
import com.javatraining.bank.domain.Transfer;
import com.javatraining.bank.domain.TransferStatus;
import com.javatraining.bank.domain.exception.AccountNotFoundException;
import com.javatraining.bank.domain.exception.InsufficientFundsException;
import com.javatraining.bank.repository.AccountRepository;
import com.javatraining.bank.repository.TransferRepository;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("BankService")
class BankServiceTest {

  @Mock private AccountRepository accountRepository;
  @Mock private TransferRepository transferRepository;

  private BankService sut;

  @BeforeEach
  void setUp() {
    sut = new BankServiceImpl(accountRepository, transferRepository);
  }

  private static Account account(String owner, String balance) {
    var a = new Account();
    ReflectionTestUtils.setField(a, "id", UUID.randomUUID());
    a.setOwner(owner);
    a.setBalance(new BigDecimal(balance));
    return a;
  }

  @Nested
  @DisplayName("createAccount")
  class CreateAccount {

    @Test
    @DisplayName("returns account with correct owner and balance")
    void returnsAccountWithCorrectOwnerAndBalance() {
      when(accountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

      var result = sut.createAccount("Alice", new BigDecimal("500.00"));

      assertThat(result.getOwner()).isEqualTo("Alice");
      assertThat(result.getBalance()).isEqualByComparingTo("500.00");
    }

    @Test
    @DisplayName("persists the account via repository")
    void persistsAccount() {
      when(accountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

      sut.createAccount("Bob", BigDecimal.ZERO);

      verify(accountRepository).save(any(Account.class));
    }
  }

  @Nested
  @DisplayName("getAccount")
  class GetAccount {

    @Test
    @DisplayName("returns account when found")
    void returnsAccountWhenFound() {
      var a = account("Alice", "100.00");
      when(accountRepository.findById(a.getId())).thenReturn(Optional.of(a));

      var result = sut.getAccount(a.getId());

      assertThat(result).isSameAs(a);
    }

    @Test
    @DisplayName("throws AccountNotFoundException when not found")
    void throwsWhenNotFound() {
      var id = UUID.randomUUID();
      when(accountRepository.findById(id)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> sut.getAccount(id))
          .isInstanceOf(AccountNotFoundException.class)
          .hasMessageContaining(id.toString());
    }
  }

  @Nested
  @DisplayName("createTransfer")
  class CreateTransfer {

    @Test
    @DisplayName("completes transfer and updates both account balances")
    void completesTransferAndUpdatesBalances() {
      var from = account("Alice", "1000.00");
      var to = account("Bob", "200.00");

      when(accountRepository.findById(from.getId())).thenReturn(Optional.of(from));
      when(accountRepository.findById(to.getId())).thenReturn(Optional.of(to));
      when(transferRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

      var result = sut.createTransfer(from.getId(), to.getId(), new BigDecimal("300.00"));

      assertThat(result.getStatus()).isEqualTo(TransferStatus.COMPLETED);
      assertThat(from.getBalance()).isEqualByComparingTo("700.00");
      assertThat(to.getBalance()).isEqualByComparingTo("500.00");
    }

    @Test
    @DisplayName("throws InsufficientFundsException when balance too low")
    void throwsWhenInsufficientFunds() {
      var from = account("Alice", "50.00");
      var to = account("Bob", "0.00");

      when(accountRepository.findById(from.getId())).thenReturn(Optional.of(from));
      when(accountRepository.findById(to.getId())).thenReturn(Optional.of(to));

      assertThatThrownBy(
              () -> sut.createTransfer(from.getId(), to.getId(), new BigDecimal("100.00")))
          .isInstanceOf(InsufficientFundsException.class)
          .hasMessageContaining(from.getId().toString());
    }

    @Test
    @DisplayName("saves the completed transfer record")
    void savesTransfer() {
      var from = account("Alice", "500.00");
      var to = account("Bob", "100.00");

      when(accountRepository.findById(from.getId())).thenReturn(Optional.of(from));
      when(accountRepository.findById(to.getId())).thenReturn(Optional.of(to));
      when(transferRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

      sut.createTransfer(from.getId(), to.getId(), new BigDecimal("200.00"));

      verify(transferRepository).save(any(Transfer.class));
    }
  }
}
