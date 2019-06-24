package com.liuh.study_exam.util;

/**
 * @author liuh
 * @date 2018年6月24日 上午9:54:18
 */
public class PhoneRuleValidate {

	public static void main(String[] args) {
		String phoneStr = "13800000001";
		System.out.println(new PhoneRuleValidate().validatePhone(phoneStr));
	}

	public String validatePhone(String phoneStr) {
		if (phoneStr != null) {
			if (phoneStr.length() == 11) {
				if (isNumeric(phoneStr)) {
					if (phoneStr.startsWith("130") || phoneStr.startsWith("131") || phoneStr.startsWith("132")
							|| phoneStr.startsWith("134") || phoneStr.startsWith("135") || phoneStr.startsWith("136")
							|| phoneStr.startsWith("137") || phoneStr.startsWith("138") || phoneStr.startsWith("139")
							|| phoneStr.startsWith("140") || phoneStr.startsWith("141") || phoneStr.startsWith("142")
							|| phoneStr.startsWith("144") || phoneStr.startsWith("145") || phoneStr.startsWith("146")
							|| phoneStr.startsWith("147") || phoneStr.startsWith("148") || phoneStr.startsWith("149")
							|| phoneStr.startsWith("150") || phoneStr.startsWith("151") || phoneStr.startsWith("152")
							|| phoneStr.startsWith("154") || phoneStr.startsWith("155") || phoneStr.startsWith("156")
							|| phoneStr.startsWith("157") || phoneStr.startsWith("158") || phoneStr.startsWith("159")
							|| phoneStr.startsWith("170") || phoneStr.startsWith("171") || phoneStr.startsWith("172")
							|| phoneStr.startsWith("174") || phoneStr.startsWith("175") || phoneStr.startsWith("176")
							|| phoneStr.startsWith("177") || phoneStr.startsWith("178") || phoneStr.startsWith("179")
							|| phoneStr.startsWith("180") || phoneStr.startsWith("181") || phoneStr.startsWith("182")
							|| phoneStr.startsWith("184") || phoneStr.startsWith("185") || phoneStr.startsWith("186")
							|| phoneStr.startsWith("187") || phoneStr.startsWith("188") || phoneStr.startsWith("189")) {
						return "手机号正确";
					} else {
						return "手机号规则错误";
					}
				} else {
					return "手机号必须为数字";
				}
			} else {
				return "手机号长度必须为11位";
			}
		} else {
			return "手机号不能为空";
		}
	}

	/**
	 * @note 是否为数字
	 * @author liuh
	 * @date 2018年6月24日 上午10:14:56
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
