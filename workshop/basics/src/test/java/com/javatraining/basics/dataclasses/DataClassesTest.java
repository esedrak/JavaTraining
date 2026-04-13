package com.javatraining.basics.dataclasses;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DataClassesTest {

    @Test
    void record_equality_sameValues_areEqual() {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        assertThat(p1).isEqualTo(p2);
    }

    @Test
    void record_equality_differentValues_areNotEqual() {
        assertThat(new Point(1, 2)).isNotEqualTo(new Point(3, 4));
    }

    @Test
    void record_compactConstructor_throwsOnNegativeCoordinates() {
        assertThatThrownBy(() -> new Point(-1, 0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Point(0, -5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void record_compactConstructor_allowsZero() {
        Point origin = new Point(0, 0);
        assertThat(origin.x()).isEqualTo(0);
        assertThat(origin.y()).isEqualTo(0);
    }

    @Test
    void person_greetingMethod() {
        Person person = new Person("Alice", 30);
        assertThat(person.greeting()).isEqualTo("Hi, I'm Alice");
    }

    @Test
    void person_accessors() {
        Person person = new Person("Bob", 25);
        assertThat(person.name()).isEqualTo("Bob");
        assertThat(person.age()).isEqualTo(25);
    }

    @Test
    void money_of_equalsWorks() {
        Money m1 = Money.of(new BigDecimal("10.00"), "USD");
        Money m2 = Money.of(new BigDecimal("10.00"), "USD");
        assertThat(m1).isEqualTo(m2);
    }

    @Test
    void money_of_differentAmounts_notEqual() {
        Money m1 = Money.of(new BigDecimal("10.00"), "USD");
        Money m2 = Money.of(new BigDecimal("20.00"), "USD");
        assertThat(m1).isNotEqualTo(m2);
    }

    @Test
    void money_of_differentCurrencies_notEqual() {
        Money m1 = Money.of(new BigDecimal("10.00"), "USD");
        Money m2 = Money.of(new BigDecimal("10.00"), "EUR");
        assertThat(m1).isNotEqualTo(m2);
    }

    @Test
    void builder_buildsCorrectObject() {
        PersonBuilder person = new PersonBuilder.Builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@example.com")
                .age(28)
                .build();

        assertThat(person.getFirstName()).isEqualTo("Jane");
        assertThat(person.getLastName()).isEqualTo("Doe");
        assertThat(person.getEmail()).isEqualTo("jane@example.com");
        assertThat(person.getAge()).isEqualTo(28);
    }

    @Test
    void builder_fullName_concatenatesFirstAndLast() {
        PersonBuilder person = new PersonBuilder.Builder()
                .firstName("John")
                .lastName("Smith")
                .build();
        assertThat(person.getFullName()).isEqualTo("John Smith");
    }

    @Test
    void record_hashCode_sameValuesProduceSameHashCode() {
        Point p1 = new Point(3, 4);
        Point p2 = new Point(3, 4);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }

    @Test
    void record_toString_containsComponents() {
        Point p = new Point(7, 8);
        assertThat(p.toString()).contains("7").contains("8");
    }
}
