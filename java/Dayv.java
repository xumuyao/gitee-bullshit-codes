package com.test.java.enumtest;


public class Dayv {
	
	//记得刚工作的时候踩过这样一个坑 当时debug发现相等数值情况下有时返回true 有时返回false 
    // 这个问题有在网上看过，关于Integer对象的值，在127以内时，值会缓存起来，值相同时会返回同一个引用，因此==判断两个对象的引用相等结果返回true，但是超过127之后就没有缓存，因此每次new都是一个新的引用，因此就返回false
    // 具体可百度/谷歌搜索“java integer 127 缓存”
    // 参考：https://www.cnblogs.com/xiehongwei/p/7595520.html
    // 参考：https://www.jianshu.com/p/ba0bc8ea3d3a
	public boolean isEqual(Integer a,Integer b){
		return a==b;
	}
}
