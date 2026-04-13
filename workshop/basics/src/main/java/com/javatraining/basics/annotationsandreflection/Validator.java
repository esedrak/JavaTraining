package com.javatraining.basics.annotationsandreflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Reflection-based validator that reads {@link Validate} annotations from an object's fields and
 * collects error messages.
 *
 * <p>For each field annotated with {@link Validate}:
 *
 * <ul>
 *   <li>If {@code required = true} and the field is {@code null}, an error is added.
 *   <li>If the field is a non-null {@link String}, its length is checked against {@code min} and
 *       {@code max}.
 * </ul>
 */
public class Validator {

  /**
   * Validates all {@link Validate}-annotated fields on the given object.
   *
   * @param obj the object to validate
   * @return list of error messages; empty if validation passes
   */
  public List<String> validate(Object obj) {
    List<String> errors = new ArrayList<>();
    Class<?> clazz = obj.getClass();

    for (Field field : clazz.getDeclaredFields()) {
      Validate annotation = field.getAnnotation(Validate.class);
      if (annotation == null) {
        continue;
      }

      field.setAccessible(true);
      Object value;
      try {
        value = field.get(obj);
      } catch (IllegalAccessException e) {
        errors.add("Cannot access field: " + field.getName());
        continue;
      }

      String fieldName = field.getName();

      // Required check
      if (annotation.required() && value == null) {
        errors.add(fieldName + ": is required but was null");
        continue;
      }

      // String length checks
      if (value instanceof String str) {
        if (annotation.required() && str.isBlank()) {
          errors.add(fieldName + ": is required but was blank");
          continue;
        }
        if (str.length() < annotation.min()) {
          errors.add(
              fieldName
                  + ": length "
                  + str.length()
                  + " is less than minimum "
                  + annotation.min());
        }
        if (str.length() > annotation.max()) {
          errors.add(
              fieldName
                  + ": length "
                  + str.length()
                  + " exceeds maximum "
                  + annotation.max());
        }
      }
    }

    return errors;
  }
}
