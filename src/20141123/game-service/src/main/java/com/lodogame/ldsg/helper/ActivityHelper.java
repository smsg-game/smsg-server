package com.lodogame.ldsg.helper;

import java.util.Date;

import com.lodogame.game.utils.DateUtils;
import com.lodogame.model.SystemRecivePower;

public class ActivityHelper {

	/**
	 * 活动是否开放
	 * 
	 * @param openWeeks
	 * @return
	 */
	public static boolean isActivityOpen(String openWeeks) {

		int dayOfWeek = DateUtils.getDayOfWeek();
		if (dayOfWeek == 1) {
			dayOfWeek = 7;
		} else {
			dayOfWeek = dayOfWeek - 1;
		}

		return openWeeks != null && openWeeks.indexOf(String.valueOf(dayOfWeek)) >= 0;

	}

	/**
	 * 当前是否可以领体力
	 * 
	 * @param systemRecivePower
	 * @return
	 */
	public static boolean isNowCanRecive(SystemRecivePower systemRecivePower) {

		if (systemRecivePower == null) {
			return false;
		}

		Date now = new Date();

		Date start = DateUtils.str2Date(DateUtils.getDate() + " " + systemRecivePower.getStartTime());
		Date end = DateUtils.str2Date(DateUtils.getDate() + " " + systemRecivePower.getEndTime());

		if (now.before(start) || now.after(end)) {
			return false;
		}

		return true;
	}

	public static void main(String[] args) {
		System.out.println(isActivityOpen("1, 6, 7"));
		System.out.println(isActivityOpen("1,2,3,4,5,7"));
		System.out.println(isActivityOpen("6"));
	}

}
