package com.javatraining.bank.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Entry point for the JavaBank CLI.
 *
 * <p>Usage:
 *
 * <pre>{@code
 * export BANK_API_URL=http://localhost:8080
 * export BANK_TOKEN=<jwt-token-from-POST-/v1/token>
 *
 * ./gradlew :src:bank-cli:run --args="account list"
 * ./gradlew :src:bank-cli:run --args="account create alice --balance 500"
 * ./gradlew :src:bank-cli:run --args="transfer list"
 * }</pre>
 */
@Command(
    name = "bank-cli",
    description = "JavaBank CLI — interact with the Bank API",
    subcommands = {AccountCommand.class, TransferCommand.class, CommandLine.HelpCommand.class},
    mixinStandardHelpOptions = true,
    version = "0.1.0")
public class BankCli {

  public static void main(String[] args) {
    int exitCode = new CommandLine(new BankCli()).execute(args);
    System.exit(exitCode);
  }
}
