package com.wang;

import java.util.Scanner;

/**
 * AI核心代码 价值1个亿~
 * 
 * @author z77z
 *
 */
public class AiMain {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String str;
		while (true) {
			str = sc.next();
			str = str.replace("吗", "");
			str = str.replace("?", "!");
			str = str.replace("？", "！");
			System.out.println(str);
		}
	}
}