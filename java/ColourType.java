/**
 * 
 */
package com.jf.common.constant;

public enum ColourType {
	ColourType_R(10,"红"),
	ColourType_B(20,"蓝"),
	ColourType_G(110,"绿"),
	ColourType_Y(1100,"黄");

	/**
	 * 数据字典ID
	 */
	private Integer id;

	/**
	 * 数据字典名称
	 */
	private String name;
	
	private ColourType(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * 通过ID 查找名字
	 */
	public static String findName(Integer Id) {
		ColourType[] tyList = ColourType.values();
		for (int i = 0; i < tyList.length; i++) {
			if (tyList[i] != null && tyList[i].id == Id) {
				return tyList[i].name;
			}
		}
		return "";
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static void main(String[] args) {
		Integer idB = 20;
		System.out.println("idB对应颜色名：" + ColourType.findName(idB));
		//输出结果：idB对应颜色名：蓝
		
		Integer idY = 1100;
		System.out.println("idY对应颜色名：" + ColourType.findName(idY));
		//输出结果：idY对应颜色名：
		
		//我蒙了，大神为啥呀！你们执行好着吗？
	}
}
