package juc;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by 李恒名 on 2017/6/18.
 *
 * Semaphore
 * 信号量，它可用于对资源进行有效的控制，获取到许可就可以使用，使用完许可主动释放掉，获取不到就需要等到有许可可以使用
 * Semaphore其实和锁有点类似，它一般用于控制对某组资源的访问权限
 *
 * 用于那些资源有明确访问数量限制的场景，常用于限流 。
 * 比如：数据库连接池，同时进行连接的线程有数量限制，连接不能超过一定的数量，当连接达到了限制数量后，后面的线程只能排队等
 * 前面的线程释放了数据库连接才能获得数据库连接。
 * 比如：停车场场景，车位数量有限，同时只能容纳多少台车，车位满了之后只有等里面的车离开停车场外面的车才可以进入。
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        //总共 三个厕所，一开始三个人上，但是现在有5个线程，剩下的两个线程必须在前三个线程释放资源后才可以上厕所
        WC wc = new WC();
        wc.ticket();
       /* new Thread(() -> wc.use()).start();
        new Thread(() -> wc.use()).start();
        new Thread(() -> wc.use()).start();

        new Thread(() -> wc.use()).start();
        new Thread(() -> wc.use()).start();*/
        /**
         输出：
             Thread-1 正在使用卫生间
             Thread-2 正在使用卫生间
             Thread-0 正在使用卫生间
             Thread-0 使用完毕
             Thread-2 使用完毕
             Thread-1 使用完毕
             Thread-3 正在使用卫生间
             Thread-4 正在使用卫生间
             Thread-4 使用完毕
             Thread-3 使用完毕
         */
    }
}

class WC {
    private Semaphore semaphore = new Semaphore(10);//最大线程许可量
    //厕所
    public void use() {
        try {
            //获得许可  获取一个或多个令牌，在获取到令牌、或者被其他线程调用中断之前线程一直处于阻塞状态。
            semaphore.acquire(2);
            System.out.println(Thread.currentThread().getName() +" 正在使用卫生间");
            TimeUnit.SECONDS.sleep(1);
            System.out.println(Thread.currentThread().getName() +" 使用完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            //释放你获取的一个或多个许可
            semaphore.release(2);
        }
    }

    public void ticket() {
        //模拟100辆车进入停车场
        for(int i=0;i<100;i++){
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("===="+Thread.currentThread().getName()+"来到停车场");
                        if(semaphore.availablePermits()==0){
                            System.out.println("车位不足，请耐心等待");
                        }
                        semaphore.acquire();//获取令牌尝试进入停车场
                        System.out.println(Thread.currentThread().getName()+"成功进入停车场");
                        Thread.sleep(new Random().nextInt(10000));//模拟车辆在停车场停留的时间
                        System.out.println(Thread.currentThread().getName()+"驶出停车场");
                        semaphore.release();//释放令牌，腾出停车场车位
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },i+"号车");
            thread.start();
        }
    }



}
//https://zhuanlan.zhihu.com/p/98593407
/*
 *
5、Semaphore实现原理
(1)、Semaphore初始化。
Semaphore semaphore=new Semaphore(2);
1、当调用new Semaphore(2) 方法时，默认会创建一个非公平的锁的同步阻塞队列。
2、把初始令牌数量赋值给同步队列的state状态，state的值就代表当前所剩余的令牌数量。

（2）获取令牌
semaphore.acquire();
1、当前线程会尝试去同步队列获取一个令牌，获取令牌的过程也就是使用原子的操作去修改同步队列的state ,获取一个令牌则修改为state=state-1。
2、 当计算出来的state<0，则代表令牌数量不足，此时会创建一个Node节点加入阻塞队列，挂起当前线程。
3、当计算出来的state>=0，则代表获取令牌成功。

(3)、释放令牌
 semaphore.release();
当调用semaphore.release() 方法时
1、线程会尝试释放一个令牌，释放令牌的过程也就是把同步队列的state修改为state=state+1的过程
2、释放令牌成功之后，同时会唤醒同步队列的所有阻塞节共享节点线程
3、被唤醒的节点会重新尝试去修改state=state-1 的操作，如果state>=0则获取令牌成功，否则重新进入阻塞队列，挂起线程。

































 */
