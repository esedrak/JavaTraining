package com.javatraining.basics.annotationsandreflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Field-level annotation used by {@link Validator} to enforce constraints on {@code String} fields.
 *
 * <p>Example usage:
 *
 * <pre>
 * {@literal @}Validate(min = 2, max = 50, required = true)
 * private String username;
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Validate {

  /** Minimum length of the string field (default 0). */
  int min() default 0;

  /** Maximum length of the string field (default {@link Integer#MAX_VALUE}). */
  int max() default Integer.MAX_VALUE;

  /** If {@code true}, a {@code null} or blank value is an error (default {@code true}). */
  boolean required() default true;

  /** Error message template used when validation fails. */
  String message() default "Validation failed";
}
