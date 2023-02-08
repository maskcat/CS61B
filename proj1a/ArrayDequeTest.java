import org.junit.Assert;
import org.junit.Test;

public class ArrayDequeTest {
    @Test
    public void addResizeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(1);
        ad1.addLast(2);
        ad1.addLast(3);
        Integer[] t1 = {null, null, null, 1, 2, 3, null, null};
        Assert.assertArrayEquals(ad1.getItems(), t1);

        ad1.addFirst(4);
        Integer[] t2 = {null, null, 4, 1, 2, 3, null, null};
        Assert.assertArrayEquals(ad1.getItems(), t2);

        ad1.addFirst(5);
        ad1.addFirst(6);
        ad1.addFirst(7);
        ad1.addLast(8);
        Integer[] t3 = {6, 5, 4, 1, 2, 3, 8, 7};
        Assert.assertArrayEquals(ad1.getItems(), t3);

        // resize
        ad1.addFirst(9);
        Integer item0 = ad1.get(0);
    }
    @Test
    public void removeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.addLast(5);
        ad1.addLast(6);
        ad1.addLast(7);
        ad1.addLast(8);
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
    }
}
