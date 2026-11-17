package com.jubu.pofol.util;

import java.util.function.Supplier;

public class Check {
	
	private Check() {}
	
	public static void notNull(Object obj, Supplier<? extends RuntimeException> e) {
		if (obj == null) {
			throw e.get();
		}
	}
	
	public static void notBlank(String value, Supplier<? extends RuntimeException> e) {
		if (value == null || value.isBlank()) {
			throw e.get();
		}
	}
	
}
