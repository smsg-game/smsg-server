package com.lodogame.game.utils;

import org.apache.commons.lang.math.NumberUtils;

public class PartnerUtil {

	public static int getPartnerIdPre(String partnerId) {

		int id = 0;
		if (partnerId != null && partnerId.length() >= 1) {
			id = NumberUtils.toInt(partnerId.substring(0, 1), 1);
		}
		return id;

	}

	public static void main(String[] args) {
		System.out.println(getPartnerIdPre("1004"));
		System.out.println(getPartnerIdPre("2004"));
		System.out.println(getPartnerIdPre("3004"));
	}

}
