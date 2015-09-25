package com.lodogame.ldsg.constants;

import com.lodogame.ldsg.exception.ServiceException;

public class MessageType {

	/**
	 * 获取装备消息
	 */
	public final static int MESSAGE_TYPE_GAIN_EQUIP = 101;

	/**
	 * 获取武将消息
	 */
	public final static int MESSAGE_TYPE_GAIN_HERO = 102;

	/**
	 * 争霸赛打赢消息
	 */
	public final static int MESSAGE_TYPE_PK_WIN = 103;

	/**
	 * 系统消息
	 */
	public final static int MESSAGE_TYPE_SYSTEM_MSG = 104;

	/**
	 * 顶级玩家登录消息
	 */
	public final static int MESSAGE_TYPE_TOP_USER_LOGIN = 105;
	/**
	 * 获取珍惜道具消息
	 */
	public final static int MESSAGE_TYPE_GAIN_TOOL = 106;

	/**
	 * 排行榜玩家登录消息
	 */
	public final static int MESSAGE_TYPE_RANK_USER_LOGIN = 107;

	/**
	 * VIP等级消息
	 */
	public final static int MESSAGE_TYPE_VIP_LEVEL = 108;

	/**
	 * 关卡消息
	 */
	public final static int MESSAGE_TYPE_SCENE = 109;

	/**
	 * 百人斩消息
	 */
	public final static int MESSAGE_TYPE_ARENA_RANK = 110;

	/**
	 * 武将转生消息
	 */
	public final static int MESSAGE_TYPE_HERO_TOWER = 111;

	/**
	 * 争霸称号玩家上线消息
	 */
	public final static int MESSAGE_TYPE_PK_USER_LOGIN = 112;

	/**
	 * 争霸称号玩家下线消息
	 */
	public final static int MESSAGE_TYPE_PK_USER_LOGOUT = 113;

	/**
	 * 争霸玩家连胜消息
	 */
	public final static int MESSAGE_TYPE_PK_GO_WIN = 114;

	/**
	 * 争霸玩家连胜50以上消息
	 */
	public final static int MESSAGE_TYPE_PK_GO_WIN_FIFTY = 115;

	/**
	 * 争霸玩家获得称号消息
	 */
	public final static int MEESSAGE_TYPE_PK_GET_TITLE = 116;

	/**
	 * 争霸获得总排名第一名
	 */
	public final static int MESSAGE_TYPE_PK_ONE = 117;

	/**
	 * 争霸获得组排名第一名
	 */
	public final static int MESSAGE_TYPE_PK_GROUP_ONE = 118;

	/**
	 * 国战占领皇城
	 */
	public final static int MESSAGE_TYPE_WAR_MAJOR_CITY = 119;

	/**
	 * 防守胜利10次
	 */
	public final static int MESSAGE_TYPE_WAR_DEFENSE_CITY = 200;

	/**
	 * 新春活 动
	 */
	public final static int MESSAGE_TYPE_OPEN_GIFT_BOX = 201;

	/**
	 * 击杀榜第一名
	 */
	public final static int MESSAGE_TYPE_ATTACK_RANK_CREATE = 202;

	/**
	 * 击杀榜第一名
	 */
	public final static int MESSAGE_TYPE_HORN_MSG = 203;
	
	/**
	 * 神器锻造
	 */
	public final static int DEIFY_FROGE_TYPE_MSG = 204;

	public static int getMessageType(String messageType) {

		if (messageType.equals("MESSAGE_TYPE_GAIN_EQUIP")) {
			return 101;
		} else if (messageType.equals("MESSAGE_TYPE_GAIN_HERO")) {
			return 102;
		} else if (messageType.equals("MESSAGE_TYPE_PK_WIN")) {
			return 103;
		} else if (messageType.equals("MESSAGE_TYPE_SYSTEM_MSG")) {
			return 104;
		} else if (messageType.equals("MESSAGE_TYPE_TOP_USER_LOGIN")) {
			return 105;
		} else if (messageType.equals("MESSAGE_TYPE_GAIN_TOOL")) {
			return 106;
		} else if (messageType.equals("MESSAGE_TYPE_RANK_USER_LOGIN")) {
			return 107;
		} else if (messageType.equals("MESSAGE_TYPE_VIP_LEVEL")) {
			return 108;
		} else if (messageType.equals("MESSAGE_TYPE_SCENE")) {
			return 109;
		} else if (messageType.equals("MESSAGE_TYPE_ARENA_RANK")) {
			return 110;
		} else if (messageType.equals("MESSAGE_TYPE_HERO_TOWER")) {
			return 111;
		} else if (messageType.equals("MESSAGE_TYPE_PK_USER_LOGIN")) {
			return 112;
		} else if (messageType.equals("MESSAGE_TYPE_PK_USER_LOGOUT")) {
			return 113;
		} else if (messageType.equals("MESSAGE_TYPE_PK_GO_WIN")) {
			return 114;
		} else if (messageType.equals("MESSAGE_TYPE_PK_GO_WIN_FIFTY")) {
			return 115;
		} else if (messageType.equals("MESSAGE_TYPE_PK_GET_TITLE")) {
			return 116;
		} else if (messageType.equals("MESSAGE_TYPE_PK_ONE")) {
			return 117;
		} else if (messageType.equals("MESSAGE_TYPE_PK_GROUP_ONE")) {
			return 118;
		} else if (messageType.equals("MESSAGE_TYPE_WAR_MAJOR_CITY")) {
			return 119;
		} else if (messageType.equals("MESSAGE_TYPE_WAR_DEFENSE_CITY")) {
			return 200;
		} else if (messageType.equals("MESSAGE_TYPE_OPEN_GIFT_BOX")) {
			return 201;
		} else if (messageType.equals("MESSAGE_TYPE_ATTACK_RANK_CREATE")) {
			return 202;
		} else if (messageType.equals("MESSAGE_TYPE_HORN_MSG")) {
			return 203;
		} else if (messageType.equals("DEIFY_FROGE_TYPE_MSG")) {
			return 204;
		} else {
			throw new ServiceException(ServiceReturnCode.NO_MESSAGE_TYPE_FOUND, "没有这个 message 类型 messageType[" + messageType + "]");
		}
	}

}
