package com.lodogame.ldsg.helper;

public class PowerHelper {

	/**
	 * 获取购买体力需要花费的金币
	 * 
	 * 1 10元宝 2～3次 20元宝 4～6次 40元宝 7～21次 80元宝 22～46次 200元宝
	 * 
	 * @param times
	 * @return
	 */
	public static int getBuyPowerNeedMoney(int times) {

		int needMoney = 10;

		if (times == 1) {
			return 10;
		} else if (times >= 2 && times <= 3) {
			return 20;
		} else if (times >= 4 && times <= 6) {
			return 40;
		} else if (times >= 7 && times <= 21) {
			return 80;
		} else if (times >= 22) {
			return 200;
		}

		return needMoney;

	}

}
