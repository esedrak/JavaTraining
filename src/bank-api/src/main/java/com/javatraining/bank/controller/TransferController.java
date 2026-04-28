package com.javatraining.bank.controller;

import com.javatraining.bank.domain.Transfer;
import com.javatraining.bank.domain.exception.AccountNotFoundException;
import com.javatraining.bank.domain.exception.InsufficientFundsException;
import com.javatraining.bank.domain.exception.TransferNotFoundException;
import com.javatraining.bank.dto.CreateTransferRequest;
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
@RequestMapping("/v1/transfers")
@Tag(name = "Transfers", description = "Fund transfer operations")
@SecurityRequirement(name = "bearerAuth")
public class TransferController {

  private static final Logger log = LoggerFactory.getLogger(TransferController.class);

  private final BankService bankService;

  public TransferController(BankService bankService) {
    this.bankService = bankService;
  }

  @GetMapping
  @Operation(summary = "List all transfers")
  @ApiResponse(responseCode = "200", description = "List of transfers")
  @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token")
  public ResponseEntity<List<Transfer>> listTransfers() {
    return ResponseEntity.ok(bankService.listTransfers());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a transfer by ID")
  @ApiResponse(responseCode = "200", description = "Transfer found")
  @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token")
  @ApiResponse(responseCode = "404", description = "Transfer not found")
  public ResponseEntity<?> getTransfer(@PathVariable UUID id) {
    try {
      return ResponseEntity.ok(bankService.getTransfer(id));
    } catch (TransferNotFoundException ex) {
      return ResponseEntity.status(404).body(Map.of("message", ex.getMessage()));
    }
  }

  // ── Quest 1 ───────────────────────────────────────────────────────────────────
  // Add @ApiResponse annotations for ALL seven possible response codes.
  // AccountController.createAccount is your reference — it documents 201, 400, and 403.
  // Add the missing four: 401, 404, 422, and 500.
  //
  // Quest 2 ───────────────────────────────────────────────────────────────────
  // Add a scope check for "transfers:write" authority.
  // See AccountController.createAccount lines 69-75 for the exact pattern.
  //
  // Quest 3 ───────────────────────────────────────────────────────────────────
  // Replace the 501 stub below with the full implementation:
  //   TODO 1 – Scope check:      see AccountController.createAccount:69
  //   TODO 2 – Ownership check:  fetch the source account, compare auth.getName() to owner
  //   TODO 3 – Call service:     bankService.createTransfer(fromId, toId, amount)
  //   TODO 4 – Map exceptions:   AccountNotFoundException→404, InsufficientFundsException→422,
  //                              IllegalArgumentException→400; let everything else propagate
  //   TODO 5 – Log & return:     log.info, ResponseEntity.created(location).body(transfer)
  @PostMapping
  @Operation(summary = "Create a new transfer between accounts")
  // Quest 1: add the missing @ApiResponse annotations below ↓
  @ApiResponse(responseCode = "201", description = "Transfer created")
  @ApiResponse(responseCode = "400", description = "Validation error or invalid argument")
  @ApiResponse(responseCode = "403", description = "Missing transfers:write scope or caller is not the source account owner")
  public ResponseEntity<?> createTransfer(
      @RequestBody CreateTransferRequest request, Authentication auth) {

    // TODO 1 (Quest 2) – Scope check
    // Pattern: AccountController.createAccount:69-75
    // boolean hasScope = auth.getAuthorities().stream()
    //     .anyMatch(a -> a.getAuthority().equals("SCOPE_transfers:write"));
    // if (!hasScope) return ResponseEntity.status(403).body(...);

    // TODO 2 (Quest 3) – Ownership check
    // Fetch the source account and verify the caller owns it:
    //   var sourceAccount = bankService.getAccount(request.fromAccountId());
    //   if (!auth.getName().equals(sourceAccount.getOwner())) return ResponseEntity.status(403)...;

    // TODO 3 (Quest 3) – Call service inside try/catch
    // TODO 4 (Quest 3) – Map exceptions
    //   AccountNotFoundException      → 404
    //   InsufficientFundsException    → 422  (ResponseEntity.unprocessableEntity())
    //   IllegalArgumentException      → 400
    //   all others                    → rethrow (let Spring's exception handler return 500)

    // TODO 5 (Quest 3) – Log & return 201
    //   log.info("Transfer created: {}", transfer.getId());
    //   var location = URI.create("/v1/transfers/" + transfer.getId());
    //   return ResponseEntity.created(location).body(transfer);

    return ResponseEntity.status(501).body(Map.of("message", "Not yet implemented — complete Quest 3."));
  }
}
