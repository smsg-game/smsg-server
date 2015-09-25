package com.lodogame.ldsg.constants;

public class TavernConstant {

	/**
	 * 抽奖类型:广交豪杰
	 */
	public final static int DRAW_TYPE_1 = 0;

	/**
	 * 奖类型:大摆筵席
	 */
	public final static int DRAW_TYPE_2 = 1;

	/**
	 * 奖类型:千金一掷
	 */
	public final static int DRAW_TYPE_3 = 2;

	/**
	 * 抽奖类型:广交豪杰,每天限制次数
	 */
	public final static int DRAW_TYPE_1_DAY_LIMIT = 10;

	/**
	 * 抽奖类型:大摆筵席,每天限制次数
	 */
	public final static int DRAW_TYPE_2_DAY_LIMIT = 1;

	/**
	 * 抽奖类型:千金一掷,每天限制次数
	 */
	public final static int DRAW_TYPE_3_DAY_LIMIT = 1;

	/**
	 * 抽奖类型:广交豪杰,冷却时间(毫秒)
	 */
	public final static int DRAW_TYPE_1_CD_TIME = 60 * 30 * 1000;

	/**
	 * 抽奖类型:大摆筵席,冷却时间
	 */
	public final static int DRAW_TYPE_2_CD_TIME = 60 * 60 * 8 * 1000;

	/**
	 * 抽奖类型:千金一掷,冷却时间
	 */
	public final static int DRAW_TYPE_3_CD_TIME = 60 * 60 * 24 * 1000;

	/**
	 * 每日豪杰
	 */
	public final static int TAVERN_REBATE_1 = 1;
	
	/**
	 * 每日筵席
	 */
	public final static int TAVERN_REBATE_2 = 2;
	
	/**
	 * tips1
	 */
	public final static String DRAW_TYPE_TIPS_FIRST = "首次元宝广交豪杰必得4星将，首次元宝大摆筵席必得5星将";
	
	/**
	 * tips2
	 */
	public final static String DRAW_TYPE_TIPS_HERO = "再元宝招募N次【大摆筵席】，必得一张五星卡";

}
