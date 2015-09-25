package com.lodogame.ldsg.helper;

import java.util.List;

import org.apache.commons.lang.math.RandomUtils;

import com.lodogame.model.ActivityDrawTool;

public class DrawHelper {

	/**
	 * 抽奖
	 * 
	 * @param normalDropToolList
	 * @param times
	 * @return
	 */
	public static ActivityDrawTool draw(List<ActivityDrawTool> dropToolList, int random) {

		int rnd = RandomUtils.nextInt(random) + 1;
		if (dropToolList != null && dropToolList.size() > 0) {
			for (ActivityDrawTool activityDrawTool : dropToolList) {
				if (activityDrawTool.getLowerNum() <= rnd && rnd <= activityDrawTool.getUpperNum()) {
					return activityDrawTool;
				}
			}
		}
		return null;

	}

	/**
	 * 获取掉落类型
	 * 
	 * @param times
	 * @return
	 */
	// public static int getDropType(int times) {
	//
	// int type = 1;
	//
	// if (times < 100) {
	// type = 1;
	// } else if (times < 500) {
	// type = 2;
	// } else {
	// type = 3;
	// }
	//
	// return type;
	//
	// }

	/**
	 * 获取掉落类型
	 * 
	 * @param times
	 * @return
	 */
	public static int getDropType(int times) {

		int type = 1;

		if (times < 15) {
			type = 1;
		} else if (times < 30) {
			type = 2;
		} else {
			type = 3;
		}

		return type;

	}
}
