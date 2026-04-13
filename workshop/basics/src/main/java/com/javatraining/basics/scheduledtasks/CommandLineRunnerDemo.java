package com.javatraining.basics.scheduledtasks;

import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Demonstrates {@link CommandLineRunner}.
 *
 * <p>Spring Boot calls {@link #run(String...)} once after the application context is fully
 * initialized. This is useful for start-up jobs such as data seeding or printing a banner.
 *
 * <p>Multiple {@code CommandLineRunner} beans are executed in declaration order (or ordered via
 * {@code @Order}).
 */
@Component
public class CommandLineRunnerDemo implements CommandLineRunner {

  @Override
  public void run(String... args) {
    System.out.println("App started with args: " + Arrays.toString(args));
  }
}
