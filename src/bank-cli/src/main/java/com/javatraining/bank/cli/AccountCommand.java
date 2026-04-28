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
import picocli.CommandLine.Parameters;

@Command(
    name = "account",
    description = "Account management commands",
    subcommands = {
      AccountCommand.List.class,
      AccountCommand.Get.class,
      AccountCommand.Create.class,
      HelpCommand.class
    },
    mixinStandardHelpOptions = true)
public class AccountCommand {

  @Command(name = "list", description = "List all accounts", mixinStandardHelpOptions = true)
  static class List implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
      var token = requireToken();
      if (token == null) return 1;

      var request =
          withBearer(
                  HttpRequest.newBuilder().uri(URI.create(API_URL + "/v1/accounts")).GET(), token)
              .build();

      var response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println(response.body());
      return response.statusCode() < 400 ? 0 : 1;
    }
  }

  @Command(name = "get", description = "Get an account by ID", mixinStandardHelpOptions = true)
  static class Get implements Callable<Integer> {

    @Parameters(index = "0", description = "Account UUID")
    private String id;

    @Override
    public Integer call() throws Exception {
      var token = requireToken();
      if (token == null) return 1;

      var request =
          withBearer(
                  HttpRequest.newBuilder()
                      .uri(URI.create(API_URL + "/v1/accounts/" + id))
                      .GET(),
                  token)
              .build();

      var response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println(response.body());

      if (response.statusCode() == 404) {
        System.err.println("Error: account " + id + " not found.");
        return 1;
      }
      return response.statusCode() < 400 ? 0 : 1;
    }
  }

  @Command(name = "create", description = "Create a new account", mixinStandardHelpOptions = true)
  static class Create implements Callable<Integer> {

    @Parameters(index = "0", description = "Account owner name")
    private String owner;

    @Option(
        names = {"--balance", "-b"},
        description = "Initial balance (default: 0)",
        defaultValue = "0")
    private String balance;

    @Override
    public Integer call() throws Exception {
      var token = requireToken();
      if (token == null) return 1;

      var body =
          String.format(
              "{\"owner\":\"%s\",\"initialBalance\":%s}", owner.replace("\"", "\\\""), balance);

      var request =
          withBearer(
                  HttpRequest.newBuilder()
                      .uri(URI.create(API_URL + "/v1/accounts"))
                      .POST(HttpRequest.BodyPublishers.ofString(body))
                      .header("Content-Type", "application/json"),
                  token)
              .build();

      var response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println(response.body());

      if (response.statusCode() == 403) {
        System.err.println("Error: token missing 'accounts:write' scope.");
        return 1;
      }
      return response.statusCode() < 400 ? 0 : 1;
    }
  }
}
