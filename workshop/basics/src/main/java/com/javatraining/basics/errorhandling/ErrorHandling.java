package com.javatraining.basics.errorhandling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Topic 12: Error Handling in Java
 *
 * <p>Demonstrates checked vs unchecked exceptions, multi-catch, custom exception hierarchies, and
 * the Optional pattern for safe value parsing.
 *
 * <p>Key Java concept: Checked exceptions (extend Exception) must be declared with 'throws' or
 * caught. Unchecked exceptions (extend RuntimeException) require neither.
 */
public class ErrorHandling {

  // -------------------------------------------------------------------------
  // Custom exception hierarchy (all unchecked — extend RuntimeException)
  // -------------------------------------------------------------------------

  /** Base banking exception — unchecked so callers are not forced to handle it. */
  public static class BankException extends RuntimeException {
    public BankException(String message) {
      super(message);
    }

    public BankException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  /** Thrown when a withdrawal exceeds the available balance. */
  public static class InsufficientFundsException extends BankException {
    private final double shortfall;

    public InsufficientFundsException(double shortfall) {
      super("Insufficient funds — shortfall: " + shortfall);
      this.shortfall = shortfall;
    }

    public double getShortfall() {
      return shortfall;
    }
  }

  /** Thrown when no account with the given id exists. */
  public static class AccountNotFoundException extends BankException {
    public AccountNotFoundException(String accountId) {
      super("Account not found: " + accountId);
    }
  }

  // -------------------------------------------------------------------------
  // Checked exception — must declare with 'throws'
  // -------------------------------------------------------------------------

  /**
   * Reads the content of a file.
   *
   * <p>Because {@link IOException} is a checked exception, the method signature must declare it
   * with {@code throws} so callers know they have to handle it.
   */
  public String readFile(String path) throws IOException {
    return Files.readString(Paths.get(path));
  }

  // -------------------------------------------------------------------------
  // Multi-catch
  // -------------------------------------------------------------------------

  /**
   * Demonstrates catching multiple unrelated exception types in a single catch block. Both {@link
   * IOException} and {@link SQLException} are caught and handled the same way.
   */
  public String multiCatchDemo(boolean throwIo, boolean throwSql) {
    try {
      if (throwIo) {
        throw new IOException("simulated IO error");
      }
      if (throwSql) {
        throw new SQLException("simulated SQL error");
      }
      return "no error";
    } catch (IOException | SQLException e) {
      // single handler for both checked exception types
      return "caught: " + e.getMessage();
    }
  }

  // -------------------------------------------------------------------------
  // Optional as a Result-like pattern
  // -------------------------------------------------------------------------

  /**
   * Safely parses a string to an integer.
   *
   * <p>Instead of letting a {@link NumberFormatException} propagate, we return an empty {@link
   * Optional} when the input is not a valid integer. This is idiomatic Java for "value may be
   * absent".
   */
  public Optional<Integer> parseInt(String s) {
    try {
      return Optional.of(Integer.parseInt(s));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }
}
