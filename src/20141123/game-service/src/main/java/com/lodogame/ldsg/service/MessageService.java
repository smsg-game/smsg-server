package com.lodogame.ldsg.service;

public interface MessageService {

	/**
	 * 发小喇叭消息，没有足够的道具
	 */
	public static final int SEND_HORN_MSG_NOT_ENOUGH_TOOL = 2001;

	/**
	 * 发送获得竟技场第一名消息
	 * 
	 * @param userId
	 *            获得者用户ID
	 * @param username
	 *            获得者名称
	 */
	public void sendPkMsg(String userId, String username);

	/**
	 * 发送不小喇叭消息
	 * 
	 * @param userId
	 * @param content
	 */
	public void sendHornMsg(String userId, String content);

	/**
	 * 发送获得五星武将消息
	 * 
	 * @param userId
	 * @param username
	 * @param heroName
	 */
	public void sendGainHeroMsg(String userId, String username, String heroName, int star, String place);

	/**
	 * 发送获得珍惜道具消息
	 * 
	 * @param userId
	 * @param username
	 * @param toolName
	 * @param title
	 *            可以为空""
	 * @param place
	 */
	public void sendGainToolMsg(String userId, String username, String toolName, String place, String title);

	/**
	 * 发送获得五星装备消息
	 * 
	 * @param userId
	 * @param username
	 * @param equipName
	 */
	public void sendGainEquipMsg(String userId, String username, String equipName);

	/**
	 * 发送系统消息 *
	 * 
	 * @param content
	 * @param
	 */
	public void sendSystemMsg(String content, String partnerIds);

	/**
	 * 发送系统消息 *
	 * 
	 * @param content
	 * @param
	 */
	public void sendSystemMsg(String content);

	/**
	 * 
	 * @param title
	 * @param username
	 */
	public void sendTopUserLoginMsg(String userId, String title, String username);

	/**
	 * 排行榜玩家登录
	 * 
	 * @param userId
	 * @param title
	 * @param username
	 */
	public void sendRankUserLoginMsg(String userId, String title, String username);

	/**
	 * vip等级
	 * 
	 * @param userId
	 * @param userName
	 * @param level
	 */
	public void sendVipLevelMsg(String userId, String username, int level);

	/**
	 * 关卡消息
	 * 
	 * @param userId
	 * @param username
	 * @param scene
	 */
	public void sendSceneMsg(String userId, String username, String scene);

	/**
	 * 百人斩消息(前3名)
	 * 
	 * @param rank1
	 * @param username1
	 * @param rank2
	 * @param username2
	 * @param rank3
	 * @param username3
	 */
	public void sendArenaMsg(String rank1, String username1, String rank2, String username2, String rank3, String username3);

	/**
	 * 武将转生
	 * 
	 * @param userId
	 * @param username
	 * @param heroName
	 */
	public void sendHeroTowerMsg(String userId, String username, String heroName);

	/**
	 * 争霸称号玩家上线消息
	 * 
	 * @param title
	 *            称号
	 * @param username
	 */
	public void sendPkUserLogin(String title, String username);

	/**
	 * 争霸称号玩家下线消息
	 * 
	 * @param title
	 *            称号
	 * @param username
	 */
	public void sendPkUserLogout(String title, String username);

	/**
	 * 争霸获得总排名第一名
	 * 
	 * @param userId
	 * @param attack
	 *            获胜玩家名称
	 * @param defense
	 *            防守玩家名称
	 */
	public void sendPkTotalFirst(String userId, String attack, String defense);

	/**
	 * 争霸获得组排名第一名
	 * 
	 * @param userId
	 * @param attack
	 * @param defense
	 * @param group
	 */
	public void sendPkGroupFirst(String userId, String attack, String defense, String group);

	/**
	 * 争霸获得称号消息
	 * 
	 * @param userId
	 * @param username
	 * @param title
	 */
	public void sendPkGetTitle(String userId, String username, String title);

	/**
	 * 争霸玩家连胜消息
	 * 
	 * @param title
	 * @param username
	 */
	public void sendPkGoWin(String username, String title);

	/**
	 * 争霸玩家连胜50以上消息
	 * 
	 * @param title
	 * @param username
	 * @param times
	 */
	public void sendPkGoWinF(String title, String username, int times);

	/**
	 * 占领皇城
	 */
	public void sendWarMajorCity(String countryName, String username);

	/**
	 * 防御胜利10次
	 * 
	 * @param countryName
	 * @param username
	 * @param cityName
	 */
	public void sendWarDefense(String countryName, String username, String cityName);

	/**
	 * 击杀排行榜产生消息
	 * 
	 * @param username
	 */
	public void sendWarAttackRankCreate(String username);

	/**
	 * @param userId
	 * @param username
	 * @param title
	 * @param place
	 * @param toolName
	 */
	public void sendOpenGiftBoxMsg(String userId, String username, String title, String place, String toolName);
	
	/**
	 * 神器锻造
	 * @param username
	 * @param userHeroName
	 * @param equipName
	 */
	public void sendDeifyForgeMsg(String username, String userHeroName, String equipName);

}
