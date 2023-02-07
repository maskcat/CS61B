public class LinkedListDeque<T> {
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
        sentinel.next = new DequeNode<>(sentinel, x, null);
        sentinel.prev = sentinel.next;
    }

    LinkedListDeque() {
        sentinel = new DequeNode<>(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        sentinel.next = new DequeNode<>(sentinel.next ,item, sentinel.next.next);
        sentinel.prev = sentinel.next;
        size += 1;
    }

    public void addLast(T item) {
        sentinel.prev = new DequeNode<>(sentinel.prev.prev,item,sentinel.next);

        size += 1;
    }

    public int size() {
        return this.size;
    }
    public boolean isEmpty(){
        return sentinel.prev == sentinel.next;
    }
    public void printDeque(){
        LinkedListDeque<T> temp = this;
        while (!temp.isEmpty()){
            System.out.println(temp.sentinel.next + " ");
            temp.sentinel.next = temp.sentinel.next.next;
        }
    }
}
