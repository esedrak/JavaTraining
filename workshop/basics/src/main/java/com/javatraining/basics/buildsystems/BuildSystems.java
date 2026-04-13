package com.javatraining.basics.buildsystems;

/**
 * Topic 21: Build Systems
 *
 * <p>Compares the three most common JVM build tools: Gradle with Kotlin DSL, Gradle with Groovy
 * DSL, and Apache Maven. Each has different syntax but similar capabilities for dependency
 * management, compilation, testing, and artifact publishing.
 */
public class BuildSystems {

  // -------------------------------------------------------------------------
  // Enum with per-value description
  // -------------------------------------------------------------------------

  /** The three major JVM build systems in common use. */
  public enum BuildSystem {
    GRADLE_KOTLIN {
      @Override
      public String description() {
        return "Gradle (Kotlin DSL) — type-safe build scripts in Kotlin, "
            + "IDE auto-complete, incremental builds, rich plugin ecosystem.";
      }
    },
    GRADLE_GROOVY {
      @Override
      public String description() {
        return "Gradle (Groovy DSL) — dynamic scripting with Groovy, "
            + "concise syntax, large community base.";
      }
    },
    MAVEN {
      @Override
      public String description() {
        return "Maven — convention-over-configuration with XML POMs, "
            + "mature lifecycle, ubiquitous in enterprise Java.";
      }
    };

    /** Returns a human-readable description of this build system. */
    public abstract String description();
  }

  /** The build system used by this project. */
  public static final BuildSystem CURRENT_BUILD_SYSTEM = BuildSystem.GRADLE_KOTLIN;

  // -------------------------------------------------------------------------
  // Snippet constants (text blocks)
  // -------------------------------------------------------------------------

  /** Example Gradle Kotlin DSL dependency declaration. */
  public static final String GRADLE_KOTLIN_SNIPPET =
      """
      // build.gradle.kts
      dependencies {
          implementation("org.springframework.boot:spring-boot-starter-web")
          testImplementation("org.junit.jupiter:junit-jupiter")
      }
      """;

  /** Example Gradle Groovy DSL dependency declaration. */
  public static final String GRADLE_GROOVY_SNIPPET =
      """
      // build.gradle
      dependencies {
          implementation 'org.springframework.boot:spring-boot-starter-web'
          testImplementation 'org.junit.jupiter:junit-jupiter'
      }
      """;

  /** Example Maven dependency declaration. */
  public static final String MAVEN_SNIPPET =
      """
      <!-- pom.xml -->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-web</artifactId>
      </dependency>
      """;

  // -------------------------------------------------------------------------
  // Comparison method
  // -------------------------------------------------------------------------

  /**
   * Returns a string comparing two build systems.
   *
   * @param a the first build system
   * @param b the second build system
   * @return a non-null comparison string
   */
  public String compare(BuildSystem a, BuildSystem b) {
    if (a == b) {
      return a.name() + " compared with itself.";
    }
    return a.name() + " vs " + b.name() + ": " + a.description() + " | " + b.description();
  }
}
