import java.util.Date;

public class getData {

	
	/**
	 * 获取下一天的日期
	 * @return
	 */
	public static Date getNextDay()
	{
		try {
			Thread.sleep(24*60*60*1000);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return new Date();
	}
}
