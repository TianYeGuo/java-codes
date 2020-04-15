package juc;

import java.util.concurrent.CountDownLatch;

/**
 * Created by 李恒名 on 2017/6/18.
 *
 * 这三个辅助类都基于AQS同步器框架实现，下面我们简单介绍下它们的简单使用
 *
 * CountDownLatch
 * CountDownLatch类似是一个计数器，他可以实现需要所有任务都执行完毕才可以执行接下来的任务，日常场景中我们可以使用他来做并行分布运算，借用多核cpu对数据分别进行计算，然后再汇总，也可以实现在加载某些东西前初始化一些信息。
 * 作者：你干么那么凶
 * 链接：https://www.jianshu.com/p/4f8b6a761da7
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class CountDownLatchTest {
    static CountDownLatch latch = new CountDownLatch(3);//创建计数器并设置初始值为3

    static void work() {
        System.out.println(Thread.currentThread().getName() + " Work End");
        latch.countDown();//计数器值-1
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> work()).start();
        new Thread(() -> work()).start();
        new Thread(() -> work()).start();

        latch.await();//当前线程（主线程）等待计数器值为0，才会执行
        System.out.println("Main Thread Work End");

        /**
         输出：
             Thread-0 Work End
             Thread-1 Work End
             Thread-2 Work End
             Main Thread Work End
         */
    }
}
