package com.lodogame.ldsg.constants;

/**
 * 擂台赛中用到的常量
 * @author chengevo
 *
 */
public class ContestConstant {

	public final static String START_TIME_ROUND_128 = "2 11:30";
	public final static String START_TIME_ROUND_64 = "2 11:32";
	public final static String START_TIME_ROUND_32 = "4 11:30";
	public final static String START_TIME_ROUND_16 = "4 11:32";
	public static final String START_TIME_ROUND_BET = "6 11:30";
	public final static String START_TIME_ROUND_8 = "6 11:40";
	public final static String START_TIME_ROUND_4 = "6 11:42";
	public final static String START_TIME_ROUND_2 = "6 11:44";
	public final static String START_TIME_ROUND_1 = "6 11:46";
	
	/*
	 * 进入各个界面的时间
	 */
	public final static String REG_START_TIME = "1 00:00";
	public final static String REG_END_TIME = "1 24:00";
	
	public final static String VERSUS_1_START_TIME = "2 00:00";
	public final static String VERSUS_1_END_TIME = "2 12:02";
	public final static String VERSUS_2_START_TIME = "4 00:00";
	public final static String VERSUS_2_END_TIME = "4 12:02";
	public final static String VERSUS_3_START_TIME = "6 00:00";
	public final static String VERSUS_3_END_TIME = "6 12:16";
	
	public final static String REC_REWARD_1_START_TIME = "2 12:02";
	public final static String REC_REWARD_1_END_TIME = "3 24:00";
	public final static String REC_REWARD_2_START_TIME = "4 12:02";
	public final static String REC_REWARD_2_END_TIME = "5 24:00";
	public final static String REC_REWARD_3_START_TIME = "6 12:16";
	public final static String REC_REWARD_3_END_TIME = "7 24:00";
	
	
	/* 各轮比赛对应的编号, 客户端根据这个编号决定显示哪轮比赛的界面 */
	
	public static final int ROUND_128 = 0; 
	public static final int ROUND_64 = 1; 
	public static final int ROUND_32 = 2; 
	public static final int ROUND_16 = 3; 
	public static final int ROUND_BET = 4; 
	public static final int ROUND_8 = 5; 
	public static final int ROUND_4 = 6; 
	public static final int ROUND_2 = 7; 
	public static final int ROUND_1 = 8; 
	
	public static final int[] ROUNDS = { 
		ContestConstant.ROUND_128,ContestConstant.ROUND_64, ContestConstant.ROUND_32,
		ContestConstant.ROUND_16, ContestConstant.ROUND_BET, ContestConstant.ROUND_8, ContestConstant.ROUND_4, 
		ContestConstant.ROUND_2, ContestConstant.ROUND_1
	};
	
	public static final String[] START_TIME_OF_EACH_ROUND = {
			START_TIME_ROUND_128, START_TIME_ROUND_64, START_TIME_ROUND_32,
			START_TIME_ROUND_16, START_TIME_ROUND_BET, START_TIME_ROUND_8,
			START_TIME_ROUND_4, START_TIME_ROUND_2, START_TIME_ROUND_1 
	};
	
	
	/**
	 * 玩家报名时可以选择的小组
	 */
	public static final  String[] REG_TEAM_NAMES = { "青龙", "白虎", "朱雀", "玄武"};
	
	/**
	 * 四个报名小组的 id
	 */
	public static final int[] REG_TEAM_IDS = {12801, 12802, 12803, 12804};
	
	/**
	 * 玩家报名时有多少支小组可以选择
	 */
	public static final int REG_TEAM_NUM = 4;
	/**
	 * 玩家报名时，每组中可以有多少个玩家
	 */
	public static final int REG_TEAM_CAPACITY = 64;
	
	/**
	 * 这个擂台赛包括下注在内一共要进行多少场比赛
	 */
	public static final int ROUND_NUM = 9;
	
	/**
	 * 进攻方获胜
	 */
	public static final int ATTACK_USER_WIN = 1;
	
	/**
	 * 防守方获胜
	 */
	public static final int DEFENSE_USER_WIN = 2;
	
	/**
	 * 第一届擂台赛开始时间
	 */
	public static final String FIRST_CONTEST_TIME = "2014/04/21 00:00:00";
	
	/**
	 * 下注所需要的金币
	 */
	public static final int COPPER_NEED_TO_BET = 880000;
	
}
