package multithread;

/**
 * @Description: TODO
 * @Author: cosine
 * @Date: 2021/9/28 4:05 下午
 * @Version: 1.0.0
 */
public class MyBlockingQueue {
    /**
     * 双向链表节点存值，插入和删除的时间复杂度为O(1)
     */
    private class Node {
        Object object;
        Node pre;
        Node next;
        public Node(Object object) {this.object = object;}
    }
    /** 头节点和尾节点，头节点插入，尾节点删除 */
    private Node head;
    private Node tail;

    /** 阻塞队列的容量 */
    private int capacity;
    /** 当前的大小 */
    private int size;

    /**
     * @Author kongshuaiying
     * @Description TODO
     * @Date 10:43 下午 2021/9/27
     * @Param [capacity]
     * @return
     */
    public MyBlockingQueue(int capacity) {
        this.capacity = capacity;
    }


}
