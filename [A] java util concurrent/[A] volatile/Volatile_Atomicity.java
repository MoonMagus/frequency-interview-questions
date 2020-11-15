import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyData {
    volatile int number = 0;
    // Uncomment this line will make variable number is not visible.
    //int number = 0;

    public void addPlusPlus() {
        // 此时number前面是加了volatile修饰的，但是不保证原子性
        number++;
    }

    AtomicInteger atomicInteger = new AtomicInteger();
    public void addAtomicInteger() {
        atomicInteger.getAndIncrement();
    }
}

/**
 * 2 验证volatile不保证原子性
 * 2.1 原子性指的是什么意思？
 *    不可分割，完整性，也即某个线程正在做某个具体业务时，中间不可以被加塞或者被分割。需要整体完整，要么同时成功，要么同时失败。
 * 2.2 volatile不保证原子性的案例演示
 * 2.3 Why
 *     * 反汇编.class文件，即可看出来在壁垒屏障之前的读改写包含三条汇编指令，是很难保证不被分割的。比如线程2已经完成了读和改，是不会重新再用
 *     * 新修改的值进行写回的。
 * 2.4 如何解决原子性？
 *     * 加sync
 *     * 使用AtomicInteger, 底层使用了CAS
 */
public class Volatile_Atomicity {
    public static void main(String[] args) {
        MyData myData = new MyData();
        for(int i = 1; i <= 20;++i) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    myData.addPlusPlus();
                    myData.addAtomicInteger();
                }
            }, String.valueOf(i)).start();
        }

        // 需要等待上面20个线程都计算完成之后，由main线程统计number值
        while(Thread.activeCount() > 2) {
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName() + "\t int type, finally number value:" + myData.number);
        System.out.println(Thread.currentThread().getName() + "\t atomicInteger type, finally number value:" + myData.atomicInteger);
    }
}
