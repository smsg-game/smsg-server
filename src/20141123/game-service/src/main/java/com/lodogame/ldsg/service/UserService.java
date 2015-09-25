package com.lodogame.ldsg.service;

import java.util.Date;
import java.util.List;

import com.lodogame.ldsg.bo.UserBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.model.User;
import com.lodogame.model.UserMapper;

public interface UserService {

	/**
	 * 金币不够
	 */
	public final static int RESUME_POWER_NOT_ENOUGH_GOLD = 2002;

	/**
	 * 购买武将背包，金币不够
	 */
	public final static int BUY_HERO_BAG_NOT_ENOUGH_GOLD = 2002;

	/**
	 * 购买武器背包，金币不够
	 */
	public final static int BUY_EQUIP_BAG_NOT_ENOUGH_GOLD = 2002;

	/**
	 * 购买VIP，金币不够
	 */
	public final static int BUY_VIP_NOT_ENOUGH_GOLD = 2002;

	/**
	 * 购买VIP的等级没有比现在更高
	 */
	public final static int BUY_VIP_LEVEL_NOT_ENOUGH = 2003;

	/**
	 * 体力已经到上限
	 */
	public final static int RESUME_POWER_POWER_IS_MAX = 2003;

	/**
	 * 超过次数限制
	 */
	public final static int RESUME_POWER_OVER_MAX_TIMES = 2004;

	/**
	 * 体力已经到上限
	 */
	public final static int RESUME_POWER_POWER_IS_BEYOND = 2005;

	/**
	 * 用户不存在
	 */
	public final static int LOAD_USER_NOT_EXIST = 2001;

	/**
	 * 用户未注册角色
	 */
	public final static int LOGIN_NO_REG = 2001;

	/**
	 * 用户已经被封号
	 */
	public final static int LOGIN_USER_BANNED = 2002;

	/**
	 * 用户未登陆
	 */
	public final static int LOGIN_NOT_LOGIN = 2002;
	/**
	 * 用户登陆数据不匹配
	 */
	public final static int LOGIN_DATA_NOT_MATCH = 2003;

	/**
	 * 用户已经存在
	 */
	public final static int CREATE_USER_NAME_EXIST = 2001;

	/**
	 * 用户已经存在
	 */
	public final static int CREATE_USER_EXIST = 2003;

	/**
	 * 用户名非法
	 */
	public final static int CREATE_USERNAME_INVAILD = 2002;

	/**
	 * 邀请注册 - 邀请码不存在
	 */
	public final static int CODE_NOT_EXIST = 2004;

	/**
	 * 邀请注册 - 邀请码验证失败
	 */
	public final static int CODE_VERIFY_FAILED = 2005;

	/**
	 * 用户不存在
	 */
	public final static int LOADINFO_USER_NOT_EXIST = 2001;

	/**
	 * 用户昵称重复
	 */
	public final static int NICKNAME_EXIST = 2004;

	/**
	 * 昵称长度非法
	 */
	public final static int NICKNAME_LENGTH_ILLEGAL = 2001;
	/**
	 * 昵称包含非法文字
	 */
	public final static int NICKNAME_WORD_ILLEGAL = 2002;
	/**
	 * 昵称为空
	 */
	public final static int NICKNAME_IS_NULL = 2003;

	/**
	 * 购买银币，金币不足
	 */
	public final static int BUY_COPPER_NOT_ENOUGH_GOLD = 2002;

	/**
	 * 每次购买，增加5个背包
	 */
	public final static int INCREASE_BAG_STEP = 5;

	/**
	 * 获取单个用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public User get(String userId);

	/**
	 * 根据playerId获取用户信息
	 * 
	 * @param playerId
	 * @return
	 */
	public User getByPlayerId(String playerId);

	/**
	 * 获取UserMapper信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserMapper getUserMapper(String userId);

	/**
	 * 获取单个用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserBO getUserBO(String userId);

	/**
	 * 创建角色
	 * 
	 * @param userId
	 *            用户ID
	 * @param systemHeroId
	 *            系统武将ID
	 * @param username
	 *            用户名
	 * @param handle
	 * @return
	 */
	public boolean create(String userId, int systemHeroId, String username, EventHandle handle);

	/**
	 * 邀请注册
	 * 
	 * @param uid
	 *            用户ID
	 * @param systemHeroId
	 *            系统武将 ID
	 * @param username
	 *            用户名
	 * @param code
	 *            邀请码
	 */
	public void invited(String uid, int systemHeroId, String username, int code);

	/**
	 * 要求注册 - 检测邀请码
	 * 
	 * @param code
	 */
	public void checkCode(int code);

	/**
	 * 增加银币
	 * 
	 * @param userId
	 * @param amount
	 * @param useType
	 * @return
	 */
	public boolean addCopper(String userId, int amount, int useType);

	/**
	 * 花费银币
	 * 
	 * @param userId
	 * @param amount
	 * @param useType
	 * @return
	 */
	public boolean reduceCopper(String userId, int amount, int useType);

	/**
	 * 增加金币
	 * 
	 * @param userId
	 * @param amount
	 * @param useType
	 * @return
	 */
	public boolean addGold(String userId, int amount, int useType, int userLevel);

	/**
	 * 使用金币
	 * 
	 * @param userId
	 * @param amount
	 * @param useType
	 * @return
	 */
	public boolean reduceGold(String userId, int amount, int useType, int userLevel);

	/**
	 * 增加经验
	 * 
	 * @param userId
	 * @param amount
	 * @param useType
	 * @return
	 */
	public boolean addExp(String userId, int amount, int useType);

	/**
	 * 增加体力
	 * 
	 * @param userId
	 * @param amount
	 * @param useType
	 * @param powerAddTime
	 * @return
	 */
	public boolean addPower(String userId, int amount, int useType, Date powerAddTime);

	/**
	 * 增加体力
	 * 
	 * @param userId
	 * @param amount
	 * @param useType
	 * @param powerAddTime
	 * @return
	 */
	public boolean addPower(String userId, int amount, int maxPower, int useType, Date powerAddTime);

	/**
	 * 使用体力
	 * 
	 * @param userId
	 * @param amount
	 * @param useType
	 * @return
	 */
	public boolean reducePower(String userId, int amount, int useType);

	/**
	 * 恢复体力
	 * 
	 * @param userId
	 * @param handle
	 * @return 返回已经购买的次数
	 */
	public int resumePower(String userId, EventHandle handle);

	/**
	 * 体力回复
	 * 
	 * @param user
	 * @return
	 */
	public boolean checkPowerAdd(User user);

	/**
	 * 用户登录
	 * 
	 * @param userId
	 */
	public void login(String userId, String userIp);

	/**
	 * 用户登出
	 * 
	 * @param userId
	 */
	public void logout(String userId);

	/**
	 * 购买银币
	 * 
	 * @param userId
	 * @return
	 */
	public boolean buyCopper(String userId);

	/**
	 * 推送用户信息
	 * 
	 * @param userId
	 */
	public void pushUser(String userId);

	/**
	 * 保存新手引导步骤
	 * 
	 * @param step
	 * @param ip
	 */
	public boolean saveGuideStep(String userId, int step, String ip);

	/**
	 * 按照等级由高到低获取用户数据
	 * 
	 * @param offset
	 * @param size
	 * @return
	 */
	public List<UserBO> listOrderByLevelDesc(int offset, int size);

	/**
	 * 根据银币数量排序获取用户列表
	 * 
	 * @param offset
	 * @param size
	 * @return
	 */
	public List<User> listOrderByCopperDesc(int offset, int size);

	/**
	 * 用于返还用户体力
	 * 
	 * @param userId
	 * @param power
	 * @param powerUseType
	 */
	public void returnPower(String userId, int power, int powerUseType);

	/**
	 * 判断用户是否在线
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isOnline(String userId);

	/**
	 * 判断用户武将背包
	 * 
	 * @param userId
	 */
	public void checkUserHeroBagLimit(String userId);

	/**
	 * 判断用户装备背包
	 * 
	 * @param userId
	 */
	public void checkUserEquipBagLimit(String userId);

	/**
	 * 检测掉落限制
	 * 
	 * @param userId
	 * @param goldNum
	 */
	public boolean checkUserGoldGainLimit(String userId, int goldNum);

	/**
	 * 昵称检测
	 * 
	 * @param username
	 */
	public void checkUsername(String username);

	/**
	 * 更新VIP等级
	 * 
	 * @param userId
	 */
	public void updateVipLevel(String userId);

	/**
	 * 增加用户武将背包
	 * 
	 * @param userId
	 * @param equipMax
	 * @return
	 */
	public boolean addUserHeroBag(String userId, int equipMax);

	/**
	 * 增加用户装备背包
	 * 
	 * @param userId
	 * @param equipMax
	 * @return
	 */
	public boolean addUserEquipBag(String userId, int equipMax);

	/**
	 * 根据传入的武将列表，计算战斗力
	 * 
	 * @param userHeros
	 * @return
	 */
	public int getUserPower(List<UserHeroBO> userHeros);

	/**
	 * 从数据库中获取所有用户id列表 用户后台全服发放
	 * 
	 * @return
	 */
	public List<String> getAllUserIds();

	/**
	 * 花费经验
	 * 
	 * @param userId
	 * @param amount
	 * @param userType
	 * @return
	 */
	public boolean reduceExp(String userId, int amount, int useType);

	/**
	 * 记录新手引导步骤
	 * 
	 * @param uid
	 * @param guideStep
	 * @param ip
	 * @return
	 */
	public boolean recordGuideStep(String userId, int guideStep, String ip);

	/**
	 * 封号
	 * 
	 * @param dueTime
	 *            封号到期时间
	 * @return
	 */
	public boolean banUser(String userId, String dueTime);

	/**
	 * 设置 VIP 等级
	 * 
	 * @param userId
	 * @param vipLevel
	 * @param force
	 *            是否强制更新
	 * @return
	 */
	public boolean assignVipLevel(String userId, int vipLevel, boolean force);

	/**
	 * 消耗威望
	 * 
	 * @param userId
	 * @param amount
	 * @param useType
	 * @return
	 */
	public boolean reduceReputation(String userId, int amount, int useType);

	/**
	 * 增加威望
	 * 
	 * @param userId
	 * @param amount
	 * @param useType
	 * @return
	 */
	public boolean addReputation(String userId, int amount, int useType);

	/**
	 * 用户分享游戏，获得奖励
	 * 
	 * @param userId
	 * @return
	 */
	public void getShareGift(String userId);

	/**
	 * 更改用户等级，经验(只能后台用)
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
	 * @param amount
	 * @param useType
	 * @return
	 */
	public boolean reduceMind(String userId, int amount, int useType);

	/**
	 * 增加神魄
	 * 
	 * @param userId
	 * @param amount
	 * @param useType
	 * @return
	 */
	public boolean addMind(String userId, int amount, int useType);
	
	/**
	 * 获取用户
	 * 
	 * @param username
	 */
	public User getUserByUserName(String username);
	
	/**
	 * 设置用户禁言
	 * 
	 * @param userId
	 * 
	 */
	public boolean bannedToPost(String userId);
	
	/**
	 * 更新用户争霸积分
	 * @param userId
	 * @param pkScore
	 * @param useType
	 * @return
	 */
	public boolean addPkScore(String userId,int pkScore,int useType);
	
	/**
	 * 更改用户帮派信息
	 * 
	 * @param userId
	 * 
	 */
	public boolean updateUserFactionId(String userId, int factionId);
	
	/**
	 * 消耗用户争霸积分
	 * @param userId
	 * @param pkScore
	 * @param useType
	 * @return
	 */
	public boolean reducePkScore(String userId,int pkScore,int useType);
}
