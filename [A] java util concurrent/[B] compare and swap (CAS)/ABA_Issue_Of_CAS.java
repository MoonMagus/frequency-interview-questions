import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ABA_Issue_Of_CAS {
    static AtomicReference<Integer> atomicReference = new AtomicReference<>(100);

    public static void main(String[] args) {
        System.out.println("==================以下是ABA问题的产生====================");
        new Thread(() -> {
            atomicReference.compareAndSet(100, 101);
            atomicReference.compareAndSet(101, 100);
        }, "t1").start();

        new Thread(() -> {
            // 暂停两秒2线程，保证线程1已经完成了ABA操作
            try {
                TimeUnit.SECONDS.sleep(2);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(atomicReference.compareAndSet(100, 2019) + "\t" + atomicReference.get());
        }, "t2").start();
    }
}
