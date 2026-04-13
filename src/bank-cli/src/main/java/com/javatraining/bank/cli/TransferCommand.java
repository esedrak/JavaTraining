package com.javatraining.bank.cli;

import static com.javatraining.bank.cli.HttpClientFactory.API_URL;
import static com.javatraining.bank.cli.HttpClientFactory.CLIENT;
import static com.javatraining.bank.cli.HttpClientFactory.requireToken;
import static com.javatraining.bank.cli.HttpClientFactory.withBearer;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Option;

@Command(
    name = "transfer",
    description = "Fund transfer commands",
    subcommands = {TransferCommand.List.class, TransferCommand.Create.class, HelpCommand.class},
    mixinStandardHelpOptions = true)
public class TransferCommand {

  @Command(name = "list", description = "List all transfers", mixinStandardHelpOptions = true)
  static class List implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
      var token = requireToken();
      if (token == null) return 1;

      var request =
          withBearer(
                  HttpRequest.newBuilder().uri(URI.create(API_URL + "/v1/transfers")).GET(), token)
              .build();

      var response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println(response.body());
      return response.statusCode() < 400 ? 0 : 1;
    }
  }

  @Command(
      name = "create",
      description = "Create a transfer between two accounts",
      mixinStandardHelpOptions = true)
  static class Create implements Callable<Integer> {

    @Option(
        names = {"--from", "-f"},
        description = "Source account UUID",
        required = true)
    private String fromAccountId;

    @Option(
        names = {"--to", "-t"},
        description = "Destination account UUID",
        required = true)
    private String toAccountId;

    @Option(
        names = {"--amount", "-a"},
        description = "Amount to transfer",
        required = true)
    private String amount;

    @Override
    public Integer call() throws Exception {
      // TODO (Bank Transfer Quest Bonus 2): implement authenticated transfer
      // 1. Require BANK_TOKEN with transfers:write scope
      // 2. POST /v1/transfers with fromAccountId, toAccountId, amount
      // 3. Print result or friendly error (401, 403, 400, 404)
      System.err.println("Not yet implemented — complete Bonus Quest 2.");
      return 1;
    }
  }
}
