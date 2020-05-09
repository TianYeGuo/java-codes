package juc;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

/**
 * Created by 李恒名 on 2017/6/18.
 *
 * 这三个辅助类都基于AQS同步器框架实现，下面我们简单介绍下它们的简单使用
 *
 * CountDownLatch 计数器
 * CountDownLatch类似是一个计数器，他可以实现需要所有任务都执行完毕才可以执行接下来的任务，日常场景中我们可以使用他来做并行分布运算，
 * 借用多核cpu对数据分别进行计算，然后再汇总，也可以实现在加载某些东西前初始化一些信息。
 * 借用多核cpu对数据分别进行计算，然后再汇总，也可以实现在加载某些东西前初始化一些信息。
 * 作者：你干么那么凶
 * 链接：https://www.jianshu.com/p/4f8b6a761da7
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 *
 * 主要方法
 * public CountDownLatch(int count)；//构造函数
 * public void countDown()；//计数器-1
 * public void await() throws InterruptedException；//挂起
 * public boolean await(long timeout, TimeUnit unit);//与await()类似，这里可以指定时间，达到时间如果计数器没归0也可以执行下面的东西
 */
public class CountDownLatchTest {
    static CountDownLatch latch = new CountDownLatch(4);//创建计数器并设置初始值为3

    static void work() {
        System.out.println(Thread.currentThread().getName() + " Work End");
        latch.countDown();//计数器值-1
    }
	//线程池  得到一个你传入大小的线程池
	private static ExecutorService rool(int num) {
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
			.setNameFormat("demo-pool-%d").build();
//		ExecutorService executor = Executors.newFixedThreadPool(4);
		ExecutorService singleThreadPool = new ThreadPoolExecutor(num, num,
			0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
//
//		singleThreadPool.execute(() -> System.out.println(Thread.currentThread().getName()));
		//用完必须关闭线程池
//		singleThreadPool.shutdown();
		return singleThreadPool;
	}
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> work()).start();
        new Thread(() -> work()).start();
        new Thread(() -> work(),"加名字").start();
		long count = latch.getCount();
		System.out.println("计数器为"+count);
		new Thread(() -> {
			try {
				System.out.println("我是线程" + Thread.currentThread().getName() + "我执行在"+
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
				//模拟处理耗时
				Thread.sleep(2000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				latch.countDown();
				System.out.println("计数22器为"+latch.getCount());
			}
		},"hahha").start();




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
