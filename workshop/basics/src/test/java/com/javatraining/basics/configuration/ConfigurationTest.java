package com.javatraining.basics.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Pure unit tests — no Spring context needed. We exercise the POJO setters/getters directly. */
class ConfigurationTest {

  @Test
  void appConfig_gettersReturnSetValues() {
    AppConfig config = new AppConfig();
    config.setName("TestApp");
    config.setMaxConnections(10);
    config.setTimeout(Duration.ofSeconds(30));
    config.setAllowedHosts(List.of("localhost", "example.com"));

    assertThat(config.getName()).isEqualTo("TestApp");
    assertThat(config.getMaxConnections()).isEqualTo(10);
    assertThat(config.getTimeout()).isEqualTo(Duration.ofSeconds(30));
    assertThat(config.getAllowedHosts()).containsExactly("localhost", "example.com");
  }

  @Test
  void appConfig_toString_containsName() {
    AppConfig config = new AppConfig();
    config.setName("MyApp");
    assertThat(config.toString()).contains("MyApp");
  }

  @Test
  void featureFlags_darkMode_toggle() {
    FeatureFlags flags = new FeatureFlags();
    flags.setDarkMode(true);
    assertThat(flags.isDarkMode()).isTrue();

    flags.setDarkMode(false);
    assertThat(flags.isDarkMode()).isFalse();
  }

  @Test
  void featureFlags_betaFeatures_toggle() {
    FeatureFlags flags = new FeatureFlags();
    flags.setBetaFeatures(true);
    assertThat(flags.isBetaFeatures()).isTrue();
  }

  @Test
  void configurationDemo_getServerInfo_containsNameAndPort() {
    ConfigurationDemo demo = new ConfigurationDemo();
    demo.setAppName("TestApp");
    demo.setServerPort(9090);

    String info = demo.getServerInfo();
    assertThat(info).contains("TestApp").contains("9090");
  }

  @Test
  void configurationDemo_defaultValues() {
    // @Value defaults cannot be tested without Spring; we verify the setters/getters work
    ConfigurationDemo demo = new ConfigurationDemo();
    demo.setAppName("JavaTraining");
    demo.setServerPort(8080);

    assertThat(demo.getAppName()).isEqualTo("JavaTraining");
    assertThat(demo.getServerPort()).isEqualTo(8080);
    assertThat(demo.getServerInfo()).isEqualTo("Server: JavaTraining on port 8080");
  }
}
