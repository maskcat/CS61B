import org.junit.Test;

public class ArrayDequeTest {
    @Test
    public void addResizeGetTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addLast(0);
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.addLast(5);
        ad1.addLast(6);
        ad1.addFirst(7);
        ad1.addFirst(8);
        int item01 = ad1.get(6);

    }

    @Test
    public void resizeNullTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addLast(0);
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.addLast(5);
        ad1.addLast(6);
        ad1.addLast(7);
        Integer first = ad1.removeFirst();
        Integer last = ad1.removeLast();
    }

    @Test
    public void emptyRemoveSizeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.removeFirst();
        Integer last = ad1.size();
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
