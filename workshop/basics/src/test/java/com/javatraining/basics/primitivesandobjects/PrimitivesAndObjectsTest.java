package com.javatraining.basics.primitivesandobjects;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PrimitivesAndObjectsTest {

    @Test
    void autoboxing_intToInteger() {
        int primitive = 42;
        Integer boxed = primitive;  // autoboxing
        assertThat(boxed).isEqualTo(42);
        assertThat(boxed).isInstanceOf(Integer.class);
    }

    @Test
    void unboxing_IntegerToInt() {
        Integer boxed = Integer.valueOf(99);
        int primitive = boxed;  // unboxing
        assertThat(primitive).isEqualTo(99);
    }

    @Test
    void integerCache_withinRange_sameReference() {
        Integer a = 127;
        Integer b = 127;
        // Values in -128..127 are cached — same reference
        assertThat(a == b).isTrue();
    }

    @Test
    void integerCache_outsideRange_differentReference() {
        Integer a = 200;
        Integer b = 200;
        // Values outside -128..127 are NOT cached — different references
        assertThat(a == b).isFalse();
        // But .equals() still works correctly
        assertThat(a.equals(b)).isTrue();
    }

    @Test
    void integerCache_identityVsEquality() {
        assertThat(PrimitivesAndObjects.identityCheck(100, 100)).isTrue();
        assertThat(PrimitivesAndObjects.identityCheck(200, 200)).isFalse();
        assertThat(PrimitivesAndObjects.equalityCheck(200, 200)).isTrue();
    }

    @Test
    void optional_mapAndOrElse_returnsUpperCase() {
        String result = Optional.of("hello")
                .map(String::toUpperCase)
                .orElse("empty");
        assertThat(result).isEqualTo("HELLO");
    }

    @Test
    void optional_empty_returnsDefault() {
        String result = Optional.<String>empty().orElse("default");
        assertThat(result).isEqualTo("default");
    }

    @Test
    void optional_ofNullable_withNull_returnsDefault() {
        String result = PrimitivesAndObjects.optionalDemo(null);
        assertThat(result).isEqualTo("empty");
    }

    @Test
    void optional_ofNullable_withValue_mapsCorrectly() {
        String result = PrimitivesAndObjects.optionalDemo("world");
        assertThat(result).isEqualTo("WORLD");
    }

    @Test
    void stringConcatenation_createsNewObject() {
        String original = "hello";
        String concatenated = original + " world";
        // + creates a new String
        assertThat(concatenated).isNotSameAs(original);
        assertThat(concatenated).isEqualTo("hello world");
    }

    @Test
    void passByValue_primitive_callerUnaffected() {
        int value = 5;
        PrimitivesAndObjects.tryToModifyPrimitive(value);
        // The caller's variable is unchanged
        assertThat(value).isEqualTo(5);
    }

    @Test
    void passByValue_reference_reassignmentDoesNotAffectCaller() {
        StringBuilder sb = new StringBuilder("original");
        PrimitivesAndObjects.tryToModifyReference(sb);
        // Reassigning the local reference inside the method doesn't affect caller
        assertThat(sb.toString()).isEqualTo("original");
    }

    @Test
    void passByValue_mutatingObject_doesAffectCaller() {
        StringBuilder sb = new StringBuilder("original");
        PrimitivesAndObjects.mutateObject(sb);
        // Mutating the object itself is visible to the caller
        assertThat(sb.toString()).isEqualTo("original appended");
    }
}
