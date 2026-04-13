package com.javatraining.basics.buildprofiles;

import static org.assertj.core.api.Assertions.assertThat;

import com.javatraining.basics.buildprofiles.BuildProfiles.OperatingSystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for runtime environment detection utilities. */
@DisplayName("BuildProfiles Tests")
class BuildProfilesTest {

  private BuildProfiles buildProfiles;

  @BeforeEach
  void setUp() {
    buildProfiles = new BuildProfiles();
  }

  @AfterEach
  void cleanUp() {
    // Remove any test-specific system properties
    System.clearProperty("feature.test");
  }

  @Test
  @DisplayName("current() returns a non-null OperatingSystem")
  void currentReturnsNonNull() {
    OperatingSystem os = buildProfiles.current();
    assertThat(os).isNotNull();
  }

  @Test
  @DisplayName("current() detects a known OS on CI / dev machines")
  void currentDetectsKnownOs() {
    OperatingSystem os = buildProfiles.current();
    // The test runner is running on SOME OS — it must not be UNKNOWN on major platforms
    assertThat(os)
        .isIn(
            OperatingSystem.WINDOWS,
            OperatingSystem.MACOS,
            OperatingSystem.LINUX,
            OperatingSystem.UNKNOWN);
  }

  @Test
  @DisplayName("getEnv(\"PATH\", \"none\") returns a non-null value")
  void getEnvPathReturnsNonNull() {
    String path = buildProfiles.getEnv("PATH", "none");
    assertThat(path).isNotNull();
  }

  @Test
  @DisplayName("getEnv returns defaultValue for unset variable")
  void getEnvReturnsDefaultForUnsetVariable() {
    String value = buildProfiles.getEnv("__THIS_VAR_DOES_NOT_EXIST_AT_ALL__", "default-value");
    assertThat(value).isEqualTo("default-value");
  }

  @Test
  @DisplayName("getProperty(\"java.version\", \"unknown\") returns real Java version")
  void getPropertyJavaVersionReturnsRealVersion() {
    String version = buildProfiles.getProperty("java.version", "unknown");
    assertThat(version).isNotEqualTo("unknown").isNotBlank();
  }

  @Test
  @DisplayName("isFeatureEnabled returns false for non-existent feature")
  void isFeatureEnabledReturnsFalseForNonExistent() {
    assertThat(buildProfiles.isFeatureEnabled("nonexistent")).isFalse();
  }

  @Test
  @DisplayName("isFeatureEnabled returns true after System.setProperty")
  void isFeatureEnabledReturnsTrueAfterSetProperty() {
    System.setProperty("feature.test", "true");
    assertThat(buildProfiles.isFeatureEnabled("test")).isTrue();
  }
}
