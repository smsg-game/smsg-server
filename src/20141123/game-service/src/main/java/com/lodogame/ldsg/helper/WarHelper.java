package com.lodogame.ldsg.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.config.Config;
import com.lodogame.model.WarCity;

public class WarHelper {

	public static boolean isMatchDay(int week) {

		if (week == 3 || week == 7) {
			return true;
		}
		return false;
	}

	public static boolean isCleanRankData(int week) {

		if (week == 3) {
			return true;
		}
		return false;
	}

	public static boolean isRankRewardDay(int week) {

		if (Config.ins().isDebug()) {
			return true;
		}
		if (week == 7) {
			return true;
		}
		return false;
	}

	public static int getInitialPoint(int countryId) {
		Integer point = 0;
		switch (countryId) {
		case 0:
			point = 11;// 魏
			break;
		case 1:
			point = 51;// 蜀
			break;
		case 2:
			point = 55;// 吴
			break;
		case 3:
			point = 15;// 群雄
			break;
		}
		return point;
	}

	public static String getCountryName(int countryId) {
		String name = "";
		switch (countryId) {
		case 0:
			name = "魏";// 魏
			break;
		case 1:
			name = "蜀";// 蜀
			break;
		case 2:
			name = "吴";// 吴
			break;
		case 3:
			name = "群雄";// 群雄
			break;
		}
		return name;
	}

	public static List<Integer> getChooseCountryList(List<WarCity> list) {

		List<Integer> chooses = new ArrayList<Integer>();
		if (list.size() == 0) {
			return chooses;
		}

		double min = list.get(0).getNum();

		for (WarCity wc : list) {
			if (wc.getNum() < min) {
				min = wc.getNum();
			}
		}

		min = min + min * 0.3;
		for (WarCity wc : list) {
			if (wc.getNum() <= 10) {
				continue;
			}
			if (wc.getNum() > min) {
				chooses.add(wc.getCountryId());
			}
		}
		return chooses;
	}

	public static void main(String[] args) {

		Date now = new Date();
		for (int i = 0; i < 7; i++) {
			Date date = DateUtils.addDays(now, i);
			int week = DateUtils.getDayOfWeek(date);
			if (WarHelper.isMatchDay(week)) {
				System.out.println(DateUtils.getDate(date) + "is match day");
			}
		}

	}

}
