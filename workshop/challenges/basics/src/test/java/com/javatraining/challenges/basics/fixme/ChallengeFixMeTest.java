package com.javatraining.challenges.basics.fixme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.javatraining.challenges.basics.fixme.BuggyCode.ConnectionLeakBug;
import com.javatraining.challenges.basics.fixme.BuggyCode.ConnectionLeakBug.FakeConnection;
import com.javatraining.challenges.basics.fixme.BuggyCode.MutableAliasBug;
import com.javatraining.challenges.basics.fixme.BuggyCode.NullBug;
import com.javatraining.challenges.basics.fixme.BuggyCode.OffByOne;
import com.javatraining.challenges.basics.fixme.BuggyCode.PinningExample;
import com.javatraining.challenges.basics.fixme.BuggyCode.RaceCounter;
import com.javatraining.challenges.basics.fixme.BuggyCode.SwallowedExceptionBug;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests that document the EXPECTED buggy behavior for the 8 FixMe challenges.
 *
 * <p>After you fix each bug, update the corresponding test to verify the correct behavior instead.
 */
@DisplayName("FixMe Challenges")
class ChallengeFixMeTest {

  // ── Challenge 1: Race condition ───────────────────────────────────────────────

  @Test
  @DisplayName("Challenge 1 – race condition produces incorrect count")
  void raceCondition_countIsWrong() throws InterruptedException {
    RaceCounter counter = new RaceCounter();
    int expected = 1_000;

    counter.incrementMany(expected);

    // BUG: concurrent count++ is not atomic — final value is almost certainly < 1000
    assertThat(counter.getValue())
        .as("count++ without synchronization loses updates under concurrent access")
        .isLessThan(expected);
  }

  // ── Challenge 2: Virtual thread pinning ─────────────────────────────────────

  @Test
  @DisplayName("Challenge 2 – CompletableFuture.join() inside virtual thread pins carrier")
  void virtualThreadPinning_joinBlocksCarrier() {
    PinningExample example = new PinningExample();

    // The code works but .join() inside the virtual thread pins the carrier thread,
    // preventing other virtual threads from being scheduled — a liveness/throughput bug.
    // Under load this degrades to platform-thread behavior.
    String result = example.getData();

    assertThat(result)
        .as("result is correct but carrier was pinned — throughput collapses under load")
        .isEqualTo("data");
  }

  // ── Challenge 3: NullPointerException ────────────────────────────────────────

  @Test
  @DisplayName("Challenge 3 – NullPointerException when list is null")
  void nullDereference_throwsOnNullList() {
    NullBug bug = new NullBug();

    assertThatThrownBy(() -> bug.getFirstName(null))
        .as("getFirstName(null) throws NullPointerException because there is no null guard")
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Challenge 3 – IndexOutOfBoundsException when list is empty")
  void nullDereference_throwsOnEmptyList() {
    NullBug bug = new NullBug();

    assertThatThrownBy(() -> bug.getFirstName(List.of()))
        .as("getFirstName([]) throws because index 0 does not exist")
        .isInstanceOf(IndexOutOfBoundsException.class);
  }

  // ── Challenge 4: Off-by-one ───────────────────────────────────────────────────

  @Test
  @DisplayName("Challenge 4 – last element is not doubled due to off-by-one")
  void offByOne_lastElementIsSkipped() {
    OffByOne bug = new OffByOne();
    int[] input = {1, 2, 3, 4, 5};

    int[] result = bug.doubleAll(input);

    // BUG: loop runs i < length-1, so index 4 (value 5) is never processed
    assertThat(result[4])
        .as("last element should be 10 (5*2) but is 0 because the loop stops one step early")
        .isEqualTo(0);
  }

  // ── Challenge 5: Resource leak ────────────────────────────────────────────────

  @Test
  @DisplayName("Challenge 5 – FileInputStream throws IOException on missing file (no try-with-resources)")
  void resourceLeak_throwsWithoutClosing() {
    BuggyCode.ResourceLeak bug = new BuggyCode.ResourceLeak();

    // The bug is that on success the stream is never closed; on failure it also leaks.
    // We trigger the failure path to prove there's no cleanup guard.
    assertThatThrownBy(() -> bug.readFirstByte("/nonexistent/path/file.txt"))
        .as("FileNotFoundException is thrown and the stream is never closed on the failure path")
        .isInstanceOf(java.io.IOException.class);
  }

  // ── Challenge 6: Mutable object aliasing ────────────────────────────────────

  @Test
  @DisplayName("Challenge 6 – mutation through one alias affects the other")
  void mutableAliasing_mutationVisibleThroughBothReferences() {
    List<String> shared = new ArrayList<>(List.of("a", "b"));
    MutableAliasBug first = new MutableAliasBug(shared);
    MutableAliasBug second = new MutableAliasBug(shared);

    first.add("c");

    // BUG: both instances wrap the same list — second sees the mutation
    assertThat(second.getItems())
        .as("second instance shares the same list reference, so it sees first's mutation")
        .contains("c");
  }

  // ── Challenge 7: Connection leak in a loop ────────────────────────────────────

  @Test
  @DisplayName("Challenge 7 – connections are never closed in the loop")
  void connectionLeak_connectionsNotClosed() {
    ConnectionLeakBug.closedConnections.clear();
    ConnectionLeakBug bug = new ConnectionLeakBug();

    List<String> results = bug.queryAll(new String[] {"db1", "db2", "db3"});

    assertThat(results).hasSize(3);
    // BUG: no connection was closed — in production this exhausts the JDBC pool
    assertThat(ConnectionLeakBug.closedConnections)
        .as("connections were never closed — this would exhaust the connection pool in production")
        .isEmpty();
  }

  // ── Challenge 8: Swallowed exception ─────────────────────────────────────────

  @Test
  @DisplayName("Challenge 8 – invalid input silently returns null instead of failing")
  void swallowedException_returnsNullSilently() {
    SwallowedExceptionBug bug = new SwallowedExceptionBug();

    String result = bug.parseAndDouble("not-a-number");

    // BUG: the NumberFormatException is caught and swallowed — caller gets null with no error
    assertThat(result)
        .as("exception is swallowed and null is returned — caller cannot detect the failure")
        .isNull();
  }
}
