public class LinkedListDeque<T> implements Deque<T> {
    private class DequeNode<T> {
        private DequeNode<T> prev;
        private T item;
        private DequeNode<T> next;

        DequeNode(DequeNode<T> p, T i, DequeNode<T> n) {
            this.prev = p;
            this.item = i;
            this.next = n;
        }

    }

    private DequeNode<T> sentinel;
    private int size;

    LinkedListDeque(T x) {
        sentinel = new DequeNode<>(null, null, null);
        sentinel.next = new DequeNode<>(sentinel, x, sentinel);
        sentinel.prev = sentinel.next;
    }

    public LinkedListDeque() {
        sentinel = new DequeNode<>(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        sentinel.next = new DequeNode<>(sentinel, item, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size += 1;
    }

    public void addLast(T item) {
        sentinel.prev.next = new DequeNode<>(sentinel.prev, item, sentinel);
        sentinel.prev = sentinel.prev.next;
        size += 1;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return sentinel.prev == sentinel && sentinel.next == sentinel;
    }

    public void printDeque() {
        LinkedListDeque<T> temp = this;
        while (temp.sentinel.next != temp.sentinel) {
            System.out.print(temp.sentinel.next.item + " ");
            temp.sentinel.next = temp.sentinel.next.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T first = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        this.size -= 1;
        return first;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T last = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        this.size -= 1;
        return last;
    }

    public T get(int index) {
        int i = 0;
        DequeNode<T> temp = this.sentinel.next;
        while (i < this.size) {
            if (index == i) {
                return temp.item;
            }
            i += 1;
            temp = temp.next;
        }
        return null;
    }

    public T getRecursive(int index) {
        return getRecursiveHelp(0, index, this.sentinel.next);
    }

    private T getRecursiveHelp(int start, int index, DequeNode<T> temp) {
        if (start == size) {
            return null;
        }
        if (start == index) {
            return temp.item;
        }
        return getRecursiveHelp(start + 1, index, temp.next);
    }
}
