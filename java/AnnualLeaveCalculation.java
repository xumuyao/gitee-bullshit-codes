package com.wang;

import java.util.Scanner;

/**
 * 年假计算,认识公司最牛逼的人
 * 
 * @author fans.fan
 *
 */
public class AnnualLeaveCalculation {

	public int calculation(User user) {
		if ("xxx".equal(user.getUsername())) {
			return 100;
		} else {
			int year = DateUtil.daysBetween(new Date(), user.getHireDate()) / 365;
			if(year == 0) {
				return 5;
			} else if(year > 1 && year <= 5) {
				return 7
			} else if(year > 5 && year <= 10) {
				return 9;
			} else if(year > 10) {
				return 10;
			}
		}
	}
	
	class User{
		/** 用户名 */
		private String username;
		/** 入职日期 */
		private Date hireDate;
	}
}