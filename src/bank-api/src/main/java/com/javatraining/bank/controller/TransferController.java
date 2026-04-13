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
  public ResponseEntity<List<Transfer>> listTransfers() {
    return ResponseEntity.ok(bankService.listTransfers());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a transfer by ID")
  @ApiResponse(responseCode = "200", description = "Transfer found")
  @ApiResponse(responseCode = "404", description = "Transfer not found")
  public ResponseEntity<?> getTransfer(@PathVariable UUID id) {
    try {
      return ResponseEntity.ok(bankService.getTransfer(id));
    } catch (TransferNotFoundException ex) {
      return ResponseEntity.status(404).body(Map.of("message", ex.getMessage()));
    }
  }

  @PostMapping
  @Operation(summary = "Create a new transfer between accounts")
  @ApiResponse(responseCode = "201", description = "Transfer created")
  @ApiResponse(responseCode = "400", description = "Validation error or insufficient funds")
  @ApiResponse(responseCode = "403", description = "Missing transfers:write scope")
  @ApiResponse(responseCode = "404", description = "Account not found")
  public ResponseEntity<?> createTransfer(
      @RequestBody CreateTransferRequest request, Authentication auth) {

    // TODO (Bank Transfer Quest 3): implement scope check and full error mapping
    // For now, return 501 so the quest implementation is clearly missing
    return ResponseEntity.status(501).body(Map.of("message", "Not yet implemented."));
  }

  /**
   * Completed implementation — used once the quest is solved.
   *
   * <p>Students should implement {@link #createTransfer} by following the pattern from {@link
   * AccountController#createAccount}.
   */
  ResponseEntity<?> createTransferImpl(CreateTransferRequest request, Authentication auth) {
    boolean hasScope =
        auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("SCOPE_transfers:write"));

    if (!hasScope) {
      return ResponseEntity.status(403)
          .body(Map.of("message", "Missing required scope: transfers:write"));
    }

    try {
      var transfer =
          bankService.createTransfer(
              request.fromAccountId(), request.toAccountId(), request.amount());
      var location = URI.create("/v1/transfers/" + transfer.getId());
      return ResponseEntity.created(location).body(transfer);
    } catch (AccountNotFoundException ex) {
      return ResponseEntity.status(404).body(Map.of("message", ex.getMessage()));
    } catch (InsufficientFundsException ex) {
      log.warn("Transfer rejected: {}", ex.getMessage());
      return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
  }
}
