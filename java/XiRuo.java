/**
 * 让人惊艳的高并发处理
 * @author xiruo
 **/
public class XiRuo implements Runnable{

    public int flag = 1;
    private static Object o1 = new Object(), o2 = new Object();

    @Override
    public void run() {
        if (flag == 1) {
            synchronized (o1) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (o2) {
                    // 业务逻辑用sout代替
                    System.out.println("业务逻辑1");
                }
            }
        }
        if (flag == 0) {
            synchronized (o2) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (o1) {
                    // 业务逻辑用sout代替
                    System.out.println("业务逻辑2");
                }
            }
        }
    }

    public static void main(String[] args) {
        XiRuo td1 = new XiRuo();
        XiRuo td2 = new XiRuo();
        td1.flag = 1;
        td2.flag = 0;
        new Thread(td1).start();
        new Thread(td2).start();
    }
}
