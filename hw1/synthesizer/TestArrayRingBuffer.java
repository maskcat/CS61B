package synthesizer;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the ArrayRingBuffer class.
 *
 * @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(10);
        arb.enqueue(1);
        arb.enqueue(2);
        arb.enqueue(3);
        assertEquals(arb.fillCount(), 3);
        int de = arb.dequeue();
        assertEquals(de, 1);
        assertEquals(arb.fillCount(), 2);
        arb.enqueue(4);
        assertEquals(arb.fillCount(), 3);
        int de1 = arb.dequeue();
        int de2 = arb.dequeue();
        int de3 = arb.dequeue();
        assertEquals(de1, 2);
        assertEquals(de2, 3);
        assertEquals(de3, 4);
        assertTrue(arb.isEmpty());
        assertFalse(arb.isFull());
        arb.enqueue(1);
        arb.enqueue(1);
        arb.enqueue(1);
        arb.enqueue(1);
        arb.enqueue(1);
        arb.enqueue(1);
        arb.enqueue(1);
        arb.enqueue(1);
        arb.enqueue(1);
        arb.enqueue(1);
        for (Integer integer : arb) {
            assertEquals(1, integer.intValue());
        }
        assertTrue(arb.isFull());
        assertFalse(arb.isEmpty());


    }

    /**
     * Calls tests for ArrayRingBuffer.
     */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
}
