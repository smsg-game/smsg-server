package com.ldsg.battle.util;

public class Convert {

	/**
	 * 转成int型
	 * 
	 * @param str
	 * @return
	 */
	public static int toInt32(String str) {
		return Double.valueOf(str).intValue();
	}

	/**
	 * 转成int型
	 * 
	 * @param obj
	 * @return
	 */
	public static int toInt32(Object obj) {
		return Double.valueOf(obj.toString()).intValue();
	}

	/**
	 * 转成long型
	 * 
	 * @param str
	 * @return
	 */
	public static long toInt64(String str) {
		return Long.parseLong(str);
	}

	/**
	 * 转成string
	 * 
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		return obj != null ? obj.toString() : null;
	}

	/**
	 * 转成double
	 * 
	 * @param str
	 * @return
	 */
	public static double toDouble(String str) {
		return Double.parseDouble(str);
	}

	/**
	 * 转成double
	 * 
	 * @param str
	 * @return
	 */
	public static double toDouble(Object obj) {
		return Double.parseDouble(obj.toString());
	}

}
