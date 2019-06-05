import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 我觉得我写的很棒，但不知道为什么第二天被辞退了
 */
public class DateUtil {

    private static String getDateFormater(Date date){
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat sf2 = new SimpleDateFormat("MM");
        SimpleDateFormat sf3 = new SimpleDateFormat("dd");
        String year = sf1.format(date);
        String month = sf2.format(date);
        String day = sf3.format(date);
        return year+"-"+month+"-"+day;
    }
}
