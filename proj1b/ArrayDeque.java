/**
 * @author fymas
 */
public class ArrayDeque<T> implements Deque<T> {
    private int nextFirst;
    private T[] items;
    private int nextLast;
    private int size;

    public ArrayDeque() {
        this.items = (T[]) new Object[8];
        this.nextFirst = this.items.length / 2 - 1;
        this.nextLast = this.nextFirst + 1;
        this.size = 0;
    }

    @Override
    public void addFirst(T item) {
        if (this.size == this.items.length) {
            resize(2 * this.items.length);
        }
        this.items[this.nextFirst] = item;
        if (this.nextFirst == 0 && this.size < this.items.length) {
            this.nextFirst = this.items.length - 1;
        } else {
            this.nextFirst -= 1;
        }
        this.size += 1;
    }

    @Override
    public void addLast(T item) {
        if (this.size == this.items.length) {
            resize(2 * this.size);
        }
        this.items[this.nextLast] = item;
        if (this.nextLast == this.items.length - 1 && this.size < this.items.length) {
            this.nextLast = 0;
        } else {
            this.nextLast += 1;
        }
        this.size += 1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < this.size; i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (needShrink()) {
            resize(this.items.length / 2);
        }
        int firstIndex = index(0);
        T first = this.items[firstIndex];
        if (isEmpty()) {
            return first;
        }
        this.items[firstIndex] = null;
        if (this.nextFirst == this.items.length - 1) {
            this.nextFirst = 0;
        } else {
            this.nextFirst += 1;
        }
        this.size -= 1;
        return first;
    }

    @Override
    public T removeLast() {
        if (needShrink()) {
            resize(this.items.length / 2);
        }
        int lastIndex = index(this.size - 1);
        T last = this.items[lastIndex];
        if (isEmpty()) {
            return last;
        }
        this.items[lastIndex] = null;
        if (this.nextLast == 0) {
            this.nextLast = this.items.length - 1;
        } else {
            this.nextLast -= 1;
        }
        this.size -= 1;
        return last;
    }

    @Override
    public T get(int index) {
        int i = index(index);
        return this.items[i];
    }

    private int index(int index) {
        int start = this.nextFirst + 1;
        if (start + index > this.items.length - 1) {
            return start + index - this.items.length;
        } else {
            return start + index;
        }
    }

    private void resize(int newSize) {
        T[] newItems = (T[]) new Object[newSize];
        int nl = (newSize - this.size) / 2;
        int nf = nl - 1;
        for (int i = 0; i < this.size; i++) {
            int index = index(i);
            newItems[nl] = this.items[index];
            nl += 1;
        }
        this.nextFirst = nf;
        this.items = newItems;
        this.nextLast = nl;
    }

    private boolean needShrink() {
        double usageRate = (double) this.size / this.items.length;
        return usageRate <= 0.25d && this.items.length > 8;
    }
}
