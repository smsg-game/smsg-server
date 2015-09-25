package com.lodogame.ldsg.helper;

import java.util.HashMap;
import java.util.Map;

public class CopperHelper {

	/**
	 * 获取购买银币需要的金币和得到的银币
	 * 
	 * @param userExtinfo
	 * @return
	 */
	public static Map<String, Integer> getCopperInfo(int times, int userLevel) {

		Map<String, Integer> map = new HashMap<String, Integer>();

		int needMoney = 0;
		int gainCopper = 0;

		if (times <= 9) {
			needMoney = times * 2;
		} else if (times <= 30) {
			needMoney = 20;
		} else if (times <= 50) {
			needMoney = 50;
		} else if (times <= 100) {
			needMoney = 100;
		} else {
			needMoney = 200;
		}

		gainCopper = 1500 + userLevel * 100;

		map.put("ncm", needMoney);
		map.put("nco", gainCopper);

		return map;

	}
}
