public class DangerLog {
	public class ReadLog {
		int readNum;

		@Override
		public String toString() {
			// 调用该方法将更改对象的状态，打印或者调试都可能会调用该方法
			readNum++;
			return "ReadLog [readNum:" + readNum + ",其他信息]";
		}
	}
	
	public ReadLog getReadLog() {
		ReadLog log = new ReadLog();
		// 删除下面不需要的日志代码或者调试时添加监视都会导致业务出错
		System.out.println(log);
		return log;
	}
}
