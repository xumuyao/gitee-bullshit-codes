package com.ccservice.ydx.common.authentication;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User:Administrator Date 2019/7/10 Time 17:58
 */
public class NewFeatures {

	public static void main(String[] args) {
		// 创建map
		HashMap<Integer, List<Integer>> map = new HashMap<>();
		List<Integer> defaultList = Arrays.stream(new int[2]).boxed().collect(Collectors.toList());
		// java8新特性，根据key获取value,key不存在，取默认值
		List<Integer> listA = map.getOrDefault(1, defaultList);
		listA.add(1);
		System.out.println("listA:" + listA);
		List<Integer> listB = map.getOrDefault(2, defaultList);
		listB.add(2);
		System.out.println("listB:" + listB);
	}
}
