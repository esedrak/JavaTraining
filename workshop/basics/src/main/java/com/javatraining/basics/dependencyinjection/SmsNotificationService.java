package com.javatraining.basics.dependencyinjection;

import org.springframework.stereotype.Service;

/**
 * SMS-based implementation of {@link NotificationService}.
 *
 * <p>The bean name {@code "sms"} allows Spring to distinguish this from the email implementation
 * when both are present in the application context.
 */
@Service("sms")
public class SmsNotificationService implements NotificationService {

  private int sentCount = 0;
  private String lastRecipient;
  private String lastMessage;

  @Override
  public void send(String recipient, String message) {
    this.lastRecipient = recipient;
    this.lastMessage = message;
    this.sentCount++;
    System.out.printf("[SMS] To: %s | Message: %s%n", recipient, message);
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
