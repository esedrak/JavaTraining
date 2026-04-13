package com.javatraining.basics.interfaces;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class InterfacesTest {

    @Test
    void circle_area_isPiRadiusSquared() {
        Circle circle = new Circle(5);
        assertThat(circle.area()).isCloseTo(Math.PI * 25, within(1e-9));
    }

    @Test
    void circle_perimeter_isTwoPiRadius() {
        Circle circle = new Circle(5);
        assertThat(circle.perimeter()).isCloseTo(2 * Math.PI * 5, within(1e-9));
    }

    @Test
    void rectangle_area_isWidthTimesHeight() {
        Rectangle rect = new Rectangle(3, 4);
        assertThat(rect.area()).isEqualTo(12.0);
    }

    @Test
    void rectangle_perimeter_isTwoTimesWidthPlusHeight() {
        Rectangle rect = new Rectangle(3, 4);
        assertThat(rect.perimeter()).isEqualTo(14.0);
    }

    @Test
    void triangle_area_heronsFormula_rightTriangle() {
        // 3-4-5 right triangle: area = 0.5 * 3 * 4 = 6.0
        Triangle triangle = new Triangle(3, 4, 5);
        assertThat(triangle.area()).isCloseTo(6.0, within(1e-9));
    }

    @Test
    void triangle_perimeter_sumOfSides() {
        Triangle triangle = new Triangle(3, 4, 5);
        assertThat(triangle.perimeter()).isEqualTo(12.0);
    }

    @Test
    void staticFactory_circle_createsCircleInstance() {
        Shape shape = Shape.circle(3);
        assertThat(shape).isInstanceOf(Circle.class);
        assertThat(shape.area()).isCloseTo(Math.PI * 9, within(1e-9));
    }

    @Test
    void defaultMethod_describe_containsAreaKeyword() {
        Shape shape = new Circle(1);
        assertThat(shape.describe()).contains("area");
    }

    @Test
    void defaultMethod_describe_containsFormattedArea() {
        Shape shape = new Rectangle(2, 3);  // area = 6.0
        assertThat(shape.describe()).contains("6.00");
    }

    @Test
    void sealedInterface_patternMatching_switchCoversAllPermits() {
        Shape circle = new Circle(2);
        Shape rectangle = new Rectangle(3, 4);
        Shape triangle = new Triangle(3, 4, 5);

        assertThat(describeShape(circle)).isEqualTo("circle");
        assertThat(describeShape(rectangle)).isEqualTo("rectangle");
        assertThat(describeShape(triangle)).isEqualTo("triangle");
    }

    /**
     * Pattern matching switch on a sealed interface — the compiler enforces
     * that all permitted subtypes are handled, so no default is needed.
     */
    private String describeShape(Shape shape) {
        return switch (shape) {
            case Circle c    -> "circle";
            case Rectangle r -> "rectangle";
            case Triangle t  -> "triangle";
        };
    }

    @Test
    void sealedInterface_patternMatching_extractsComponents() {
        Shape circle = Shape.circle(7);
        double radius = switch (circle) {
            case Circle c    -> c.radius();
            case Rectangle r -> 0;
            case Triangle t  -> 0;
        };
        assertThat(radius).isEqualTo(7.0);
    }
}
