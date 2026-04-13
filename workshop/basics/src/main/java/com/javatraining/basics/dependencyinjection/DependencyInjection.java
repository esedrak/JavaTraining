package com.javatraining.basics.dependencyinjection;

/**
 * Topic 14: Dependency Injection (DI) and Spring IoC
 *
 * <p><strong>What is DI?</strong> Instead of a class creating its own dependencies with {@code new
 * ...}, they are "injected" from outside. This decouples components and makes them independently
 * testable.
 *
 * <p><strong>Spring IoC Container</strong> manages object creation (beans) and wires dependencies
 * automatically based on {@code @Component}, {@code @Service}, {@code @Repository}, etc.
 *
 * <p><strong>Injection styles:</strong>
 *
 * <ul>
 *   <li><em>Constructor injection</em> (preferred) — dependencies declared in constructor; enables
 *       immutability and easy testing.
 *   <li><em>Setter injection</em> — flexible; allows optional dependencies.
 *   <li><em>Field injection</em> ({@code @Autowired} on a field) — concise but harder to test.
 * </ul>
 *
 * <p>See {@link UserRegistrationService} for a constructor-injection example and {@link
 * NotificationService} for the interface-based abstraction pattern.
 */
public class DependencyInjection {
  private DependencyInjection() {}
}
