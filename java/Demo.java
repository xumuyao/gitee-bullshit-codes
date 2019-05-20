import java.util.Date;

public class Demo {
    
    /**
     * 返回明天的日期
     */
    public static Date getNextDay() {
        try {
            Thread.sleep(24 * 60 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Date();
    }
    
}