package test;

import multithread.MyBlockingQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Description: 阻塞队列并发测试
 * @Author: cosine
 * @Date: 2021/9/28 7:07 下午
 * @Version: 1.0.0
 */
public class BlockingQueueTest {
    public static void main(String[] args) throws InterruptedException {

        //BlockingQueue q = new LinkedBlockingDeque();

        final MyBlockingQueue q = new MyBlockingQueue(2);
        final int threads = 2;
        final int times = 10;

        // 线程列表，用于等待所有线程完成
        List<Thread> threadList = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        // 创建两个生产者线程，向阻塞队列并发放入数字0到19，
        for (int i = 0; i < threads; i++) {
            final int offset = i * times;
            Thread producer = new Thread(() -> {
                try {
                    for (int j = 0; j < times; j++) {
                        q.put(new Integer(offset + j));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
            threadList.add(producer);
            producer.start();
        }

        // 创建两个消费者线程，从阻塞队列中弹出20次数字并打印弹出的数字
        for (int i = 0; i < threads; i++) {
            Thread consumer = new Thread(() -> {
                try {
                    for (int j = 0; j < times; j++) {
                        Integer e = (Integer) q.take();
                        System.out.println(e);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
            threadList.add(consumer);
            consumer.start();
        }

        // 等待所有线程执行完成
        for (Thread thread : threadList) {
            thread.join();
        }

        // 打印运行耗时
        long endTime = System.currentTimeMillis();
        System.out.println("总耗时：" + (endTime - startTime) + "ms");
    }
}
