package com.lodogame.ldsg.constants;

import com.lodogame.ldsg.config.Config;

/**
 * 初始化定义
 * 
 * @author jacky
 * 
 */
public class InitDefine {

	/**
	 * 装备背包上限
	 */
	public final static int EQUIP_BAG_INIT = 20;

	/**
	 * 武将背包上限
	 */
	public final static int HERO_BAG_INIT = 20;

	/**
	 * 体力上限
	 */
	public final static int POWER_INIT = 100;

	/**
	 * 体力恢复时间(毫秒)
	 */
	public final static long POWER_ADD_INTERVAL = 7 * 60 * 1000;

	/**
	 * 用户初始化等级
	 */
	public final static int INIT_USER_LEVEL = 1;

	/**
	 * 用户第一个武将的布阵
	 */
	public final static int INIT_USER_HERO_POS = 3;

	/**
	 * 初始化第1个武将ID
	 */
	public final static int INIT_USER_HERO_ID1 = 880;

	/**
	 * 初始化第2个武将ID
	 */
	public final static int INIT_USER_HERO_ID2 = 886;

	/**
	 * 初始用户银币
	 */
	public static int INIT_USER_COPPER = 1000;

	/**
	 * 用户初始金币
	 */
	public static int INIT_USER_GOLD = 50;

	/**
	 * 用户初始体力
	 */
	public final static int INIT_USER_POWER = 100;

	/**
	 * 用户初始经验
	 */
	public final static int INIT_USER_EXP = 0;

	/**
	 * 用户初始军令
	 */
	public final static int INIT_USER_ORDER = 30;

	/**
	 * 初始步骤
	 */
	public final static int INIT_GUIDE_STEP = 0;
	
	/**
	 * 禁言时间
	 */
	public final static long BANNED_TO_POST_TIME_INTERVAL = 30 * 60 * 1000;

	static {
		INIT_USER_GOLD = Config.ins().getInitGold();
		INIT_USER_COPPER = Config.ins().getInitCopper();
	}

}
