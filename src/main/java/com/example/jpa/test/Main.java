package com.example.jpa.test;

/**
 * @author Huangcz
 * @date 2018-11-09 16:18
 * @desc xxx
 */
public class Main {

	public static void main(String[] args) {
		String a = "黄玥";
		System.out.println(clearBracket(a));



	}

	public static String clearBracket(String str) {

		if (str.contains("（")||str.contains("）")){
			String bracket = str.substring(str.indexOf("（"), str.indexOf("）") + 1);
			str = str.replace(bracket, "");
		}
		return str;

	}
}
