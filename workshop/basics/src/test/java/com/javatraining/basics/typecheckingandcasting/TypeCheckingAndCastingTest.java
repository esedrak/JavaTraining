package com.javatraining.basics.typecheckingandcasting;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TypeCheckingAndCastingTest {

    @Test
    void describe_string_reportsLengthFive() {
        assertThat(TypeCheckingAndCasting.describe("hello"))
                .contains("String")
                .contains("5");
    }

    @Test
    void describe_integer_mentionsIntegerAndValue() {
        assertThat(TypeCheckingAndCasting.describe(42))
                .contains("Integer")
                .contains("42");
    }

    @Test
    void describe_double_mentionsDouble() {
        assertThat(TypeCheckingAndCasting.describe(3.14))
                .contains("Double");
    }

    @Test
    void describe_null_returnsNullString() {
        assertThat(TypeCheckingAndCasting.describe(null)).isEqualTo("null");
    }

    @Test
    void describe_list_mentsionsSizeThree() {
        assertThat(TypeCheckingAndCasting.describe(List.of(1, 2, 3)))
                .contains("List")
                .contains("3");
    }

    @Test
    void castStringToInteger_throwsClassCastException() {
        assertThatThrownBy(() -> TypeCheckingAndCasting.castStringToInteger("not an integer"))
                .isInstanceOf(ClassCastException.class);
    }

    @Test
    void safeCast_withInteger_returnsInteger() {
        Integer result = TypeCheckingAndCasting.safeCastToInteger(99);
        assertThat(result).isEqualTo(99);
    }

    @Test
    void safeCast_withString_returnsNull() {
        Integer result = TypeCheckingAndCasting.safeCastToInteger("hello");
        assertThat(result).isNull();
    }

    @Test
    void classicInstanceof_withString_reportsLength() {
        assertThat(TypeCheckingAndCasting.classicInstanceof("test"))
                .contains("String").contains("4");
    }

    @Test
    void patternMatchingInstanceof_withString_reportsLength() {
        assertThat(TypeCheckingAndCasting.patternMatchingInstanceof("java"))
                .contains("String").contains("4");
    }

    @Test
    void patternMatchingInstanceof_withNonString_reportsMismatch() {
        assertThat(TypeCheckingAndCasting.patternMatchingInstanceof(123))
                .contains("not a String");
    }
}
