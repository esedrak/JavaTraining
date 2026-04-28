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
      var token = requireToken();
      if (token == null) return 1;

      var body =
          String.format(
              "{\"fromAccountId\":\"%s\",\"toAccountId\":\"%s\",\"amount\":%s}",
              fromAccountId, toAccountId, amount);

      var request =
          withBearer(
                  HttpRequest.newBuilder()
                      .uri(URI.create(API_URL + "/v1/transfers"))
                      .POST(HttpRequest.BodyPublishers.ofString(body))
                      .header("Content-Type", "application/json"),
                  token)
              .build();

      var response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println(response.body());

      if (response.statusCode() == 401) {
        System.err.println("Error: token missing or expired — re-run the POST /v1/token command and re-export BANK_TOKEN.");
        return 1;
      }
      if (response.statusCode() == 403) {
        System.err.println("Error: token lacks the 'transfers:write' scope.");
        return 1;
      }
      return response.statusCode() < 400 ? 0 : 1;
    }
  }
}
