package com.lodogame.ldsg.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * 争霸赛
 * 
 * @author CJ
 * 
 */
public class PkHelper {

	public static void setAttackAbleList(List<Integer> attackAbleList, int rank) {

		if (attackAbleList.size() == 11) {
			return;
		}

		if (rank <= 10 && attackAbleList.isEmpty()) {
			for (int i = 1; i <= 11; i++) {
				attackAbleList.add(i);
			}
			return;
		}

		if (attackAbleList.size() == 0) {
			attackAbleList.add(rank);
		}

		int nextRank = 0;

		int step = getStep(rank);

		nextRank = rank - step;

		attackAbleList.add(nextRank);

		setAttackAbleList(attackAbleList, nextRank);

	}

	/**
	 * 获得步长
	 * 
	 * @param rank
	 * @param useMoney
	 * @return
	 */
	public static int getStep(int rank) {

		if (rank <= 50) {
			return 1;
		} else if (rank <= 100) {
			return 5;
		} else if (rank <= 1000) {
			return 50;
		} else {
			return 100;
		}
	}

	public static int getGroup(int level) {
		if (level >= 20 && level <= 39) {
			return 1;
		} else if (level >= 40 && level <= 49) {
			return 2;
		} else if (level >= 50 && level <= 59) {
			return 3;
		} else if (level >= 60 && level <= 69) {
			return 4;
		} else if (level >= 70 && level <= 79) {
			return 5;
		} else if (level >= 80 && level <= 89) {
			return 6;
		} else if (level >= 90 && level <= 99) {
			return 7;
		} else if (level >= 100 && level <= 109) {
			return 8;
		} else if (level >= 110 && level <= 119) {
			return 9;
		} else if (level >= 120 && level <= 129) {
			return 10;
		} else if (level >= 130 && level <= 139) {
			return 11;
		} else if (level >= 140 && level <= 149) {
			return 12;
		} else {
			return 13;
		}
	}

	public static String getGroupTitle(int group) {
		String groupTitle = "";
		switch (group) {
		case 1:
			groupTitle = "新兵组";
			break;
		case 2:
			groupTitle = "精锐组";
			break;
		case 3:
			groupTitle = "禁军组";
			break;
		case 4:
			groupTitle = "校尉组";
			break;
		case 5:
			groupTitle = "都尉组";
			break;
		case 6:
			groupTitle = "将军Ⅴ";
			break;
		case 7:
			groupTitle = "将军Ⅳ";
			break;
		case 8:
			groupTitle = "将军Ⅲ";
			break;
		case 9:
			groupTitle = "将军Ⅱ";
			break;
		case 10:
			groupTitle = "将军Ⅰ";
			break;
		case 11:
			groupTitle = "提督组";
			break;
		case 12:
			groupTitle = "元帅组";
			break;
		case 13:
			groupTitle = "诸侯组";
			break;
		}
		return groupTitle;
	}

	public static String getTitle(int group) {
		String title = "";
		switch (group) {
		case 1:
			title = "新兵之王";
			break;
		case 2:
			title = "铁血精锐";
			break;
		case 3:
			title = "羽林禁军";
			break;
		case 4:
			title = "奋勇校尉";
			break;
		case 5:
			title = "虎贲都尉";
			break;
		case 6:
			title = "讨逆将军";
			break;
		case 7:
			title = "杨武将军";
			break;
		case 8:
			title = "奋威将军";
			break;
		case 9:
			title = "车骑将军";
			break;
		case 10:
			title = "大将军";
			break;
		case 11:
			title = "大提督";
			break;
		case 12:
			title = "不败元帅";
			break;
		case 13:
			title = "诸侯皇者";
			break;
		}

		return title;

	}

	public static void main(String[] args) {

		List<Integer> list = new ArrayList<Integer>();
		// setAttackAbleList(list, 1, true);
		// list = new ArrayList<Integer>();
		// setAttackAbleList(list, 1, false);
		//
		// list = new ArrayList<Integer>();
		// setAttackAbleList(list, 3000, false);
		//
		// list = new ArrayList<Integer>();
		// setAttackAbleList(list, 600, true);
		//
		// list = new ArrayList<Integer>();
		// setAttackAbleList(list, 95, true);

		list = new ArrayList<Integer>();
		setAttackAbleList(list, 83);
	}
}
