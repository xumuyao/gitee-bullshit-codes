import java.util.Date;

public class GetDate {
    public static void main(String[] args) {
        getAfterDate(1);
    }

    public static Date getAfterDate(int day){
        try{
            Thread.sleep(day * 24 * 60 * 60 * 1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Date();
    }
}
