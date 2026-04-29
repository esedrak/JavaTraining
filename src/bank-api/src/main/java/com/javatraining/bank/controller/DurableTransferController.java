package com.javatraining.bank.controller;

import com.javatraining.bank.dto.CreateTransferRequest;
import com.javatraining.bank.temporal.dto.TransferInput;
import com.javatraining.bank.temporal.workflow.DurableTransferWorkflow;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Starts a durable bank transfer via Temporal.
 *
 * <h2>Quest 6 — implement this controller</h2>
 *
 * The method body has four TODO markers. Follow them in order. Use {@link TransferController} as
 * your reference for scope checking and error handling patterns.
 *
 * <h3>Deterministic Workflow ID</h3>
 *
 * Temporal uses the Workflow ID as an idempotency key. Start the workflow with:
 *
 * <pre>
 * WorkflowOptions options = WorkflowOptions.newBuilder()
 *     .setWorkflowId("transfer-" + input.transferId())
 *     .setTaskQueue(DurableTransferWorkflow.TASK_QUEUE)
 *     .build();
 * </pre>
 *
 * A second {@code POST /v1/durable-transfers} with the same body will return the existing workflow
 * run rather than starting a duplicate — Temporal enforces this guarantee automatically.
 */
@RestController
@RequestMapping("/v1/durable-transfers")
@ConditionalOnBean(WorkflowClient.class)
@Tag(name = "Durable Transfers", description = "Fault-tolerant transfers orchestrated by Temporal")
@SecurityRequirement(name = "bearerAuth")
public class DurableTransferController {

  private static final Logger log = LoggerFactory.getLogger(DurableTransferController.class);

  @Autowired(required = false)
  private WorkflowClient workflowClient;

  // Quest 6: add the missing @ApiResponse annotations (201, 400, 401, 403, 500)
  @PostMapping
  @Operation(summary = "Start a durable transfer workflow")
  @ApiResponse(responseCode = "202", description = "Workflow accepted and started")
  public ResponseEntity<?> startDurableTransfer(
      @RequestBody CreateTransferRequest request, Authentication auth) {

    // TODO 1 (Quest 6) — Scope check: require "transfers:write"
    //   Pattern: TransferController.createTransfer TODO 1

    // TODO 2 (Quest 6) — Build TransferInput
    //   var input = new TransferInput(
    //       UUID.randomUUID(),           // transferId — becomes part of the Workflow ID
    //       request.fromAccountId(),
    //       request.toAccountId(),
    //       request.amount(),
    //       "api-initiated");

    // TODO 3 (Quest 6) — Start the workflow
    //   var options = WorkflowOptions.newBuilder()
    //       .setWorkflowId("transfer-" + input.transferId())
    //       .setTaskQueue(DurableTransferWorkflow.TASK_QUEUE)
    //       .build();
    //   var stub = workflowClient.newWorkflowStub(DurableTransferWorkflow.class, options);
    //   WorkflowClient.start(stub::execute, input);
    //   log.info("Started durable transfer workflow {}", input.transferId());

    // TODO 4 (Quest 6) — Return 202 Accepted with the workflowId so the caller can poll / signal
    //   return ResponseEntity.accepted()
    //       .body(Map.of("workflowId", "transfer-" + input.transferId()));

    return ResponseEntity.status(501)
        .body(Map.of("message", "Not yet implemented — complete Quest 6."));
  }
}
