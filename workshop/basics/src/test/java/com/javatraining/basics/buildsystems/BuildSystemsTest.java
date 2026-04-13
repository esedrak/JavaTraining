package com.javatraining.basics.buildsystems;

import static org.assertj.core.api.Assertions.assertThat;

import com.javatraining.basics.buildsystems.BuildSystems.BuildSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BuildSystemsTest {

  private BuildSystems buildSystems;

  @BeforeEach
  void setUp() {
    buildSystems = new BuildSystems();
  }

  @Test
  void gradleKotlin_description_containsKotlin() {
    assertThat(BuildSystem.GRADLE_KOTLIN.description()).contains("Kotlin");
  }

  @Test
  void gradleGroovy_description_containsGroovy() {
    assertThat(BuildSystem.GRADLE_GROOVY.description()).contains("Groovy");
  }

  @Test
  void maven_description_containsMaven() {
    assertThat(BuildSystem.MAVEN.description()).contains("Maven");
  }

  @Test
  void compare_differentSystems_returnsNonNull() {
    String result = buildSystems.compare(BuildSystem.GRADLE_KOTLIN, BuildSystem.MAVEN);
    assertThat(result).isNotNull().isNotBlank();
  }

  @Test
  void compare_differentSystems_containsBothNames() {
    String result = buildSystems.compare(BuildSystem.GRADLE_KOTLIN, BuildSystem.MAVEN);
    assertThat(result).contains("GRADLE_KOTLIN").contains("MAVEN");
  }

  @Test
  void compare_sameSystems_returnsSelfComparison() {
    String result = buildSystems.compare(BuildSystem.MAVEN, BuildSystem.MAVEN);
    assertThat(result).contains("MAVEN");
  }

  @Test
  void currentBuildSystem_isGradleKotlin() {
    assertThat(BuildSystems.CURRENT_BUILD_SYSTEM).isEqualTo(BuildSystem.GRADLE_KOTLIN);
  }

  @Test
  void gradleKotlinSnippet_containsDependencies() {
    assertThat(BuildSystems.GRADLE_KOTLIN_SNIPPET).contains("dependencies");
  }

  @Test
  void mavenSnippet_containsPomXmlComment() {
    assertThat(BuildSystems.MAVEN_SNIPPET).contains("pom.xml");
  }
}
