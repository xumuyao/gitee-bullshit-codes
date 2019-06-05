package xx.xx.util;

import java.util.Random;

//生成15位的不重复的ID
//不支持集群分布式
//单项目 15位ID休眠不重复 工具类
//恐怖的是 这工具类居然满足了项目要求
public class IdUtil {
	public static Object lock = new Object();
	public static Random random = new Random();
	
	public static int min = 10;
	public static int max= 99;
	//每秒最多生成 1000/3位id
	public static synchronized long getId(){
		synchronized(lock){
	        int num = new Random().nextInt(max-min+1)+min;
			long id = System.currentTimeMillis();
			try {
				Thread.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return Long.valueOf(String.valueOf(id)+String.valueOf(num));
		}
	}
	public static void main(String[] args) {
		System.out.println(getId());
		System.out.println(String.valueOf(getId()).length());
	}
}
