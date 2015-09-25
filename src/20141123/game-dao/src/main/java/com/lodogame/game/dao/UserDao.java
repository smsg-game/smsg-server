package com.lodogame.game.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.lodogame.model.User;
import com.lodogame.model.UserFaction;
import com.lodogame.model.UserShareLog;

public interface UserDao {

	/**
	 * 添加用户对象
	 * 
	 * @param user
	 * @return
	 */
	public boolean add(User user);

	/**
	 * 获取用户
	 * 
	 * @param userId
	 * @return
	 */
	public User get(String userId);

	/**
	 * 根据playerId获取用户
	 * 
	 * @param playerId
	 * @return
	 */
	public User getByPlayerId(String playerId);

	/**
	 * 
	 * @param username
	 * @return
	 */
	public User getByName(String username);

	/**
	 * 增加银币
	 * 
	 * @param userId
	 * @param cooper
	 * @return
	 */
	public boolean addCopper(String userId, int copper);

	/**
	 * 减少银币
	 * 
	 * @param userId
	 * @param copper
	 * @return
	 */
	public boolean reduceCopper(String userId, int copper);

	/**
	 * 增加金币
	 * 
	 * @param userId
	 * @param gold
	 * @return
	 */
	public boolean addGold(String userId, int gold);

	/**
	 * 花费金币
	 * 
	 * @param userId
	 * @param gold
	 * @return
	 */
	public boolean reduceGold(String userId, int gold);

	/**
	 * 增加经验
	 * 
	 * @param userId
	 * @param exp
	 * @param level
	 * @param resumePower
	 *            是否恢复体力
	 * @return
	 */
	public boolean addExp(String userId, int exp, int level, int power);

	/**
	 * 增加体力
	 * 
	 * @param userId
	 * @param power
	 * @param powerAddTime
	 * @return
	 */
	public boolean addPower(String userId, int power, int maxPower, Date powerAddTime);

	/**
	 * 重置增加体力的时间
	 * 
	 * @param userId
	 * @param powerAddTime
	 * @return
	 */
	public boolean resetPowerAddTime(String userId, Date powerAddTime);

	/**
	 * 花费体力
	 * 
	 * @param userId
	 * @param power
	 * @return
	 */
	public boolean reducePowre(String userId, int power);

	/**
	 * 获取在线用户列表
	 * 
	 * @return
	 */
	public Set<String> getOnlineUserIdList();

	/**
	 * 设置在线，不在线
	 * 
	 * @param userId
	 * @param online
	 * @return
	 */
	public boolean setOnline(String userId, boolean online);

	/**
	 * 清除用户所有缓存数据
	 * 
	 * @param userId
	 * @return
	 */
	public boolean cleanCacheData(String userId);

	/**
	 * 更改VIP等级
	 * 
	 * @param userId
	 * @param VIPLevel
	 * @param vipExpiredTime
	 * @return
	 */
	public boolean updateVIPLevel(String userId, int VIPLevel, Date vipExpiredTime);

	/**
	 * 根据用户等级倒序获得用户列表
	 * 
	 * @param offset
	 * @param size
	 * @return
	 */
	public List<User> listOrderByLevelDesc(int offset, int size);

	/**
	 * 根据银币数量倒序获得用户列表
	 * 
	 * @param offset
	 * @param size
	 * @return
	 */
	public List<User> listOrderByCopperDesc(int offset, int size);

	/**
	 * 获取所有用户的id 用户后台管理全服发放武平
	 * 
	 * @return
	 */
	public List<String> getAllUserIds();

	/**
	 * 判断用户是否在线
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isOnline(String userId);

	/**
	 * 减经验
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public boolean reduceExp(String userId, int amount);

	/**
	 * 封用户
	 * 
	 * @param userId
	 * @param dueTime
	 * @return
	 */
	public boolean banUser(String userId, Date dueTime);

	/**
	 * 消耗声望
	 * 
	 * @param userId
	 * @param reputation
	 * @return
	 */
	public boolean reduceReputation(String userId, int reputation);

	/**
	 * 添加声望
	 * 
	 * @param userId
	 * @param reputation
	 * @return
	 */
	public boolean addReputation(String userId, int reputation);

	/**
	 * 用户分享的日志
	 * 
	 * @param userId
	 * @return
	 */
	public UserShareLog getUserLastShareLog(String userId);

	/**
	 * 增加用户分享的日志
	 * 
	 * @param userShareLog
	 * @return
	 */
	public boolean addUserShareLog(UserShareLog userShareLog);

	/**
	 * 更改用户等级
	 * 
	 * @param userId
	 * @param level
	 * @param exp
	 * @return
	 */
	public boolean updateUserLevel(String userId, int level, int exp);
	
	/**
	 * 消耗神魄
	 * 
	 * @param userId
	 * @param mind
	 * @return
	 */
	public boolean reduceMind(String userId, int mind);

	/**
	 * 添加神魄
	 * 
	 * @param userId
	 * @param mind
	 * @return
	 */
	public boolean addMind(String userId, int mind);
	
	/**
	 *  禁言
	 *  
	 *  @param userId
	 */
	public boolean bannedToPost(String userId);
	
	/**
	 *  更新用户帮派信息
	 *  
	 *  @param userId
	 *  @param factionId
	 */
	public boolean updateUserFactionId(String userId, int factionId);
	
	/**
	 * 更新用户争霸积分
	 * @param userId
	 * @param pkScore
	 * @return
	 */
	public boolean addPkScore(String userId,int pkScore) ;
	
	/**
	 * 消耗争霸积分
	 * @param userId
	 * @param pkScore
	 * @return
	 */
	public boolean reducePkScore(String userId,int pkScore);
	
	/**
	 * 解散帮派，踢出所有帮派成员
	 * @param factionId
	 * @return
	 */
	public boolean updateAllFactionByFactionId(int factionId,List<UserFaction> list);

}
