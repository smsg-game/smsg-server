package com.lodogame.ldsg.constants;

public class ConfigKey {

	/**
	 * 每日获得金币上限
	 */
	public final static String USER_DAILY_GAIN_GOLD_LIMIT = "daily_gain_gold_limit";

	/**
	 * 用户参加争霸的等级限制
	 */
	public final static String USER_PK_NEED_LIMIT = "user_pk_need_level";

	/**
	 * 重置副本需要花费的金币
	 */
	public final static String RESET_FORCES_TIMES_NEED_MONEY = "reset_forces_times_need_money";

	/**
	 * 酒馆修正配置
	 */
	public final static String TAVERN_AMEND_TIMES_1 = "tavern_amend_times_1";

	/**
	 * 酒馆修正配置
	 */
	public final static String TAVERN_AMEND_TIMES_2 = "tavern_amend_times_2";

	/**
	 * 酒馆修正配置
	 */
	public final static String TAVERN_AMEND_TIMES_3 = "tavern_amend_times_3";

	/**
	 * 重置宝塔金币
	 */
	public static final String RESET_TOWER_GOLD = "reset_tower_gold";

	// ----- BOSS[START]
	// -----------------------------------------------------------------------------

	/**
	 * 重置封魔冷却时间
	 */
	public static final String RESET_BOSS_COOLDOWN_PER_MIMUTE_NEED_GOLD = "reset_boss_cooldown_need_gold";

	/**
	 * 重置封魔冷却时间
	 */
	public static final String RESET_BOSS_COOLDOWN = "reset_boss_cooldown";

	/**
	 * 魔怪存在时间，用于清理魔怪定时任务，即在等待该时间长度后，清除已存在的魔怪信息
	 */
	public static final String B0SS_EXIST_TIME = "boss_exist_time";

	/**
	 * 魔怪刷新时间
	 */
	public static final String BOSS_REFRESH_CYCLE_TIME = "boss_refresh_cycle_time";

	/**
	 * 魔怪状态清除时间，即在该时间长度后，清楚掉魔怪的"已出现"状态
	 */
	public static final String BOSS_APPEAR_STATE_CLEAN_CYCLE_TIME = "boss_appear_state_clean_cycle_time";

	/**
	 * 总是有几率去刷新魔怪
	 */
	public static final String BOSS_ALWAYS_LUCKY = "boss_always_lucky";

	/**
	 * 魔怪刷新开始时间
	 */
	public static final String BOSS_REFRESH_START_TIME = "boss_refresh_start_time";

	/**
	 * 魔怪刷新结束时间
	 */
	public static final String BOSS_REFRESH_END_TIME = "boss_refresh_end_time";

	/**
	 * Boss 每天的开始时间
	 */
	public static final String BOSS_START_TIME = "boss_start_time";
	
	/**
	 * 幸运珠数目
	 */
	public static final String DRAW_REDUCE_TOOL = "draw_reduce_tool";

	// ----- BOSS[END]
	// -----------------------------------------------------------------------------

	/**
	 * 是否开启ACK
	 */
	public static final String OPEN_ACK = "open_ack";
	/**
	 * 用户最大等级
	 */
	public static final String USER_MAX_LEVEL = "user_max_level";

	/**
	 * 老玩家召回邮件标题
	 */
	public static final String CALLBACK_MAIL_TITLE = "callback_mail_title";

	/**
	 * 老玩家召回邮件内容
	 */
	public static final String CALLBACK_MAIL_CONTENT = "callback_mail_content";
}
