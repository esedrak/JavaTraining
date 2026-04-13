package com.javatraining.basics.httptesting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Minimal Spring Boot application entry point for the HttpTesting topic.
 *
 * <p>This class is provided for completeness; the {@code @WebMvcTest} tests do not require a
 * running server.
 */
@SpringBootApplication
public class GreetingApplication {

  public static void main(String[] args) {
    SpringApplication.run(GreetingApplication.class, args);
  }
}
