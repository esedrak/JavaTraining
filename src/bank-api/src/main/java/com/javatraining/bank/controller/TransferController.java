package com.javatraining.bank.controller;

import com.javatraining.bank.dto.CreateTransferRequest;
import com.javatraining.bank.dto.TransferResponse;
import com.javatraining.bank.service.BankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
  public ResponseEntity<List<TransferResponse>> listTransfers() {
    return ResponseEntity.ok(
        bankService.listTransfers().stream().map(TransferResponse::from).toList());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a transfer by ID")
  public ResponseEntity<TransferResponse> getTransfer(@PathVariable UUID id) {
    return ResponseEntity.ok(TransferResponse.from(bankService.getTransfer(id)));
  }

  // ── Quest 1 ───────────────────────────────────────────────────────────────────
  // Add any missing OpenAPI @Operation / @Tag documentation you think is useful.
  //
  // Quest 2 ───────────────────────────────────────────────────────────────────
  // Add @PreAuthorize("@bankPermission.canCreateTransfer(authentication)") to this method.
  // See AccountController.createAccount and BankPermission for the pattern.
  //
  // Quest 3 ───────────────────────────────────────────────────────────────────
  // Replace the 501 stub with the full implementation:
  //   TODO 1 – Ownership check: fetch the source account, compare auth.getName() to owner
  //            var source = bankService.getAccount(request.fromAccountId());
  //            if (!auth.getName().equals(source.getOwner())) return ResponseEntity.status(403)...;
  //   TODO 2 – Call service:    bankService.createTransfer(fromId, toId, amount)
  //            Let AccountNotFoundException, InsufficientFundsException, and
  //            IllegalArgumentException propagate — GlobalExceptionHandler maps them.
  //   TODO 3 – Log & return:    log.info,
  // ResponseEntity.created(location).body(TransferResponse.from(transfer))
  @PostMapping
  @Operation(summary = "Create a new transfer between accounts")
  // Quest 2: add @PreAuthorize("@bankPermission.canCreateTransfer(authentication)") here ↓
  public ResponseEntity<TransferResponse> createTransfer(
      @RequestBody CreateTransferRequest request, Authentication auth) {

    // TODO 1 (Quest 3) – Ownership check
    // TODO 2 (Quest 3) – Call service (exceptions propagate to GlobalExceptionHandler)
    // TODO 3 (Quest 3) – Log & return 201
    //   log.info("Transfer created: {}", transfer.getId());
    //   var location = URI.create("/v1/transfers/" + transfer.getId());
    //   return ResponseEntity.created(location).body(TransferResponse.from(transfer));

    throw new UnsupportedOperationException("Not yet implemented — complete Quest 3.");
  }
}
