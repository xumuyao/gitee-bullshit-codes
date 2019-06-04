import java.util.Scanner;
import org.junit.Test;

/***
 * 
 * 价值十亿的人工智能代码
 * 
 */

public class Ai {
	public static void main(String args[]) {
		Scanner sc= new Scanner(System.in);
		String str=null;
		while(true) {
			str=sc.next();
			str= str.replace("吗", "");
			str= str.replace("?", "!");
			System.out.println(str);
			
		}
		
	}
	

}
