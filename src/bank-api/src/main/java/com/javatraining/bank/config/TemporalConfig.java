package com.javatraining.bank.config;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides a {@link WorkflowClient} bean when Temporal is enabled.
 *
 * <p>Set {@code temporal.enabled=true} (or {@code TEMPORAL_ENABLED=true}) to activate. This keeps
 * the bank-api bootable without a running Temporal server during development and testing.
 *
 * <h2>Quest 6</h2>
 *
 * Ensure {@code temporal.enabled=true} is set in your local {@code application.yml} or environment
 * before testing the {@code POST /v1/durable-transfers} endpoint.
 */
@Configuration
@ConditionalOnProperty(name = "temporal.enabled", havingValue = "true")
public class TemporalConfig {

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
}
