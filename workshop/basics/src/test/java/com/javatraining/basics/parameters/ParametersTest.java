package com.javatraining.basics.parameters;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ParametersTest {

  @Test
  void varargs_sum_multipleArguments() {
    assertThat(Parameters.sum(1, 2, 3)).isEqualTo(6);
  }

  @Test
  void varargs_sum_noArguments() {
    assertThat(Parameters.sum()).isEqualTo(0);
  }

  @Test
  void varargs_sum_singleArgument() {
    assertThat(Parameters.sum(10)).isEqualTo(10);
  }

  @Test
  void overloading_formatString() {
    assertThat(Parameters.format("hello")).isEqualTo("[hello]");
  }

  @Test
  void overloading_formatStringWithInt() {
    assertThat(Parameters.format("hello", 42)).isEqualTo("[hello:42]");
  }

  @Test
  void overloading_formatStringWithPrefix() {
    assertThat(Parameters.format("hello", ">>")).isEqualTo(">>hello");
  }

  @Test
  void finalParam_greet_returnsExpectedString() {
    assertThat(Parameters.greet("Alice")).isEqualTo("Hello, Alice!");
  }

  @Test
  void finalParam_doubleIt_returnsExpectedValue() {
    assertThat(Parameters.doubleIt(7)).isEqualTo(14);
  }

  @Test
  void builder_defaultValues() {
    Parameters.Config config = new Parameters.Config.Builder().build();
    assertThat(config.getHost()).isEqualTo("localhost");
    assertThat(config.getPort()).isEqualTo(8080);
    assertThat(config.getTimeout()).isEqualTo(30);
  }

  @Test
  void builder_overridePort_defaultsForOthers() {
    Parameters.Config config = new Parameters.Config.Builder().port(9090).build();
    assertThat(config.getPort()).isEqualTo(9090);
    assertThat(config.getHost()).isEqualTo("localhost");
    assertThat(config.getTimeout()).isEqualTo(30);
  }

  @Test
  void builder_allFields() {
    Parameters.Config config =
        new Parameters.Config.Builder().host("example.com").port(443).timeout(60).build();
    assertThat(config.getHost()).isEqualTo("example.com");
    assertThat(config.getPort()).isEqualTo(443);
    assertThat(config.getTimeout()).isEqualTo(60);
  }

  @Test
  void builder_isImmutable_differentInstances() {
    Parameters.Config.Builder builder = new Parameters.Config.Builder();
    Parameters.Config c1 = builder.port(1111).build();
    Parameters.Config c2 = builder.port(2222).build();
    assertThat(c1.getPort()).isEqualTo(1111);
    assertThat(c2.getPort()).isEqualTo(2222);
    assertThat(c1).isNotSameAs(c2);
  }
}
