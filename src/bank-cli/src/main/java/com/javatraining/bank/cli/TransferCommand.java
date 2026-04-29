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
    name = "transfer",
    description = "Fund transfer commands",
    subcommands = {
      TransferCommand.List.class,
      TransferCommand.Create.class,
      TransferCommand.Approve.class,
      HelpCommand.class
    },
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
      // TODO (Bonus Quest 2): implement authenticated transfer
      // 1. Call requireToken() — return 1 if null
      // 2. Build JSON body: {"fromAccountId":"...","toAccountId":"...","amount":...}
      // 3. POST /v1/transfers with Content-Type: application/json and Authorization: Bearer <token>
      //    Pattern: AccountCommand.Create.call()
      // 4. Print the response body
      // 5. On 401: print "token missing or expired" message
      //    On 403: print "missing transfers:write scope" message
      // 6. Return 0 on success, 1 on any 4xx/5xx
      System.err.println("Not yet implemented — complete Bonus Quest 2.");
      return 1;
    }
  }

  // ── Bonus Quest: Approve a high-value transfer ────────────────────────────

  @Command(
      name = "approve",
      description = "Send an approval signal to a pending high-value durable transfer",
      mixinStandardHelpOptions = true)
  static class Approve implements Callable<Integer> {

    @Parameters(index = "0", description = "Workflow ID (e.g. transfer-<uuid>)")
    private String workflowId;

    @Override
    public Integer call() throws Exception {
      var token = requireToken();
      if (token == null) return 1;

      // TODO (Bonus Quest): Send the approval signal via POST
      // /v1/durable-transfers/{workflowId}/approve
      // 1. Build a POST request to API_URL + "/v1/durable-transfers/" + workflowId +
      // "/signal/approve"
      //    with the bearer token.
      // 2. Print the response body.
      // 3. On 404, print "Workflow not found or already completed".
      // 4. On 401/403, print the appropriate token error message (see TransferCommand.Create).
      //
      // Pattern: TransferCommand.Create.call()
      System.err.println("Not yet implemented — complete the Bonus Quest.");
      return 1;
    }
  }
}
