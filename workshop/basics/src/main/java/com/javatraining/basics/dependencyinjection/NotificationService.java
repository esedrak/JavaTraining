package com.javatraining.basics.dependencyinjection;

/**
 * Abstraction for sending notifications.
 *
 * <p>Programming to an interface (not an implementation) is the foundation of Dependency Injection:
 * callers depend on this contract, and Spring wires in the concrete implementation at runtime.
 */
public interface NotificationService {

  /**
   * Sends a notification message to the specified recipient.
   *
   * @param recipient the destination address (email, phone, etc.)
   * @param message the content to send
   */
  void send(String recipient, String message);
}
