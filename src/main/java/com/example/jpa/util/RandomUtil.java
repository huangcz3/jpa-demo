package com.example.jpa.util;

import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public class RandomUtil extends RandomUtils{

	public static <E> E randomOne(List<E> l){
		if(l == null || l.size() == 0){
			return null;
		}
		int size = l.size();
		return l.get(nextInt(0, size));
	}
}
