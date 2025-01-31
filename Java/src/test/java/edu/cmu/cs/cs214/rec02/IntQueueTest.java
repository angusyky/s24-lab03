package edu.cmu.cs.cs214.rec02;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;


/**
 * TODO:
 * 1. The {@link LinkedIntQueue} has no bugs. We've provided you with some example test cases.
 * Write your own unit tests to test against IntQueue interface with specification testing method
 * using mQueue = new LinkedIntQueue();
 * <p>
 * 2.
 * Comment `mQueue = new LinkedIntQueue();` and uncomment `mQueue = new ArrayIntQueue();`
 * Use your test cases from part 1 to test ArrayIntQueue and find bugs in the
 * {@link ArrayIntQueue} class
 * Write more unit tests to test the implementation of ArrayIntQueue, with structural testing method
 * Aim to achieve 100% line coverage for ArrayIntQueue
 *
 * @author Alex Lockwood, George Guo, Terry Li
 */
public class IntQueueTest {

  private IntQueue mQueue;
  private List<Integer> testList;
  private ArrayIntQueue arrayIntQueue;
  private List<Integer> bigTestList;

  /**
   * Called before each test.
   */
  @Before
  public void setUp() {
    // For specification testing
    // mQueue = new LinkedIntQueue();
    mQueue = new ArrayIntQueue();
    testList = new ArrayList<>(List.of(1, 2, 3));

    // For structural testing
    arrayIntQueue = new ArrayIntQueue();
    bigTestList = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
  }

  @Test
  public void testIsEmpty() {
    // This is an example unit test
    assertTrue(mQueue.isEmpty());
  }

  @Test
  public void testNotEmpty() {
    final Integer EXPECTED_VALUE = 1;
    mQueue.enqueue(EXPECTED_VALUE);
    assertFalse(mQueue.isEmpty());
  }

  @Test
  public void testPeekEmptyQueue() {
    assertNull(mQueue.peek());
  }

  @Test
  public void testPeekNoEmptyQueue() {
    final Integer EXPECTED_VALUE = 1;
    mQueue.enqueue(EXPECTED_VALUE);
    assertEquals(mQueue.peek(), EXPECTED_VALUE);
  }

  @Test
  public void testEnqueue() {
    // This is an example unit test
    for (int i = 0; i < testList.size(); i++) {
      mQueue.enqueue(testList.get(i));
      assertEquals(testList.getFirst(), mQueue.peek());
      assertEquals(i + 1, mQueue.size());
    }
  }

  @Test
  public void testDequeue() {
    // Setup with enqueue
    for (int i = 0; i < testList.size(); i++) {
      mQueue.enqueue(testList.get(i));
      assertEquals(testList.getFirst(), mQueue.peek());
      assertEquals(i + 1, mQueue.size());
    }

    // Verify with dequeue
    for (int i = 0; i < testList.size(); i++) {
      assertEquals(testList.get(i), mQueue.dequeue());
      assertEquals(testList.size() - i - 1, mQueue.size());
    }
  }

  @Test
  public void testDequeueEmptyQueue() {
    assertNull(mQueue.dequeue());
  }

  @Test
  public void testContent() throws IOException {
    // This is an example unit test
    InputStream in = new FileInputStream("src/test/resources/data.txt");
    try (Scanner scanner = new Scanner(in)) {
      scanner.useDelimiter("\\s*fish\\s*");

      List<Integer> correctResult = new ArrayList<>();
      while (scanner.hasNextInt()) {
        int input = scanner.nextInt();
        correctResult.add(input);
        System.out.println("enqueue: " + input);
        mQueue.enqueue(input);
      }

      for (Integer result : correctResult) {
        assertEquals(mQueue.dequeue(), result);
      }
    }
  }

  // Structural Tests

  @Test
  public void testArrayIntQueueClear() {
    for (int i = 0; i < testList.size(); i++) {
      mQueue.enqueue(testList.get(i));
      assertEquals(testList.getFirst(), mQueue.peek());
      assertEquals(i + 1, mQueue.size());
    }

    arrayIntQueue.clear();

    assertTrue(arrayIntQueue.isEmpty());
  }

  @Test
  public void testArrayIntQueueEnsureCapacity() {
    // Force maximum capacity with enqueue
    bigTestList.forEach(integer -> arrayIntQueue.enqueue(integer));

    // Dequeue to move head pointer to middle of array
    arrayIntQueue.dequeue();

    // Enqueue more elements beyond initial capacity
    bigTestList.forEach(integer -> arrayIntQueue.enqueue(integer));

    // Test that capacity has increased
    final int EXPECTED_SIZE = 2 * bigTestList.size() - 1;
    assertEquals(EXPECTED_SIZE, arrayIntQueue.size());

    // Test that logical queue order is preserved
    for (int i = 0; i < EXPECTED_SIZE; ++i) {
      int bigTestListIndex = (i + 1) % bigTestList.size();
      assertEquals(bigTestList.get(bigTestListIndex), arrayIntQueue.dequeue());
    }
  }
}
