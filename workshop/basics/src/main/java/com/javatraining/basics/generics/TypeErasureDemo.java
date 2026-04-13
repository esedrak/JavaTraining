package com.javatraining.basics.generics;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates type erasure in Java generics.
 *
 * <p>At compile time, generics provide type safety. At runtime, generic type information is erased
 * and replaced with the upper bound (usually Object).
 *
 * <p>This means: - ArrayList<String> and ArrayList<Integer> are the same class at runtime - You
 * cannot use instanceof with parameterized types (use List<?>) - You CAN recover generic type info
 * from fields via reflection (not variables)
 */
public class TypeErasureDemo {

  // Field-level generic type info IS retained in bytecode and accessible via reflection
  private List<String> stringList;

  public static void showErasure() {
    // 1. Same class at runtime
    ArrayList<String> strings = new ArrayList<>();
    ArrayList<Integer> integers = new ArrayList<>();
    boolean sameClass = strings.getClass() == integers.getClass();
    System.out.println("ArrayList<String>.class == ArrayList<Integer>.class: " + sameClass);
    // Prints: true

    // 2. Cannot do: strings instanceof List<String>  (compile error)
    //    Must use raw type or unbounded wildcard:
    boolean isList = strings instanceof List<?>;
    System.out.println("strings instanceof List<?>: " + isList);
    // Prints: true

    // 3. Recover generic type from a field using reflection
    try {
      Field field = TypeErasureDemo.class.getDeclaredField("stringList");
      Type genericType = field.getGenericType();
      if (genericType instanceof ParameterizedType pt) {
        Type typeArg = pt.getActualTypeArguments()[0];
        System.out.println(
            "Recovered generic type argument of stringList: " + typeArg.getTypeName());
        // Prints: java.lang.String
      }
    } catch (NoSuchFieldException e) {
      System.out.println("Field not found: " + e.getMessage());
    }
  }

  public static boolean arrayListsHaveSameClass() {
    return new ArrayList<String>().getClass() == new ArrayList<Integer>().getClass();
  }

  public static String recoverFieldGenericType() throws NoSuchFieldException {
    Field field = TypeErasureDemo.class.getDeclaredField("stringList");
    Type genericType = field.getGenericType();
    if (genericType instanceof ParameterizedType pt) {
      return pt.getActualTypeArguments()[0].getTypeName();
    }
    return "unknown";
  }
}
