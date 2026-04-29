package com.javatraining.bank.controller;

import com.javatraining.bank.dto.AccountResponse;
import com.javatraining.bank.dto.CreateAccountRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import com.javatraining.bank.service.BankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<List<AccountResponse>> listAccounts() {
    return ResponseEntity.ok(bankService.listAccounts().stream().map(AccountResponse::from).toList());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get an account by ID")
  public ResponseEntity<AccountResponse> getAccount(@PathVariable UUID id) {
    return ResponseEntity.ok(AccountResponse.from(bankService.getAccount(id)));
  }

  @PostMapping
  @Operation(summary = "Create a new account")
  @PreAuthorize("@bankPermission.canCreateAccount(authentication)")
  public ResponseEntity<AccountResponse> createAccount(@RequestBody CreateAccountRequest request) {
    var account = bankService.createAccount(request.owner(), request.initialBalance());
    var location = URI.create("/v1/accounts/" + account.getId());
    log.info("Account created: {}", account.getId());
    return ResponseEntity.created(location).body(AccountResponse.from(account));
  }
}
