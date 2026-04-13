package com.javatraining.basics.defaultandstaticinterfacemethods;

/**
 * Topic 5: Default and Static Interface Methods
 *
 * Java 8+ allows interfaces to provide:
 * - default methods: concrete implementations that can be overridden by implementors
 * - static methods: utility/factory methods that belong to the interface itself
 */
public interface Formatter {

    /**
     * Abstract method — must be implemented by every implementor.
     */
    String format(String input);

    /**
     * Default method — calls the abstract format() then converts to upper case.
     * Implementors inherit this for free; they can also override it.
     */
    default String formatUpperCase(String input) {
        return format(input).toUpperCase();
    }

    /**
     * Default method — wraps the formatted result in square brackets.
     */
    default String formatWithBrackets(String input) {
        return "[" + format(input) + "]";
    }

    /**
     * Static factory method — returns a Formatter implementation that trims whitespace.
     * Static interface methods are NOT inherited by implementing classes.
     */
    static Formatter trimming() {
        return input -> input == null ? "" : input.trim();
    }
}
