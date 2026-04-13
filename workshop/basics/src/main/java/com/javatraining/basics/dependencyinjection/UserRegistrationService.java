package com.javatraining.basics.dependencyinjection;

import org.springframework.stereotype.Service;

/**
 * Handles new user registration.
 *
 * <p>Demonstrates constructor injection — the preferred Spring idiom. Because there is exactly one
 * constructor, Spring automatically injects the matching bean without needing {@code @Autowired}.
 *
 * <p>The dependency on {@link NotificationService} is declared in the constructor, making the
 * service easy to unit-test: just pass any implementation directly (no Spring context required).
 */
@Service
public class UserRegistrationService {

  private final NotificationService notificationService;

  /**
   * Spring auto-detects a single constructor and injects dependencies automatically. No
   * {@code @Autowired} annotation is needed.
   */
  public UserRegistrationService(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  /**
   * Registers a new user and sends a welcome notification.
   *
   * @param username the chosen username
   * @param email the user's email address (used as the notification recipient)
   */
  public void registerUser(String username, String email) {
    // Business logic would persist the user here.
    notificationService.send(email, "Welcome, " + username);
  }
}
