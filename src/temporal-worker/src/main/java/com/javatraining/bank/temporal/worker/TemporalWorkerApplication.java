package com.javatraining.bank.temporal.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Entry point for the Temporal worker process. */
@SpringBootApplication
public class TemporalWorkerApplication {

  public static void main(String[] args) {
    SpringApplication.run(TemporalWorkerApplication.class, args);
  }
}
