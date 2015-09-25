package com.lodogame.ldsg.service;

import java.util.Date;
import java.util.List;

import com.lodogame.ldsg.bo.ActivityDrawToolBO;
import com.lodogame.model.PaymentLog;

/**
 * 异步存储日志service
 * 
 * @author foxwang
 * 
 */
public interface UnSynLogService {
	/**
	 * 银币日志
	 * 
	 * @param userId
	 *            用户ID
	 * @param useType
	 *            使用类型,见(CopperUserType)
	 * @param amount
	 *            数量
	 * @param flg
	 *            1 增加 -1 减少
	 * @param success
	 *            是否成功 1 成功 0 失败
	 */
	public void copperLog(String userId, int useType, int amount, int flg, boolean success);

	/**
	 * 金币日志
	 * 
	 * @param userId
	 * @param useType
	 * @param amount
	 * @param flag
	 * @param success
	 */
	public void goldLog(String userId, int userLevel, int useType, int amount, int flag, boolean success);

	/**
	 * 道具日志
	 * 
	 * @param userId
	 * @param toolType
	 * @param toolId
	 * @param num
	 * @param useType
	 * @param success
	 */
	public void toolLog(String userId, int toolType, int toolId, int num, int useType, int flag, String extinfo, boolean success);

	/**
	 * 武将日志
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param systemHeroId
	 * @param useType
	 * @param heroExp
	 * @param heroLevel
	 */
	public void heroLog(String userId, String userHeroId, int systemHeroId, int useType, int falg, int heroExp, int heroLevel);

	/**
	 * 用户升级日志
	 * 
	 * @param userId
	 * @param exp
	 * @param level
	 */
	public void levelUpLog(String userId, int exp, int level);

	/**
	 * 用户从平台导入用户表
	 * 
	 * @param userId
	 *            用户编号
	 * @param userName
	 *            平台用户名
	 * @param regTime
	 *            注册时间
	 */
	public void userRegLog(String userId, String userName, String parteneId, String qn, Date regTime);

	/**
	 * 用户创建角色日志
	 * 
	 * @param userId
	 * @param userName
	 *            用户平台用户名
	 * @param name
	 *            用户游戏内角色用户名
	 * @param sex
	 *            用户性别
	 * @param regTime
	 *            注册时间
	 */
	public void userCreatRoleLog(String userId, String userName, String name, int sex, Date regTime, long loadid);

	/**
	 * 用户登录日志
	 * 
	 * @param userId
	 * @param level
	 * @param ip
	 * @param loginTime
	 */
	public void userLoginLog(String userId, int level, String ip, Date loginTime);

	/**
	 * 用户登出日志记录
	 * 
	 * @param userId
	 *            用户id
	 * @param ip
	 *            ip地址
	 * @param loginOutTime
	 *            登出时间
	 * @param liveMinutes
	 *            用户在线的分钟数
	 */
	public void userLogOutLog(String userId, String ip, Date loginOutTime, int liveMinutes);

	/**
	 * 用户升级日志表
	 * 
	 * @param levelTime
	 *            升级时间
	 * @param userId
	 *            用户id
	 * @param level
	 *            升到的级别
	 */
	public void userLevelUpLog(Date levelTime, String userId, int level);

	/**
	 * 用户充值日志记录
	 * 
	 * @param userId
	 *            用户id
	 * @param userLevel
	 *            用户等级
	 * @param orderId
	 *            订单号（游戏内）
	 * @param orderNum
	 *            订单号（平台内）
	 * @param amount
	 *            充值数量
	 * @param payTime
	 *            充值时间
	 */
	public void userPayLog(PaymentLog paymentLog, int userLevel);

	/**
	 * 用户装备操作日志
	 * 
	 * @param userId
	 * @param userEquipMentId
	 * @param equipMentId
	 * @param userHeroId
	 * @param equipMentLevel
	 * @param type
	 * @param operatorTime
	 */
	public void userEquipMentOperatorLog(String userId, String userEquipMentId, int equipMentId, String userHeroId, int equipMentLevel, int type, Date operatorTime);

	/**
	 * 用户英雄操作日志表
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param heroId
	 * @param type
	 * @param exp
	 *            英雄当前经验
	 * @param level
	 * @param pos
	 * @param expNum
	 *            英雄增加的经验
	 * @param opratorTime
	 * @param heroName
	 */
	public void userHeroOperatorLog(String userId, String userHeroId, int heroId, int type, long exp, int level, int pos, int expNum, Date opratorTime, String heroName);

	/**
	 * 金币操作日志
	 * 
	 * @param userId
	 * @param userLevel
	 * @param getOrUse
	 *            1获得 2使用
	 * @param type
	 *            操作类型
	 * @param changeNum
	 *            改变的数量
	 * @param opratorTime
	 *            操作时间
	 */
	public void userGoldLog(String userId, int userLevel, int getOrUse, int type, int changeNum, Date opratorTime);

	/**
	 * 银币操作日志
	 * 
	 * @param userId
	 * @param type
	 *            操作类型
	 * @param money
	 *            正数为获取 负数为消费
	 * @param opratorTime
	 */
	public void userCopperLog(String userId, String type, int money, Date opratorTime);

	/**
	 * 道具日志
	 * 
	 * @param userId
	 * @param treasureId
	 * @param treasureType
	 * @param getOrUse
	 *            类别（1获得 2使用）
	 * @param oprationType
	 *            操作类型
	 * @param changeNum
	 * @param extInfo
	 * @param oprationTime
	 */
	public void userTreasureLog(String userId, int treasureId, int treasureType, int getOrUse, String oprationType, int changeNum, String extInfo, Date oprationTime);

	/**
	 * 玩家在线日志
	 * 
	 * @param oprationTime
	 *            时间
	 * @param userAmount
	 *            在线用户数量
	 * @param userIdStr
	 *            在线用户玩家id
	 */
	public void userOnlineLog(Date oprationTime, int userAmount, String userIdStr);

	/**
	 * 用户战斗日志
	 * 
	 * @param userId
	 * @param targertId
	 * @param type
	 * @param flag
	 *            1赢了 其他为输了
	 * @param report
	 * @param creatTime
	 */
	public void userBattleLog(String userId, int userLevel, String targetId, int type, int flag, Date creatTime);

	/**
	 * 用户vip升级日志
	 * 
	 * @param userId
	 * @param vipLevel
	 * @param creatTime
	 */
	public void userVipLevelLog(String userId, int vipLevel, Date creatTime);

	/**
	 * 用户商城日志
	 * 
	 * @param userId
	 * @param userLevel
	 * @param toolId
	 * @param buyNum
	 * @param cost
	 * @param creatTime
	 */
	public void userMallLog(String userId, int userLevel, int toolId, int buyNum, int cost, int costCopperNum, Date creatTime);

	/**
	 * 用户活跃打点统计日志
	 * 
	 * @param userId
	 * @param userLevel
	 * @param creatTime
	 * @param ip
	 * @param actionId
	 */
	public void userActionLog(String userId, int userLevel, Date creatTime, String ip, int actionId);

	/**
	 * 用户感恩节抽奖日志
	 * 
	 * @param drawToolBOList
	 * @param userId
	 * @param times
	 */
	public void userDrawLog(List<ActivityDrawToolBO> drawToolBOList, String userId, int times);
}
