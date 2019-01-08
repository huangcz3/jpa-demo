package com.example.jpa.test;

/**
 * @author Huangcz
 * @date 2018-11-09 16:18
 * @desc xxx
 */
public class Main {

	public static void main(String[] args) {
		String str = "a,b,c, ,";
		String[] arry = str.trim().split(",");
		System.out.println(arry.length);


	}

}
