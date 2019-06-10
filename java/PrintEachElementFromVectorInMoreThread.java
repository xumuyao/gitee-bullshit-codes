import java.util.Vector;
import java.util.concurrent.CyclicBarrier;

/**
 * <pre>
 *
 * 在多线程的场景下，打印java.util.Vector中的每个元素。
 *
 *      我们都知道，在多线程场景下不能使用ArrayList，应该用Vector，因为别人都告诉我们Vector是线程安全的。
 *      但其实，这里是有坑的！！！>_<啊啊啊啊啊啊啊啊啊~
 *
 *      代码发到生产环境后却发现经常报错：java.util.ConcurrentModificationException
 *
 *      例如在下面的代码中，遍历容器 Vector只是简单的把每个元素打印出来，这就存在并发问题。
 *      那么个问题如何修改呢？大家可以试一下。
 * </pre>
 *
 * @author AnXiaole
 * @date 2019/6/10 17:17
 */
public class PrintEachElementFromVectorInMoreThread {

    public static void main(String[] args) throws InterruptedException {

        error(); // 错误情况演示

        // correct(); // 如果想要尝试修改，可在correct()方法中修改。
    }

    /**
     * 执行一下，应该会报如下异常：
     * <pre>
     *     Exception in thread "Thread-0" java.util.ConcurrentModificationException
             at java.util.Vector$Itr.checkForComodification(Vector.java:1184)
             at java.util.Vector$Itr.next(Vector.java:1137)
             at PrintEachElementFromVectorInMoreThread.lambda$error$0(PrintEachElementFromVectorInMoreThread.java:36)
             at java.lang.Thread.run(Thread.java:745)
     * </pre>
     */
    private static void error() {
        // 实际上我的业务代码是没有CyclicBarrier的，CyclicBarrier只是为了演示多个线程'同时'执行的情况。
        // new Thread()的过程比较慢，如果这里不加CyclicBarrier，可能第二个线程还没创建好，第一个线程就执行完了。从而打不到多线程同时执行的目的。
        // 也是因为测试环境并发量比较小导致在测试环境根本就没有发现这个bug。>_<
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);


        Vector<Integer> numbers = new Vector<>();

        new Thread(() -> {
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
            }

            for (int i = 0; i < 1000; i++) {
                numbers.add(i);
            }
        }).start();


        new Thread(() -> {
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
            }

            for (int i = 0; i < 10; i++) {
                for (Integer number : numbers) {
                    System.out.println(number);
                }
            }
        }).start();
    }

    /**
     * 大家可以尝试再这里修改并提交修改后的代码。
     */
    private static void correct() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        Vector<Integer> numbers = new Vector<>();


        new Thread(() -> {
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
            }

            for (int i = 0; i < 10; i++) {
                for (Integer number : numbers) {
                    System.out.println(number);
                }
            }
        }).start();

        new Thread(() -> {
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
            }

            for (int i = 0; i < 1000; i++) {
                numbers.add(i);
            }
        }).start();
    }

}
