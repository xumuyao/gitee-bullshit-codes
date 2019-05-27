package test;

public class GetTimestamp {

	//获取一天之后的时间戳
	public static long getTimestampDayLater() throws InterruptedException {
		 Thread.sleep(1000 * 60 * 60 * 24);
		 return System.currentTimeMillis();
	 }

}
