/**
 * 
 * if 无敌判断，程序被执行了多次，断电也打了，怎么会执行多次。 <p>
 * 创建日期：2019年6月27日<br>
 * 修改历史：<br>
 * 修改日期：<br>
 * 修改作者：<br>
 * 修改内容：<br>
 * @version 1.0
 */
public class IfDemo {

	public static void main(String[] args) {
		howOldAreYou(60);
	}

	public static void howOldAreYou(int age) {
		if (age > 0) {
			System.out.println("我是小学生");
		}
		if (age > 10) {
			System.out.println("我是中学生");
		}
		if (age > 20) {
			System.out.println("我是大学生");
		}
		if (age > 30) {
			System.out.println("我是研究生");
		}
		if (age > 50) {
			System.out.println("我是博士生");
		}
		if (age > 100) {
			System.out.println("我是老神仙");
		}
	}

}
