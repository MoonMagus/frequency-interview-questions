import java.util.concurrent.TimeUnit;

class MyData {
    volatile int number = 0;
    // Uncomment this line will make variable number is not visible.
    //int number = 0;
    public void addTo60() {
        this.number = 60;
    }
}

/**
 * 1 验证volatile的可见性
 * 1.1 假如 int number = 0; number变量之前根本没有添加volatile关键字修饰，没有可见性
 * 1.2 添加了 volatile int number = 0; 可以解决可见性问题。
 */
public class Volatile_Visibility {
    public static void main(String[] args) {
        MyData myData = new MyData();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() +  "\t come in");
            //暂停一会儿线程
            try {
                TimeUnit.SECONDS.sleep(3);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            myData.addTo60();
            System.out.println(Thread.currentThread().getName() + "\t updated number value: " + myData.number);
        }, "AAA").start();

        while(myData.number == 0) {
            // main线程就一直在这里等待循环，直到number的值不为0.
        }

        System.out.println(Thread.currentThread().getName() + "\t mission is over, main get number value:" + myData.number);
    }
}