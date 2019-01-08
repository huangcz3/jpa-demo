package com.example.jpa.test;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Huangcz
 * @date 2018-11-12 13:51
 * @desc xxx
 */
public final class Phone {

	private final short areaCode;

	private final short prefix;

	private final short lineNumber;

	public Phone(int areaCode, int prefix, int lineNumber) {

		rangeCheck(areaCode, 999, "area code");
		rangeCheck(prefix, 999, "prefix");
		rangeCheck(lineNumber, 9999, "line number");

		this.areaCode = (short) areaCode;
		this.prefix = (short) prefix;
		this.lineNumber = (short) lineNumber;
	}

	private static void rangeCheck(int args, int max, String name) {
		if (args < 0 || args > max) {
			throw new IllegalArgumentException(name + ": " + args);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Phone phone = (Phone) o;
		return areaCode == phone.areaCode &&
				prefix == phone.prefix &&
				lineNumber == phone.lineNumber;
	}

	public static void main(String[] args) {
		Phone phone = new Phone(707,867,5309);
		Map<Phone,String> map = new HashMap<>();
		map.put(phone,"tom");
		String val = map.get(new Phone(707,867,5309));
		System.out.println(val);
		System.out.println(map);
	}


}
