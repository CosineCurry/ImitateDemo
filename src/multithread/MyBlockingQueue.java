package multithread;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: 阻塞队列，利用链表去实现，头部插入，尾部删除。
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
    /** 显式锁 */
    private final ReentrantLock lock = new ReentrantLock();
    /** 锁对应的条件变量 */
    private final Condition condition = lock.newCondition();

    /**
     * @Description 含有容量的构造方法
     * @Param [capacity] 容量
     * @return
     * @Author cosine
     * @Date 2021/9/28
     */
    public MyBlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
    }

    /**
     * @Description 对外提供的put加入阻塞队列的方法
     * @Param [e]
     * @return void
     * @Author cosine
     * @Date 2021/9/28
     */
    public void put(Object e) throws InterruptedException {
        // 等待可中断
        lock.lockInterruptibly();
        try {
            while (true) {
                // 队列未满的时候执行入队操作并跳出循环
                if (size != capacity) {
                    // 执行入队操作
                    enqueue(e);
                    // 唤醒所有休眠等待的线程
                    condition.signalAll();
                    break;
                }
                // 队列满了进入休眠
                condition.await();
            }
        } finally {
            // 结束释放锁
            lock.unlock();
        }

    }

    /**
     * @Description 对外提供的take取出一个元素的方法
     * @return Object
     * @Author cosine
     * @Date 2021/9/28
     */
    public Object take() throws InterruptedException {
        Object e;
        // 等待可中断
        lock.lockInterruptibly();
        try {
            while (true) {
                // 队列没有空的时候执行出队操作并跳出循环
                if (size > 0) {
                    // 执行出队操作
                    e = dequeue();
                    // 唤醒所有等待的线程
                    condition.signalAll();
                    break;
                }
                // 队列空了进入休眠
                condition.await();
            }
        } finally {
            // 结束释放锁
            lock.unlock();
        }
        return e;
    }

    private void enqueue(Object o) {
        Node node = new Node(o);
        // 队列为空
        if (size == 0) {
            head = node;
            tail = node;
        } else {//队列不为空
            node.next = head;
            head.pre = node;
            head = node;
        }
        size++;
    }

    private Object dequeue() {
        Object o = tail.object;
        // 如果阻塞队列中只有一个元素
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            tail.pre.next = null;
            tail = tail.pre;
        }
        size--;
        return o;
    }
}
