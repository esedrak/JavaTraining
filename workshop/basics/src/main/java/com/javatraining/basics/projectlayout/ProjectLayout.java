package com.javatraining.basics.projectlayout;

/**
 * Topic 20: Java/Gradle Project Layout
 *
 * <p>Documents the conventional directory structure for a Gradle Java project and demonstrates Java
 * access modifiers.
 */
public class ProjectLayout {

  // -------------------------------------------------------------------------
  // Conventional path constants
  // -------------------------------------------------------------------------

  public static final String MAIN_SOURCE_ROOT = "src/main/java";
  public static final String TEST_SOURCE_ROOT = "src/test/java";
  public static final String MAIN_RESOURCES_ROOT = "src/main/resources";
  public static final String TEST_RESOURCES_ROOT = "src/test/resources";
  public static final String BUILD_OUTPUT_DIR = "build/classes";
  public static final String BUILD_LIBS_DIR = "build/libs";

  // -------------------------------------------------------------------------
  // Project structure description
  // -------------------------------------------------------------------------

  /**
   * Returns a text-block description of the standard Gradle project layout.
   *
   * @return multiline string documenting the directory tree
   */
  public String describeStructure() {
    return """
        Gradle Java Project Layout
        ==========================
        project-root/
        ├── build.gradle.kts          ← Kotlin DSL build script
        ├── settings.gradle.kts       ← project/module names
        ├── gradle/
        │   └── libs.versions.toml    ← version catalog
        ├── src/main/java             ← production source code
        ├── src/main/resources        ← application.yml, logback.xml, etc.
        ├── src/test/java             ← test source code
        └── src/test/resources        ← test fixtures
        """;
  }

  // -------------------------------------------------------------------------
  // Access modifier demonstration
  // -------------------------------------------------------------------------

  /** Public: accessible from anywhere. */
  public String publicMethod() {
    return "public";
  }

  /** Protected: accessible within the same package AND subclasses. */
  protected String protectedMethod() {
    return "protected";
  }

  /** Package-private (no modifier): accessible only within the same package. */
  String packagePrivateMethod() {
    return "package-private";
  }

  /** Private: accessible only within this class. */
  @SuppressWarnings("unused")
  private String privateMethod() {
    return "private";
  }

  // -------------------------------------------------------------------------
  // Inner classes showing different access levels
  // -------------------------------------------------------------------------

  /** A public nested class accessible by anyone who has a reference to the enclosing class. */
  public static class PublicNested {
    public String greet() {
      return "Hello from PublicNested";
    }
  }

  /** A package-private nested class; only visible within {@code projectlayout}. */
  static class PackagePrivateNested {
    String greet() {
      return "Hello from PackagePrivateNested";
    }
  }

  /** Private nested class; only the enclosing class can instantiate or use it. */
  private static class PrivateNested {
    @SuppressWarnings("unused")
    private String greet() {
      return "Hello from PrivateNested";
    }
  }
}
