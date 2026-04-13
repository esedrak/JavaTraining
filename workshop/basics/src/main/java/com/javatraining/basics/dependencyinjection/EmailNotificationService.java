package com.javatraining.basics.dependencyinjection;

import org.springframework.stereotype.Service;

/**
 * Email-based implementation of {@link NotificationService}.
 *
 * <p>{@code @Service} registers this bean with the Spring IoC container. When multiple
 * implementations of the same interface exist, callers can use {@code @Qualifier("email")} to
 * select this one specifically.
 */
@Service("email")
public class EmailNotificationService implements NotificationService {

  private int sentCount = 0;
  private String lastRecipient;
  private String lastMessage;

  @Override
  public void send(String recipient, String message) {
    this.lastRecipient = recipient;
    this.lastMessage = message;
    this.sentCount++;
    System.out.printf("[EMAIL] To: %s | Message: %s%n", recipient, message);
  }

  public int getSentCount() {
    return sentCount;
  }

  public String getLastRecipient() {
    return lastRecipient;
  }

  public String getLastMessage() {
    return lastMessage;
  }
}
