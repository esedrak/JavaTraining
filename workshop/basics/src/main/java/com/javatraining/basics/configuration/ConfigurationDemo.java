package com.javatraining.basics.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Demonstrates {@code @Value} for injecting individual property values.
 *
 * <p>{@code @Value} supports SpEL (Spring Expression Language) and default values via the {@code :}
 * separator, so the app starts even when the property is not defined.
 *
 * <p>Prefer {@link AppConfig} ({@code @ConfigurationProperties}) for groups of related settings;
 * use {@code @Value} for a small number of standalone values.
 */
@Component
public class ConfigurationDemo {

  @Value("${server.port:8080}")
  private int serverPort;

  @Value("${app.name:JavaTraining}")
  private String appName;

  /**
   * Returns a human-readable description of the running server.
   *
   * @return e.g. {@code "Server: JavaTraining on port 8080"}
   */
  public String getServerInfo() {
    return "Server: " + appName + " on port " + serverPort;
  }

  // Setters allow manual injection in unit tests (no Spring context required).

  public void setServerPort(int serverPort) {
    this.serverPort = serverPort;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public int getServerPort() {
    return serverPort;
  }

  public String getAppName() {
    return appName;
  }
}
