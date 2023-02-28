/**
 * @author fymas
 */
public interface Deque<T> {
    /**
     * 在首位添加元素
     * @param item 需要添加的元素
     */
    public void addFirst(T item);

    /**
     * 在末尾添加元素
     * @param item 需要添加的元素
     */
    public void addLast(T item);

    /**
     * 判断是否为空
     * @return 是否为空
     */
    public boolean isEmpty();

    /**
     * 返回大小
     * @return 大小
     */
    public int size();

    /**
     * 打印
     */
    public void printDeque();

    /**
     * 删除首位元素
     * @return 被删除的元素
     */
    public T removeFirst();

    /**
     * 删除末尾元素
     * @return 被删除的元素
     */
    public T removeLast();

    /**
     * 根据索引获取元素
     * @param index 索引
     * @return 索引对应的元素
     */
    public T get(int index);
}
