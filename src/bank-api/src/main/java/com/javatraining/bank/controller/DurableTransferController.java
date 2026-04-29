package com.javatraining.bank.controller;

import com.javatraining.bank.dto.CreateTransferRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.temporal.client.WorkflowClient;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Starts and signals durable bank transfer workflows via Temporal.
 *
 * <h2>Quest 6 — implement this controller</h2>
 *
 * Use {@link AccountController} and {@link TransferController} as your reference for patterns:
 * {@code @PreAuthorize}, constructor injection, {@code ResponseEntity<T>}.
 *
 * <h3>Deterministic Workflow ID</h3>
 *
 * Temporal uses the Workflow ID as an idempotency key. A second {@code POST /v1/durable-transfers}
 * with the same {@code transferId} will reuse the existing workflow run rather than starting a
 * duplicate — Temporal enforces this automatically.
 */
@RestController
@RequestMapping("/v1/durable-transfers")
@ConditionalOnBean(WorkflowClient.class)
@Tag(name = "Durable Transfers", description = "Fault-tolerant transfers orchestrated by Temporal")
@SecurityRequirement(name = "bearerAuth")
public class DurableTransferController {

  private static final Logger log = LoggerFactory.getLogger(DurableTransferController.class);

  // TODO 1 (Quest 6) — constructor injection
  // Replace this field with a constructor that receives WorkflowClient.
  // Pattern: AccountController constructor injection.
  private final WorkflowClient workflowClient;

  public DurableTransferController(WorkflowClient workflowClient) {
    this.workflowClient = workflowClient;
  }

  // Quest 6: add @PreAuthorize("@bankPermission.canCreateTransfer(authentication)") below ↓
  @PostMapping
  @Operation(summary = "Start a durable transfer workflow")
  public ResponseEntity<Map<String, String>> startDurableTransfer(
      @RequestBody CreateTransferRequest request, Authentication auth) {

    // TODO 2 (Quest 6) — Build TransferInput
    //   var input = new TransferInput(
    //       UUID.randomUUID(),          // transferId — becomes part of the Workflow ID
    //       request.fromAccountId(),
    //       request.toAccountId(),
    //       request.amount(),
    //       "api-initiated");

    // TODO 3 (Quest 6) — Start the workflow asynchronously (use WorkflowClient.start, not execute)
    //   var options = WorkflowOptions.newBuilder()
    //       .setWorkflowId("transfer-" + input.transferId())
    //       .setTaskQueue(DurableTransferWorkflow.TASK_QUEUE)
    //       .build();
    //   var stub = workflowClient.newWorkflowStub(DurableTransferWorkflow.class, options);
    //   WorkflowClient.start(stub::execute, input);
    //   log.info("Started durable transfer workflow {}", input.transferId());

    // TODO 4 (Quest 6) — Return 202 Accepted with the workflowId
    //   return ResponseEntity.accepted()
    //       .body(Map.of("workflowId", "transfer-" + input.transferId()));

    throw new UnsupportedOperationException("Not yet implemented — complete Quest 6.");
  }

  // Quest 6: add @PreAuthorize("@bankPermission.canCreateTransfer(authentication)") below ↓
  @PostMapping("/{workflowId}/signal/approve")
  @Operation(summary = "Send approval signal to a pending high-value transfer")
  public ResponseEntity<Void> approveDurableTransfer(@PathVariable String workflowId) {

    // TODO 5 (Quest 6) — Send the approve() signal to the running workflow
    //   var stub = workflowClient.newWorkflowStub(DurableTransferWorkflow.class, workflowId);
    //   stub.approve();
    //   log.info("Sent approve signal to workflow {}", workflowId);
    //   return ResponseEntity.ok().build();
    //
    //   On WorkflowNotFoundException: return ResponseEntity.notFound().build()

    throw new UnsupportedOperationException("Not yet implemented — complete Quest 6.");
  }
}
