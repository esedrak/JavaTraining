package com.javatraining.basics.annotationsandreflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Demonstrates Java reflection: inspecting classes, methods, and fields at runtime. */
public class ReflectionDemo {

  /**
   * Returns a sorted list of the names of all public methods declared by {@code clazz} and its
   * superclasses (including inherited methods from {@link Object}).
   *
   * @param clazz the class to inspect
   * @return sorted list of public method names (may contain duplicates for overloaded methods)
   */
  public List<String> getPublicMethodNames(Class<?> clazz) {
    return Arrays.stream(clazz.getMethods())
        .map(Method::getName)
        .distinct()
        .sorted()
        .toList();
  }

  /**
   * Returns a map of field name to field value for all declared fields on {@code obj}'s class.
   *
   * <p>Uses {@link Field#setAccessible(boolean)} to read {@code private} fields. This is for
   * educational purposes — in production code prefer proper accessor methods.
   *
   * @param obj the object to inspect
   * @return map of field names to their current values
   */
  public Map<String, Object> getFieldValues(Object obj) {
    Map<String, Object> result = new HashMap<>();
    Class<?> clazz = obj.getClass();

    for (Field field : clazz.getDeclaredFields()) {
      // Skip synthetic fields (e.g. from inner classes)
      if (field.isSynthetic()) {
        continue;
      }
      field.setAccessible(true);
      try {
        result.put(field.getName(), field.get(obj));
      } catch (IllegalAccessException e) {
        result.put(field.getName(), "<inaccessible>");
      }
    }

    return result;
  }

  /**
   * Returns all methods on {@code clazz} that are annotated with {@link Audited}.
   *
   * @param clazz the class to scan
   * @return list of audited method names
   */
  public List<String> getAuditedMethodNames(Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredMethods())
        .filter(m -> m.isAnnotationPresent(Audited.class))
        .map(Method::getName)
        .sorted()
        .toList();
  }

  /**
   * Returns all static public fields on the given class.
   *
   * @param clazz class to inspect
   * @return list of static field names
   */
  public List<String> getStaticFieldNames(Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields())
        .filter(f -> Modifier.isStatic(f.getModifiers()))
        .map(Field::getName)
        .sorted()
        .toList();
  }
}
