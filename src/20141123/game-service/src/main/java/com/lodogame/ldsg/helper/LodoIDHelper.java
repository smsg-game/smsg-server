package com.lodogame.ldsg.helper;

public class LodoIDHelper {

	public static String getIdSaveKey(String serverId) {
		return "lodo_id_" + serverId;
	}

	public static long getLodoId(String serverId, long id) {
		String ind = serverId.replaceAll("[a-zA-Z]+", "");
		String sid = String.valueOf(id);
		String s = ind + "000000".substring(0, 6 - sid.length()) + sid;
		return Long.valueOf(s);
	}

	public static void main(String[] args) {
		System.out.println(getLodoId("s1", 1));
		System.out.println(getLodoId("s1", 10000));
		System.out.println(getLodoId("s1", 45));
		System.out.println(getLodoId("t1", 45));
		System.out.println(getLodoId("T1", 45));
	}
}
