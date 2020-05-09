package juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by 李恒名 on 2017/6/18.
 *
 * CyclicBarrier 回环栅栏
 * 回环栅栏，他可以使线程全部到达一个同步点后，再一起执行下面的动作，他是可重用的，等线程到达同步点，这个线程是可以被做其他使用的
 * ，我们姑且叫这个状态为可重用态，当调用await()，线程就为可重用态
 * 主要方法
 * public CyclicBarrier(int parties);//构造方法
 * public CyclicBarrier(int parties, Runnable barrierAction);//构造方法，可实现更复杂的动作
 * public int await() throws InterruptedException, BrokenBarrierException;//挂起
 * public int await(long timeout, TimeUnit unit);//带时间，到期可执行下面操作
 *
 *
 *   如何使用   启用多个线程  去分别完成不同的任务    加速接口的返回   然后
 *
 */
public class CyclicBarrierTest {
    //定义一个barrier并设置parties，当线程数达到parties后，barrier失效，线程可以继续运行，在未达到parties值之前，线程将持续等待。
    static CyclicBarrier barrier = new CyclicBarrier(3,()-> System.out.println("栅栏：“这么多猪，我恐怕扛不住了”"+
        "此时正重用的线程为"+
        Thread.currentThread().getName()));

    static void go() {
        System.out.println("小猪[" + Thread.currentThread().getName() + "] 在栅栏边等待其他小猪");
        try {
            barrier.await();//等待数+1
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        //CountDownLatch与CyclicBarrier的比较
        //他们的功能有一些类似，CountDownLatch是所有线程都到达一个点才能执行下面的动作，而CyclicBarrier是所有线程都到达一个点再一起执行下面的动作
        // CountDownLatch不可被重用，CyclicBarrier可以被重用
//        CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；
//        而CyclicBarrier一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；
        System.out.println("猪到齐了，小猪[" + Thread.currentThread().getName() + "] 与其他小猪一起冲破栅栏");
    }

    public static void main(String[] args) {

        new Thread(() -> go()).start();
        new Thread(() -> go()).start();
        new Thread(() -> go()).start();

        /**
         输出：
             小猪[Thread-0] 在栅栏边等待其他小猪
             小猪[Thread-1] 在栅栏边等待其他小猪
             小猪[Thread-2] 在栅栏边等待其他小猪
             栅栏：“这么多猪，我恐怕扛不住了”
             猪到齐了，小猪[Thread-2] 与其他小猪一起冲破栅栏
             猪到齐了，小猪[Thread-0] 与其他小猪一起冲破栅栏
             猪到齐了，小猪[Thread-1] 与其他小猪一起冲破栅栏
         */
    }
}


