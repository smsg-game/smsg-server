package com.lodogame.ldsg.helper;

public class UserOnlineRewardHelper {

	public static long getOnlineRewaradTime(int subType) {

		int minute = 0;
		switch (subType) {
		case 1:
			minute = 1;
			break;
		case 2:
			minute = 5;
			break;
		case 3:
			minute = 10;
			break;
		case 4:
			minute = 20;
			break;
		case 5:
			minute = 40;
			break;
		case 6:
			minute = 60;
			break;
		case 7:
			minute = 120;
			break;
		default:
			break;
		}

		return minute * 60 * 1000;
	}
}
