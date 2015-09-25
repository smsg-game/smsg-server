package com.ldsg.battle.constant;

public class ReportConstant {

	/**
	 * 当前没有行动者时的攻击行为表现战报
	 */
	public static String EMPTY_ATTACK_REPORT = "[[0,0,0],[0,0,0,0],";

	/**
	 * }
	 */
	public static String REPORT_BLOCK_END_TAG = "}";

	/**
	 * {
	 */
	public static String REPORT_BLOCK_START_TAG = "{";

	/**
	 * buff数据格式[BUFF武将位置,效果数值,效果,持续效果/移除,当前血量]
	 */
	public static String REPORT_BUFF_FORMAT = "[\"{0}\",{1,number,#},{2,number,#},{3,number,#},{4,number,#}]";

	/**
	 * ,
	 */
	public static String REPORT_COMMA_SPLIT_TAG = ",";

	/**
	 * 将领数据格式 [武将ID,武领名称,武将等级,武将图标名称,兵符名称,最大血量,血量,士气,技能,位置,攻击类型]
	 */
	public static String REPORT_HERO_FORMAT = "[{0,number,#},\"{1}\",{2,number,#},{3,number,#},{4,number,#},{5,number,#},{6,number,#},{7,number,#},{8,number,#},\"{9}\",{10,number,#},{11,number,#}]";

	/**
	 * |
	 */
	public static String REPORT_LINE_SPLIT_TAG = "|";

	/**
	 * ]
	 */
	public static String REPORT_LIST_END_TAG = "]";

	/**
	 * [
	 */
	public static String REPORT_LIST_START_TAG = "[";

	/**
	 * ;
	 */
	public static String REPORT_SEMICOLON_SPLIT_TAG = ";";

}
