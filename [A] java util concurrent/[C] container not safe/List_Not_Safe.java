import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 笔记
 * 写时复制 copyOnWrite 容器即写时复制的容器 往容器添加元素的时候,不直接往当前容器object[]添加,而是先将当前容器object[]进行
 * copy 复制出一个新的object[] newElements 然后向新容器object[] newElements 里面添加元素 添加元素后,
 * 再将原容器的引用指向新的容器 setArray(newElements);
 * 这样的好处是可以对copyOnWrite容器进行并发的读,而不需要加锁 因为当前容器不会添加任何容器.所以copyOnwrite容器也是一种
 * 读写分离的思想,读和写不同的容器.
 *  // The lock protecting all mutators
 *  final transient ReentrantLock lock=new ReentrantLock();
 *
 *  // The array, accessed only via getArray/setArray.
 *  private transient volatile Object[]array;
 *
 *  public boolean add(E e) {
 *         public boolean add(E e) {
 *         final ReentrantLock lock = this.lock;
 *         lock.lock();
 *         try {
 *             Object[] elements = getArray();
 *             int len = elements.length;
 *             Object[] newElements = Arrays.copyOf(elements, len + 1);
 *             newElements[len] = e;
 *             setArray(newElements);
 *             return true;
 *         } finally {
 *             lock.unlock();
 *         }
 *     }
 */

public class List_Not_Safe {
    public static void main(String[] args) {
        List<String> list = new CopyOnWriteArrayList<>();
        //List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }

        // java.util.ConcurrentModificationException

        /**
         * 1. 故障现象
         *    java.util.ConcurrentModificationException
         *
         * 2. 导致原因
         *     并发修改异常是因为线程并发争抢修改导致。举个例子：上课的时候老师拿了一份名单要点名，
         *     说来了的同学就上去签自己的名字。这份名单就是集合，每个同学就是一个线程。上去签名就
         *     是往集合中添加元素的add操作。当张三同学上去签名的时候，刚写完 “张” 字，李四同学
         *     就上来把笔抢了去，结果就是张三同学的名只签了一半。这就是并发修改异常。
         *
         * 3. 解决方案
         *    3.1 new Vector<>()
         *    3.2 Collections.synchronizedList(new ArrayList<>());
         *    3.3 new CopyOnWriteArrayList<>();
         */
    }
}
