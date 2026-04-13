package com.javatraining.basics.errorhandling;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ErrorHandlingTest {

  private ErrorHandling errorHandling;

  @BeforeEach
  void setUp() {
    errorHandling = new ErrorHandling();
  }

  @Test
  void parseInt_validInput_returnsOptionalOfValue() {
    Optional<Integer> result = errorHandling.parseInt("42");
    assertThat(result).isPresent().hasValue(42);
  }

  @Test
  void parseInt_invalidInput_returnsEmpty() {
    Optional<Integer> result = errorHandling.parseInt("abc");
    assertThat(result).isEmpty();
  }

  @Test
  void insufficientFundsException_hasCorrectShortfall() {
    ErrorHandling.InsufficientFundsException ex =
        new ErrorHandling.InsufficientFundsException(150.75);
    assertThat(ex.getShortfall()).isEqualTo(150.75);
    assertThat(ex.getMessage()).contains("150.75");
  }

  @Test
  void insufficientFundsException_isInstanceOfBankException() {
    ErrorHandling.InsufficientFundsException ex =
        new ErrorHandling.InsufficientFundsException(50.0);
    assertThat(ex).isInstanceOf(ErrorHandling.BankException.class);
  }

  @Test
  void accountNotFoundException_isInstanceOfBankException() {
    ErrorHandling.AccountNotFoundException ex =
        new ErrorHandling.AccountNotFoundException("ACC-001");
    assertThat(ex).isInstanceOf(ErrorHandling.BankException.class);
    assertThat(ex.getMessage()).contains("ACC-001");
  }

  @Test
  void bankException_supportsMessageAndCauseConstructors() {
    RuntimeException cause = new RuntimeException("root cause");
    ErrorHandling.BankException ex = new ErrorHandling.BankException("bank error", cause);
    assertThat(ex.getMessage()).isEqualTo("bank error");
    assertThat(ex.getCause()).isSameAs(cause);
  }

  @Test
  void multiCatchDemo_ioPath_returnsCaughtMessage() {
    String result = errorHandling.multiCatchDemo(true, false);
    assertThat(result).startsWith("caught:").contains("IO");
  }

  @Test
  void multiCatchDemo_sqlPath_returnsCaughtMessage() {
    String result = errorHandling.multiCatchDemo(false, true);
    assertThat(result).startsWith("caught:").contains("SQL");
  }

  @Test
  void multiCatchDemo_noError_returnsNoError() {
    String result = errorHandling.multiCatchDemo(false, false);
    assertThat(result).isEqualTo("no error");
  }

  @Test
  void readFile_nonExistentPath_throwsIOException() {
    assertThatThrownBy(() -> errorHandling.readFile("/no/such/file/xyz.txt"))
        .isInstanceOf(IOException.class);
  }
}
