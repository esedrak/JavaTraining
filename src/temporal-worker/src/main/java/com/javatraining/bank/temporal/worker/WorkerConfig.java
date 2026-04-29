package com.javatraining.bank.temporal.worker;

import com.javatraining.bank.temporal.activities.TransferActivitiesImpl;
import com.javatraining.bank.temporal.impl.DurableTransferWorkflowImpl;
import com.javatraining.bank.temporal.workflow.DurableTransferWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wires up the Temporal {@link WorkflowClient}, {@link WorkerFactory}, and the single worker that
 * polls {@link DurableTransferWorkflow#TASK_QUEUE}.
 *
 * <h2>Quest 7</h2>
 *
 * This class is pre-wired. Run {@code make run-worker} and verify the worker appears in the
 * Temporal Web UI at <a href="http://localhost:8233">http://localhost:8233</a>.
 */
@Configuration
public class WorkerConfig {

  private static final Logger log = LoggerFactory.getLogger(WorkerConfig.class);

  @Value("${temporal.service-address:localhost:7233}")
  private String serviceAddress;

  @Value("${temporal.namespace:default}")
  private String namespace;

  @Bean
  public WorkflowServiceStubs workflowServiceStubs() {
    return WorkflowServiceStubs.newServiceStubs(
        WorkflowServiceStubsOptions.newBuilder().setTarget(serviceAddress).build());
  }

  @Bean
  public WorkflowClient workflowClient(WorkflowServiceStubs stubs) {
    return WorkflowClient.newInstance(
        stubs, WorkflowClientOptions.newBuilder().setNamespace(namespace).build());
  }

  @Bean
  public WorkerFactory workerFactory(
      WorkflowClient client,
      DurableTransferWorkflowImpl workflowImpl,
      TransferActivitiesImpl activitiesImpl) {

    WorkerFactory factory = WorkerFactory.newInstance(client);
    Worker worker = factory.newWorker(DurableTransferWorkflow.TASK_QUEUE);

    // Quest 7: register your implementation classes here
    worker.registerWorkflowImplementationTypes(DurableTransferWorkflowImpl.class);
    worker.registerActivitiesImplementations(activitiesImpl);

    log.info("Worker registered on task queue '{}'", DurableTransferWorkflow.TASK_QUEUE);
    factory.start();
    return factory;
  }
}
