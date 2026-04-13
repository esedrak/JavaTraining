package com.javatraining.basics.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Feature-flag configuration bound to the {@code features.*} prefix.
 *
 * <p>Example {@code application.yml}:
 *
 * <pre>
 * features:
 *   dark-mode: true
 *   beta-features: false
 * </pre>
 */
@Component
@ConfigurationProperties(prefix = "features")
public class FeatureFlags {

  private boolean darkMode;
  private boolean betaFeatures;

  public boolean isDarkMode() {
    return darkMode;
  }

  public void setDarkMode(boolean darkMode) {
    this.darkMode = darkMode;
  }

  public boolean isBetaFeatures() {
    return betaFeatures;
  }

  public void setBetaFeatures(boolean betaFeatures) {
    this.betaFeatures = betaFeatures;
  }
}
