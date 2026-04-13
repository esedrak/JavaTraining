package com.javatraining.basics.annotationsandreflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Tests for custom annotations, reflection-based validation, and the ReflectionDemo utilities. */
@DisplayName("Annotations and Reflection Tests")
class AnnotationsAndReflectionTest {

  private Validator validator;
  private ReflectionDemo reflectionDemo;

  @BeforeEach
  void setUp() {
    validator = new Validator();
    reflectionDemo = new ReflectionDemo();
  }

  // ── Annotation retention ─────────────────────────────────────────────────────

  @Nested
  @DisplayName("Annotation Retention")
  class AnnotationRetentionTests {

    @Test
    @DisplayName("@Validate is retained at runtime")
    void validateAnnotationIsRetainedAtRuntime() throws NoSuchFieldException {
      Field usernameField = UserForm.class.getDeclaredField("username");
      Validate annotation = usernameField.getAnnotation(Validate.class);

      assertThat(annotation).isNotNull();
      assertThat(annotation.min()).isEqualTo(2);
      assertThat(annotation.max()).isEqualTo(50);
      assertThat(annotation.required()).isTrue();
    }

    @Test
    @DisplayName("@Audited is retained at runtime")
    void auditedAnnotationIsRetainedAtRuntime() throws Exception {
      // Create a test class with an @Audited method via anonymous class isn't possible,
      // but we can verify the annotation's own retention meta-annotation.
      assertThat(Audited.class.isAnnotationPresent(java.lang.annotation.Retention.class)).isTrue();
      java.lang.annotation.Retention retention =
          Audited.class.getAnnotation(java.lang.annotation.Retention.class);
      assertThat(retention.value()).isEqualTo(java.lang.annotation.RetentionPolicy.RUNTIME);
    }
  }

  // ── Validator ────────────────────────────────────────────────────────────────

  @Nested
  @DisplayName("Validator Tests")
  class ValidatorTests {

    @Test
    @DisplayName("validate form with null username returns errors")
    void validateFormWithNullUsernameReturnsErrors() {
      UserForm form = new UserForm();
      List<String> errors = validator.validate(form);

      assertThat(errors).isNotEmpty();
      assertThat(errors).anyMatch(e -> e.contains("username"));
    }

    @Test
    @DisplayName("validate valid form returns no errors")
    void validateValidFormReturnsNoErrors() {
      UserForm form = new UserForm("alice", "password123");
      List<String> errors = validator.validate(form);

      assertThat(errors).isEmpty();
    }

    @Test
    @DisplayName("validate form with too-short username returns length error")
    void validateFormWithTooShortUsernameReturnsError() {
      UserForm form = new UserForm("a", "password123");
      List<String> errors = validator.validate(form);

      assertThat(errors).anyMatch(e -> e.contains("username") && e.contains("minimum"));
    }

    @Test
    @DisplayName("validate form with too-short password returns length error")
    void validateFormWithTooShortPasswordReturnsError() {
      UserForm form = new UserForm("alice", "short");
      List<String> errors = validator.validate(form);

      assertThat(errors).anyMatch(e -> e.contains("password"));
    }

    @Test
    @DisplayName("validate form with null optional nickname does not produce error")
    void validateFormWithNullNicknameProducesNoError() {
      UserForm form = new UserForm("alice", "password123", null);
      List<String> errors = validator.validate(form);

      assertThat(errors).isEmpty();
    }
  }

  // ── ReflectionDemo ───────────────────────────────────────────────────────────

  @Nested
  @DisplayName("ReflectionDemo Tests")
  class ReflectionDemoTests {

    @Test
    @DisplayName("getPublicMethodNames(String.class) contains 'length', 'substring', 'toUpperCase'")
    void publicMethodNamesContainsExpectedMethods() {
      List<String> methods = reflectionDemo.getPublicMethodNames(String.class);

      assertThat(methods).contains("length", "substring", "toUpperCase");
    }

    @Test
    @DisplayName("getFieldValues returns expected entries for UserForm")
    void getFieldValuesReturnsExpectedEntries() {
      UserForm form = new UserForm("alice", "password123");
      Map<String, Object> fields = reflectionDemo.getFieldValues(form);

      assertThat(fields)
          .containsEntry("username", "alice")
          .containsEntry("password", "password123");
    }

    @Test
    @DisplayName("getFieldValues includes null for unset nickname")
    void getFieldValuesIncludesNullNickname() {
      UserForm form = new UserForm("alice", "password123");
      Map<String, Object> fields = reflectionDemo.getFieldValues(form);

      assertThat(fields).containsKey("nickname");
      assertThat(fields.get("nickname")).isNull();
    }
  }
}
