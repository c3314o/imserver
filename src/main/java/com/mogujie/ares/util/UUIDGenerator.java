package com.mogujie.ares.util;

import java.util.UUID;


public class UUIDGenerator {
	
	private UUIDGenerator() {
	}

	// 获取uuid
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		// 去掉"-"符号
		String temp = str.replaceAll("-", "");
		return temp.toUpperCase();
	}
	
	public static void main(String[] args) {
		System.out.println(getUUID());
	}
}
