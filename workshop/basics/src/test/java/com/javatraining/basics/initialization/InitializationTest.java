package com.javatraining.basics.initialization;

import static org.assertj.core.api.Assertions.assertThat;

import com.javatraining.basics.initialization.Initialization.DatabasePool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InitializationTest {

  @BeforeEach
  void resetSingleton() {
    // Reset the singleton between tests so each test starts with a clean slate
    DatabasePool.resetForTesting();
  }

  @Test
  void staticInitializer_configValueIsSet() {
    // Accessing the class triggers the static initializer if not yet run
    assertThat(DatabasePool.staticConfig).isNotNull().isNotBlank();
    assertThat(DatabasePool.staticConfig).isEqualTo("jdbc:h2:mem:training");
  }

  @Test
  void getInstance_returnsSameInstance() {
    DatabasePool first = DatabasePool.getInstance();
    DatabasePool second = DatabasePool.getInstance();
    assertThat(first).isSameAs(second);
  }

  @Test
  void getInstance_sameReference() {
    assertThat(DatabasePool.getInstance() == DatabasePool.getInstance()).isTrue();
  }

  @Test
  void instanceCreationTime_isGreaterThanZero() {
    DatabasePool pool = DatabasePool.getInstance();
    assertThat(pool.getCreationTime()).isGreaterThan(0L);
  }

  @Test
  void instanceInitializer_setsCreationTime() {
    long before = System.nanoTime();
    DatabasePool pool = DatabasePool.getInstance();
    long after = System.nanoTime();
    // creation time was captured during instance initializer, so it falls between before and after
    assertThat(pool.getCreationTime()).isBetween(before, after);
  }
}
