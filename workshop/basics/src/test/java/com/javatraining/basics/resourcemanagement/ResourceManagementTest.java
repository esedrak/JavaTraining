package com.javatraining.basics.resourcemanagement;

import static org.assertj.core.api.Assertions.assertThat;

import com.javatraining.basics.resourcemanagement.ResourceManagement.ManagedConnection;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourceManagementTest {

  private ResourceManagement resourceManagement;

  @BeforeEach
  void setUp() {
    resourceManagement = new ResourceManagement();
  }

  @Test
  void managedConnection_isClosedAfterTryWithResources() {
    ManagedConnection conn = new ManagedConnection("closeable-test");
    try (conn) {
      assertThat(conn.isClosed()).isFalse();
    }
    assertThat(conn.isClosed()).isTrue();
  }

  @Test
  void managedConnection_isClosedEvenWhenExceptionThrown() {
    boolean wasClosed = resourceManagement.closeOnException();
    assertThat(wasClosed).isTrue();
  }

  @Test
  void queryWithResource_returnsExpectedString() {
    String result = resourceManagement.queryWithResource("SELECT 1");
    assertThat(result).isEqualTo("Result for: SELECT 1");
  }

  @Test
  void twoResources_closedInLifoOrder() {
    // Use tracking subclass to capture close order
    List<String> closeOrder = new ArrayList<>();

    ManagedConnection first =
        new ManagedConnection("first") {
          @Override
          public void close() {
            closeOrder.add(getName());
            super.close();
          }
        };

    ManagedConnection second =
        new ManagedConnection("second") {
          @Override
          public void close() {
            closeOrder.add(getName());
            super.close();
          }
        };

    try (first;
        second) {
      first.query("SELECT 1");
      second.query("SELECT 2");
    }

    // LIFO: second opened last, so it closes first
    assertThat(closeOrder).containsExactly("second", "first");
  }

  @Test
  void managedConnection_query_returnsResultForSql() {
    ManagedConnection conn = new ManagedConnection();
    String result = conn.query("SELECT *");
    assertThat(result).isEqualTo("Result for: SELECT *");
    conn.close();
  }
}
