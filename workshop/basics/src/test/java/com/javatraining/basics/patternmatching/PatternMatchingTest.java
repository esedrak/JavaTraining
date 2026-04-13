package com.javatraining.basics.patternmatching;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class PatternMatchingTest {

    @Test
    void area_circle_isPiRadiusSquared() {
        double result = PatternMatching.area(new PatternMatching.Circle(5));
        assertThat(result).isCloseTo(Math.PI * 25, within(1e-9));
    }

    @Test
    void area_rectangle_isWidthTimesHeight() {
        double result = PatternMatching.area(new PatternMatching.Rectangle(3, 4));
        assertThat(result).isEqualTo(12.0);
    }

    @Test
    void area_triangle_isHalfBaseTimesHeight() {
        double result = PatternMatching.area(new PatternMatching.Triangle(6, 4));
        assertThat(result).isEqualTo(12.0);
    }

    @Test
    void classify_string_containsStringLabel() {
        assertThat(PatternMatching.classify("hello")).contains("String");
    }

    @Test
    void classify_integer_containsIntegerLabel() {
        assertThat(PatternMatching.classify(42)).contains("Integer");
    }

    @Test
    void classify_null_returnsNull() {
        assertThat(PatternMatching.classify(null)).isEqualTo("null");
    }

    @Test
    void classify_long_containsLongLabel() {
        assertThat(PatternMatching.classify(100L)).contains("Long");
    }

    @Test
    void classify_double_containsDoubleLabel() {
        assertThat(PatternMatching.classify(3.14)).contains("Double");
    }

    @Test
    void guardedPattern_largeCircle_classifiedDifferently() {
        String large = PatternMatching.classifyShape(new PatternMatching.Circle(15));
        String small = PatternMatching.classifyShape(new PatternMatching.Circle(5));

        assertThat(large).contains("large circle");
        assertThat(small).contains("small circle");
    }

    @Test
    void guardedPattern_rectangle_containsDimensions() {
        String result = PatternMatching.classifyShape(new PatternMatching.Rectangle(3, 4));
        assertThat(result).contains("rectangle").contains("3.0").contains("4.0");
    }

    @Test
    void recordPattern_circle_extractsRadius() {
        String result = PatternMatching.describeCircle(new PatternMatching.Circle(7));
        assertThat(result).contains("radius").contains("7.0");
    }

    @Test
    void recordPattern_degenerateCircle_identifiedSeparately() {
        String result = PatternMatching.describeCircle(new PatternMatching.Circle(0));
        assertThat(result).contains("degenerate");
    }

    @Test
    void recordPattern_notCircle_returnsNotACircle() {
        String result = PatternMatching.describeCircle("not a shape");
        assertThat(result).isEqualTo("not a circle");
    }
}
