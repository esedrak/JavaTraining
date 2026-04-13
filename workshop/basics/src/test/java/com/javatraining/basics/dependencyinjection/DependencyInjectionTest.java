package com.javatraining.basics.dependencyinjection;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pure unit test — no Spring context. We wire dependencies manually, exactly as Spring would do at
 * runtime, which proves constructor injection makes classes trivially testable.
 */
class DependencyInjectionTest {

  private EmailNotificationService emailService;
  private UserRegistrationService registrationService;

  @BeforeEach
  void setUp() {
    emailService = new EmailNotificationService();
    registrationService = new UserRegistrationService(emailService);
  }

  @Test
  void registerUser_incrementsSentCount() {
    registrationService.registerUser("Alice", "alice@example.com");
    assertThat(emailService.getSentCount()).isEqualTo(1);
  }

  @Test
  void registerUser_lastMessageContainsUsername() {
    registrationService.registerUser("Alice", "alice@example.com");
    assertThat(emailService.getLastMessage()).contains("Alice");
  }

  @Test
  void registerUser_lastRecipientIsEmail() {
    registrationService.registerUser("Bob", "bob@example.com");
    assertThat(emailService.getLastRecipient()).isEqualTo("bob@example.com");
  }

  @Test
  void registerUser_multipleCalls_incrementsEachTime() {
    registrationService.registerUser("Alice", "alice@example.com");
    registrationService.registerUser("Bob", "bob@example.com");
    assertThat(emailService.getSentCount()).isEqualTo(2);
  }

  @Test
  void smsService_sendWorks() {
    SmsNotificationService smsService = new SmsNotificationService();
    UserRegistrationService smsRegistration = new UserRegistrationService(smsService);
    smsRegistration.registerUser("Carol", "555-1234");
    assertThat(smsService.getSentCount()).isEqualTo(1);
    assertThat(smsService.getLastMessage()).contains("Carol");
  }
}
