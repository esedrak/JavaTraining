package com.javatraining.basics.buildprofiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Example of a Spring bean that is only loaded when the {@code dev} profile is active.
 *
 * <p>Spring reads the {@code spring.profiles.active} property (or the {@code
 * SPRING_PROFILES_ACTIVE} environment variable) and only registers {@code @Profile}-annotated beans
 * whose profiles match.
 *
 * <pre>
 * # application.properties
 * spring.profiles.active=dev
 * </pre>
 *
 * <p>In tests, use {@code @ActiveProfiles("dev")} on the test class to activate this bean.
 */
@Component
@Profile("dev")
public class SpringProfileDemo {

  /**
   * Returns the name of the active environment.
   *
   * @return "development"
   */
  public String getEnvironment() {
    return "development";
  }
}
