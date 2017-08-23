package com.servicos.estatica.resicolor.lab.util;

public class NumberUtil {

	public static String adjustDecimal(String value) {
		if (value == null || value.trim().equals("")) {
			return "0";
		}
		if (value.contains(",")) {
			return value.replaceAll(",", ".");
		}
		return value;
	}
}
