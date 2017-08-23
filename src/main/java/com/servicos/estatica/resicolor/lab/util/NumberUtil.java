package com.servicos.estatica.resicolor.lab.util;

public class NumberUtil {

	public static String adjustDecimal(String value, String origin, String destiny) {
		if (value == null || value.trim().equals("")) {
			return "0";
		}
		if (value.contains(origin)) {
			return value.replace(origin, destiny);
		}
		return value;
	}

}
