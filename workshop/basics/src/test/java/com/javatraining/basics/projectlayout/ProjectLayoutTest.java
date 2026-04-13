package com.javatraining.basics.projectlayout;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProjectLayoutTest {

  private ProjectLayout layout;

  @BeforeEach
  void setUp() {
    layout = new ProjectLayout();
  }

  @Test
  void describeStructure_containsMainSourceRoot() {
    assertThat(layout.describeStructure()).contains("src/main/java");
  }

  @Test
  void describeStructure_containsTestSourceRoot() {
    assertThat(layout.describeStructure()).contains("src/test/java");
  }

  @Test
  void describeStructure_containsBuildScript() {
    assertThat(layout.describeStructure()).contains("build.gradle.kts");
  }

  @Test
  void constant_mainSourceRoot_hasExpectedValue() {
    assertThat(ProjectLayout.MAIN_SOURCE_ROOT).isEqualTo("src/main/java");
  }

  @Test
  void constant_testSourceRoot_hasExpectedValue() {
    assertThat(ProjectLayout.TEST_SOURCE_ROOT).isEqualTo("src/test/java");
  }

  @Test
  void constant_mainResourcesRoot_hasExpectedValue() {
    assertThat(ProjectLayout.MAIN_RESOURCES_ROOT).isEqualTo("src/main/resources");
  }

  @Test
  void constant_buildOutputDir_hasExpectedValue() {
    assertThat(ProjectLayout.BUILD_OUTPUT_DIR).isEqualTo("build/classes");
  }

  @Test
  void publicMethod_returnsPublic() {
    assertThat(layout.publicMethod()).isEqualTo("public");
  }

  @Test
  void protectedMethod_returnsProtected() {
    assertThat(layout.protectedMethod()).isEqualTo("protected");
  }

  @Test
  void packagePrivateMethod_returnsPackagePrivate() {
    assertThat(layout.packagePrivateMethod()).isEqualTo("package-private");
  }

  @Test
  void publicNested_greet_returnsExpectedString() {
    ProjectLayout.PublicNested nested = new ProjectLayout.PublicNested();
    assertThat(nested.greet()).contains("PublicNested");
  }
}
