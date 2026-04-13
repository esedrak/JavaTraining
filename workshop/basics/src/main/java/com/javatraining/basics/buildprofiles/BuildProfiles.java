package com.javatraining.basics.buildprofiles;

/**
 * Demonstrates runtime environment detection in Java.
 *
 * <p>Unlike C/C++ which has preprocessor {@code #if} directives, Java has no compile-time
 * conditional compilation. Instead, branching is done at runtime using system properties,
 * environment variables, or Spring {@code @Profile} annotations.
 *
 * <h2>Spring Profiles</h2>
 *
 * <p>Spring Boot's {@code @Profile} annotation ({@link org.springframework.context.annotation.Profile})
 * controls which beans are loaded. For example:
 *
 * <pre>
 * {@literal @}Component
 * {@literal @}Profile("dev")
 * public class DevDataSourceConfig { ... }
 * </pre>
 *
 * <p>Activated via {@code spring.profiles.active=dev} in application properties, or
 * {@code --spring.profiles.active=dev} on the command line.
 */
public class BuildProfiles {

  /** Enumeration of supported operating systems. */
  public enum OperatingSystem {
    WINDOWS,
    MACOS,
    LINUX,
    UNKNOWN
  }

  /**
   * Detects the current operating system from the {@code os.name} system property.
   *
   * @return the detected {@link OperatingSystem}, never {@code null}
   */
  public OperatingSystem current() {
    String osName = System.getProperty("os.name", "").toLowerCase();
    if (osName.contains("win")) {
      return OperatingSystem.WINDOWS;
    } else if (osName.contains("mac") || osName.contains("darwin")) {
      return OperatingSystem.MACOS;
    } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
      return OperatingSystem.LINUX;
    }
    return OperatingSystem.UNKNOWN;
  }

  /**
   * Returns the value of the given environment variable, or {@code defaultValue} if it is not set.
   *
   * @param key          environment variable name
   * @param defaultValue fallback value
   * @return non-null string
   */
  public String getEnv(String key, String defaultValue) {
    String value = System.getenv(key);
    return value != null ? value : defaultValue;
  }

  /**
   * Returns the value of the given system property, or {@code defaultValue} if it is not set.
   *
   * @param key          property name (e.g. "java.version")
   * @param defaultValue fallback value
   * @return non-null string
   */
  public String getProperty(String key, String defaultValue) {
    return System.getProperty(key, defaultValue);
  }

  /**
   * Returns {@code true} if the system property {@code feature.<featureName>} is set to
   * {@code "true"} (case-insensitive).
   *
   * @param featureName the feature flag name
   * @return {@code true} if enabled
   */
  public boolean isFeatureEnabled(String featureName) {
    String value = System.getProperty("feature." + featureName, "false");
    return Boolean.parseBoolean(value);
  }
}
