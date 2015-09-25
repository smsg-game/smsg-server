package com.lodogame.ldsg.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.TavernConstant;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.model.TavernAmendDropTool;
import com.lodogame.model.TavernDropTool;

/**
 * 酒馆帮助类
 * 
 * @author jacky
 * 
 */

public class TavernHelper {

	private static final Logger logger = Logger.getLogger(TavernHelper.class);

	/**
	 * 抽奖
	 * 
	 * @param list
	 *            正常掉落列表
	 * @param amendList
	 *            修正掉落列表
	 * @param times
	 *            抽奖次数
	 * @param ind
	 *            当前游票
	 * @param amend
	 *            是否需要修正
	 * @param isLogExist
	 *            是否第一次大摆宴席
	 * @param isFirstUsedMoney
	 *            是否第一次使用元宝大摆筵席。或是第一次使用元宝千金一掷
	 * @return
	 */
	public static List<TavernDropTool> draw(List<TavernDropTool> list, List<TavernAmendDropTool> amendList, int times, long ind, boolean amend, boolean isLogExist, int isFirstUsedMoney) {

		// 循环处理
		while (ind >= list.size()) {
			ind = ind - list.size();
		}

		int end = (int) ind;
		int start = (int) (ind - times);

		List<TavernDropTool> tavernDropToolList = new ArrayList<TavernDropTool>();
		if (start >= 0) {
			List<TavernDropTool> temp  = list.subList(start, end);
			tavernDropToolList.addAll(temp);
		} else {
			List<TavernDropTool>  temp = list.subList(list.size() + start - 1, list.size() - 1);
			tavernDropToolList.addAll(temp);
			temp = list.subList(0, end);
			tavernDropToolList.addAll(temp);
		}

		if (amend) {// 做修正
			TavernDropTool tavernDropTool = null;
			int randVal = 1 + new Random().nextInt(10000);
			for (TavernAmendDropTool t : amendList) {
				if (t.getLowerNum() <= randVal && randVal <= t.getUpperNum()) {
					tavernDropTool = new TavernDropTool();
					tavernDropTool.setSystemHeroId(t.getSystemHeroId());
					break;
				}
			}

			if (tavernDropTool == null) {
				logger.error("修正异常，获取不到修正掉落");
			} else {
				tavernDropToolList.set(RandomUtils.nextInt(tavernDropToolList.size()), tavernDropTool);
			}

		} else {
			if (isLogExist == false) {
				// 随机把一个替换成4星武将
				tavernDropToolList.set(RandomUtils.nextInt(tavernDropToolList.size()), randomStarDropTool(list, 4));
			}

			if (isFirstUsedMoney == 1) {
				tavernDropToolList.set(RandomUtils.nextInt(tavernDropToolList.size()), randomStarDropTool(list, 4));
			} else if (isFirstUsedMoney == 2) {
				tavernDropToolList.set(RandomUtils.nextInt(tavernDropToolList.size()), randomStarDropTool(list, 5));
			}
		}

		return tavernDropToolList;
	}

	private static TavernDropTool randomStarDropTool(List<TavernDropTool> list, int star) {

		List<TavernDropTool> tavernDropToolList = new ArrayList<TavernDropTool>();

		for (TavernDropTool tavernDropTool : list) {

			if (tavernDropTool.getSystemHeroId() <= 4) {// 1-4这四个武将不能出
				continue;
			}

			if (tavernDropTool.getStar() == star) {
				tavernDropToolList.add(tavernDropTool);
			}
		}

		return tavernDropToolList.get(RandomUtils.nextInt(tavernDropToolList.size()));
	}

	public static int getCostCopper(int type, int times) {

		if (type == TavernConstant.DRAW_TYPE_1) {
			if (times == 1) {
				return 1000;
			} else {
				return 9000;
			}
		}

		throw new ServiceException(ServiceReturnCode.FAILD, "未知的抽奖类型[" + type + "]");
	}

	public static int getCostMoney(int type, int times) {

		if (type == TavernConstant.DRAW_TYPE_2) {
			if (times == 1) {
				return 10;
			} else {
				return 90;
			}
		} else if (type == TavernConstant.DRAW_TYPE_3) {
			if (times == 1) {
				return 50;
			} else {
				return 430;
			}
		}

		throw new ServiceException(ServiceReturnCode.FAILD, "未知的抽奖类型[" + type + "]");
	}

	/**
	 * 获取冷却时间周期
	 * 
	 * @param type
	 * @return
	 */
	public static long getCoolTimeInterval(int type) {
		switch (type) {
		case TavernConstant.DRAW_TYPE_1:
			return TavernConstant.DRAW_TYPE_1_CD_TIME;
		case TavernConstant.DRAW_TYPE_2:
			return TavernConstant.DRAW_TYPE_2_CD_TIME;
		case TavernConstant.DRAW_TYPE_3:
			return TavernConstant.DRAW_TYPE_3_CD_TIME;
		}

		return 0;
	}

	/**
	 * 根据类型获得描述
	 * 
	 * @param type
	 * @return
	 */
	public static String getTavernDesc(int type) {
		switch (type) {
		case TavernConstant.DRAW_TYPE_1:
			return "市井偶遇：每半小时进行一次招募";
		case TavernConstant.DRAW_TYPE_2:
			return "广交豪杰：每天可进行一次招募";
		case TavernConstant.DRAW_TYPE_3:
			return "大摆筵席：每三天可进行一次招募";
		}

		return "广交豪杰";
	}

	/**
	 * 获取每天次数限制
	 * 
	 * @param type
	 * @return
	 */
	public static int getTimeLimit(int type) {

		switch (type) {
		case 0:
			return 10;
		case 1:
			return 1;
		case 2:
			return 1;
		}

		return 0;
	}
}
