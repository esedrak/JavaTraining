package com.javatraining.bank.controller;

import com.javatraining.bank.domain.Account;
import com.javatraining.bank.domain.exception.AccountNotFoundException;
import com.javatraining.bank.dto.CreateAccountRequest;
import com.javatraining.bank.service.BankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/accounts")
@Tag(name = "Accounts", description = "Account management operations")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

  private static final Logger log = LoggerFactory.getLogger(AccountController.class);

  private final BankService bankService;

  public AccountController(BankService bankService) {
    this.bankService = bankService;
  }

  @GetMapping
  @Operation(summary = "List all accounts")
  @ApiResponse(responseCode = "200", description = "List of accounts")
  public ResponseEntity<List<Account>> listAccounts() {
    return ResponseEntity.ok(bankService.listAccounts());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get an account by ID")
  @ApiResponse(responseCode = "200", description = "Account found")
  @ApiResponse(responseCode = "404", description = "Account not found")
  public ResponseEntity<?> getAccount(@PathVariable UUID id) {
    try {
      return ResponseEntity.ok(bankService.getAccount(id));
    } catch (AccountNotFoundException ex) {
      return ResponseEntity.status(404).body(Map.of("message", ex.getMessage()));
    }
  }

  @PostMapping
  @Operation(summary = "Create a new account")
  @ApiResponse(responseCode = "201", description = "Account created")
  @ApiResponse(responseCode = "400", description = "Validation error")
  @ApiResponse(responseCode = "403", description = "Missing accounts:write scope")
  public ResponseEntity<?> createAccount(
      @RequestBody CreateAccountRequest request, Authentication auth) {

    // Scope-based authorization: require accounts:write claim in JWT
    boolean hasScope =
        auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("SCOPE_accounts:write"));

    if (!hasScope) {
      return ResponseEntity.status(403)
          .body(Map.of("message", "Missing required scope: accounts:write"));
    }

    try {
      var account = bankService.createAccount(request.owner(), request.initialBalance());
      var location = URI.create("/v1/accounts/" + account.getId());
      return ResponseEntity.created(location).body(account);
    } catch (IllegalArgumentException ex) {
      log.warn("Invalid account creation request: {}", ex.getMessage());
      return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
  }
}
