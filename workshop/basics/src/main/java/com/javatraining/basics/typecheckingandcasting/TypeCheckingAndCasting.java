package com.javatraining.basics.typecheckingandcasting;

import java.util.List;

/**
 * Topic 8: Type Checking and Casting
 *
 * Demonstrates:
 * - Classic instanceof + explicit cast
 * - Pattern matching instanceof (Java 16+): if (obj instanceof String s)
 * - ClassCastException when casting incompatible types
 * - Describing objects using pattern matching
 */
public class TypeCheckingAndCasting {

    /**
     * Classic (pre-Java 16) approach: instanceof check then explicit cast.
     */
    public static String classicInstanceof(Object obj) {
        if (obj instanceof String) {
            String s = (String) obj;  // explicit cast required
            return "Classic: String of length " + s.length();
        }
        return "Classic: not a String";
    }

    /**
     * Modern approach (Java 16+): pattern matching for instanceof.
     * The variable 's' is in scope only within the if-block.
     */
    public static String patternMatchingInstanceof(Object obj) {
        if (obj instanceof String s) {
            // 's' is already bound and typed here — no explicit cast needed
            return "Pattern: String of length " + s.length();
        }
        return "Pattern: not a String";
    }

    /**
     * Describes an object using if/else-if chains with pattern matching instanceof.
     */
    public static String describe(Object obj) {
        if (obj == null) {
            return "null";
        } else if (obj instanceof String s) {
            return "String of length " + s.length();
        } else if (obj instanceof Integer i) {
            return "Integer: " + i;
        } else if (obj instanceof Double d) {
            return "Double: " + d;
        } else if (obj instanceof Long l) {
            return "Long: " + l;
        } else if (obj instanceof List<?> list) {
            return "List of size " + list.size();
        } else {
            return "Unknown type: " + obj.getClass().getSimpleName();
        }
    }

    /**
     * Demonstrates a ClassCastException when an illegal cast is attempted.
     * In practice you'd always check with instanceof first — this shows
     * what happens if you don't.
     */
    public static Integer castStringToInteger(Object obj) {
        // This will throw ClassCastException at runtime if obj is not an Integer
        return (Integer) obj;
    }

    /**
     * Safe cast using pattern matching — returns null instead of throwing.
     */
    public static Integer safeCastToInteger(Object obj) {
        if (obj instanceof Integer i) {
            return i;
        }
        return null;
    }
}
