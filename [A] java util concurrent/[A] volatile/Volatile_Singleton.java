public class Volatile_Singleton {
    // 使用volatile禁止指令重拍
    private static volatile Volatile_Singleton instance = null;

    private Volatile_Singleton() {
        System.out.println(Thread.currentThread().getName() + "new Volatile_Singleton()...");
    }

    // DCL: double check lock, 双检锁机制
    public static Volatile_Singleton getInstance() {
        if(instance == null) {
            synchronized (Volatile_Singleton.class) {
                if(instance == null) {
                    // 分解指令：
                    // 1. memory = allocate() : 分配对象内存空间
                    // 2. instance(memory) : 初始化对象
                    // 3. instance = memory : 设置instance指向刚分配的内存地址，此时instance != null
                    // 其中指令2,3没有数据依赖关系，如果没有volatile指令设置内存屏障，可能出现重拍。从而可能存在线程获得一个没有初始化的地址
                    instance = new Volatile_Singleton();
                }
            }
        }

        return instance;
    }

    public static void main(String[] args) {
        for(int i = 1; i <= 10;i++) {
            new Thread(() -> {
                Volatile_Singleton.getInstance();
            }, String.valueOf(i)).start();
        }
    }
}
