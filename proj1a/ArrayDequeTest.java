import org.junit.Assert;
import org.junit.Test;

public class ArrayDequeTest {
    @Test
    public void addResizeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addLast(0);
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.addLast(5);
        ad1.addFirst(6);
        ad1.addFirst(7);
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
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
