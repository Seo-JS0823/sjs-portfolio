package com.jubu.pofol.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public final class YmdDate {

	private static final DateTimeFormatter FORMAT =
			DateTimeFormatter.ofPattern("uuuuMMdd").withResolverStyle(ResolverStyle.STRICT);

	private YmdDate() {}

	public static boolean isValid(String value) {
		if (value == null || !value.matches("\\d{8}")) {
			return false;
		}

		try {
			LocalDate.parse(value, FORMAT);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}

	public static String format(String value) {
		if (value == null) {
			return null;
		}

		return LocalDate.parse(value, FORMAT).toString();
	}

}
