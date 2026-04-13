package com.javatraining.basics.configuration;

import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Topic 15: Configuration — {@code @ConfigurationProperties}
 *
 * <p>Spring Boot binds properties with the {@code app.*} prefix from {@code
 * application.properties} / {@code application.yml} to this class automatically. No {@code
 * @Value} needed for structured config.
 *
 * <p>Example {@code application.yml}:
 *
 * <pre>
 * app:
 *   name: JavaTraining
 *   max-connections: 20
 *   timeout: 30s
 *   allowed-hosts:
 *     - localhost
 *     - example.com
 * </pre>
 */
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {

  private String name;
  private int maxConnections;
  private Duration timeout;
  private List<String> allowedHosts;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getMaxConnections() {
    return maxConnections;
  }

  public void setMaxConnections(int maxConnections) {
    this.maxConnections = maxConnections;
  }

  public Duration getTimeout() {
    return timeout;
  }

  public void setTimeout(Duration timeout) {
    this.timeout = timeout;
  }

  public List<String> getAllowedHosts() {
    return allowedHosts;
  }

  public void setAllowedHosts(List<String> allowedHosts) {
    this.allowedHosts = allowedHosts;
  }

  @Override
  public String toString() {
    return "AppConfig{"
        + "name='"
        + name
        + '\''
        + ", maxConnections="
        + maxConnections
        + ", timeout="
        + timeout
        + ", allowedHosts="
        + allowedHosts
        + '}';
  }
}
