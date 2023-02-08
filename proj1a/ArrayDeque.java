public class ArrayDeque<T> implements Deque<T> {
    private int nextFirst;
    private T[] items;
    private int nextLast;
    private int size;

    ArrayDeque() {
        this.items = (T[]) new Object[8];
        this.nextFirst = this.items.length / 2 - 1;
        this.nextLast = this.nextFirst + 1;
        this.size = 0;
    }

    public void addFirst(T item) {
        if (this.size == this.items.length) {
            resize(2 * this.size);
        }
        this.items[this.nextFirst] = item;
        if (this.nextFirst == 0 && this.size < this.items.length) {
            this.nextFirst = this.items.length - 1;
        } else {
            this.nextFirst -= 1;
        }
        this.size += 1;
    }

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

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void printDeque() {
        for (int i = 0;i<this.size;i++){
            System.out.print(get(i)+" ");
        }
        System.out.println();
    }

    public T removeFirst() {
        int firstIndex = getIndex(0);
        T first = this.items[firstIndex];
        this.items[firstIndex] = null;
        if (this.nextFirst == this.items.length - 1) {
            this.nextFirst = 0;
        } else {
            this.nextFirst += 1;
        }
        this.size -= 1;
        return first;
    }

    public T removeLast() {
        int lastIndex = getIndex(this.size);
        T last = this.items[lastIndex];
        this.items[lastIndex] = null;
        if (this.nextLast == 0) {
            this.nextLast = this.items.length - 1;
        } else {
            this.nextLast -= 1;
        }
        this.size -= 1;
        return last;
    }

    public T get(int index) {
        return items[getIndex(index)];
    }

    private int getIndex(int index) {
        int start = this.nextFirst + 1;
        return start + index > this.size - 1 ? start + index - this.size : start + index;
    }

    private void resize(int newSize) {
        T[] newItems = (T[]) new Object[newSize];
        int nl = (newSize - this.size) / 2;
        int nf = nl - 1;
        int index = this.nextFirst - 1;
        for (int i = 0; i < this.size; i++) {
            newItems[nl] = this.items[index];
            nl += 1;
            index += 1;
            if (index == this.size) {
                index = 0;
            }
        }
        this.nextFirst = nf - 1;
        this.items = newItems;
        this.nextLast = nl;
    }

    public T[] getItems() {
        return this.items;
    }
}