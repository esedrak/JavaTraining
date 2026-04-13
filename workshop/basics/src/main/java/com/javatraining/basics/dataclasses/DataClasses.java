package com.javatraining.basics.dataclasses;

/**
 * Topic 3: Data Classes
 *
 * Java 16+ introduces records as a concise way to model immutable data.
 * A record automatically provides:
 *   - A canonical constructor
 *   - Accessor methods (component name, no "get" prefix)
 *   - equals(), hashCode(), toString() based on all components
 *
 * Records are implicitly final and cannot extend other classes.
 *
 * For mutable data holders or when you need more control,
 * a regular POJO with a Builder is the idiomatic approach.
 *
 * See also: Point.java, Person.java, Money.java, PersonBuilder.java
 */
public class DataClasses {
    // This class serves as documentation for the topic.
    // See the other classes in this package for concrete examples.
}
