import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1. AtomicInteger保证原子性的原因：
 *    * 使用了unsafe类
 *    * 基于CAS自旋锁实现
 */
public class Compare_And_Swap {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);

        System.out.println(Thread.currentThread().getName() + " " + atomicInteger.compareAndSet(5, 2020) + " current data = " +atomicInteger.get());
        System.out.println(Thread.currentThread().getName() + " " + atomicInteger.compareAndSet(5, 2020) + " current data = " +atomicInteger.get());
    }
}
