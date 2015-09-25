package com.lodogame.ldsg.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.AdminSendAttachBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.model.SystemActivity;
import com.lodogame.model.SystemEquip;
import com.lodogame.model.SystemGoldSet;
import com.lodogame.model.SystemHero;
import com.lodogame.model.SystemMallDiscountActivity;
import com.lodogame.model.SystemMallDiscountItems;
import com.lodogame.model.SystemTool;
import com.lodogame.model.User;
import com.lodogame.model.UserMapper;

/**
 * 用于游戏服务端提供给外部调用的WEB 接口，如登录、充值等
 * 
 * @author CJ
 * 
 */
public interface GameApiService {

	/**
	 * 用户不存在
	 */
	public static final int PAYMENT_ERROR_USER_NOT_EXISTS = 2005;

	/**
	 * 成功
	 */
	public static final int SUCCESS = 1000;
	/**
	 * 未知错误
	 */
	public static final int UNKNOWN_ERROR = 2000;
	/**
	 * 参数错误
	 */
	public static final int PARAM_ERROR = 2001;
	/**
	 * 校验签名错误sss
	 */
	public static final int SIGN_CHECK_ERROR = 2002;

	/**
	 * 加载平台用户映射，不存在则重新创建
	 * 
	 * @param partnerUserId
	 * @param serverId
	 * @param partnerId
	 * @param qn
	 * @return
	 */
	public UserMapper loadUserMapper(String partnerUserId, String serverId, String partnerId, String qn, String imei, String mac, String idfa);

	/**
	 * 游戏支付
	 * 
	 * @param partnerId
	 *            合作商ID
	 * @param serverId
	 *            服务器ID
	 * @param partnerUserId
	 *            合作商用户ID
	 * @param channel
	 *            充值渠道
	 * @param orderId
	 *            订单ID
	 * @param amount
	 *            金额
	 * @param gold
	 *            金币
	 * @param userIp
	 *            用户IP
	 * @param remark
	 *            备注
	 * @return
	 */
	public boolean doPayment(String partnerId, String serverId, String partnerUserId, String channel, String orderId, BigDecimal amount, int gold, String userIp, String remark);

	/**
	 * 增加金币
	 * 
	 * @param gold
	 *            金币数量
	 * @param playerIds
	 *            增加用户列表
	 * @return
	 */
	public String addGold(int gold, String[] playerIds);

	/**
	 * 增加金币t 通过userid来发
	 * 
	 * @param gold
	 *            金币数量
	 * @param userIds
	 *            增加用户列表
	 * @return
	 */
	public String addGoldByUserIds(int gold, String[] userIds);

	/**
	 * 增加银币
	 * 
	 * @param type
	 * @param copper
	 *            银币数量
	 * @param playerIdArr
	 *            用户列表
	 * @param sign
	 */
	public String addCopper(int copper, String[] playerIds);

	/**
	 * 增加武将
	 * 
	 * @param heroIdArr
	 *            武将列表
	 * @param playerIdArr
	 *            用户列表
	 */
	public String addHeros(String[] heroIdArr, String[] playerIdArr);

	/**
	 * 增加装备
	 * 
	 * @param equipIdArr
	 * @param playerIdArr
	 */
	public String addEquips(String[] equipIdArr, String[] playerIdArr);

	/**
	 * 发送消息
	 * 
	 * @param content
	 * @param partnerIds
	 */
	public void sendSysMsg(String content, String partnerIds);

	/**
	 * 获取英雄列表
	 * 
	 * @return
	 */
	public List<SystemHero> getAllSystemHero();

	/**
	 * 获取装备列表
	 * 
	 * @return
	 */
	public List<SystemEquip> getAllSystemEquip();

	/**
	 * 减银币
	 * 
	 * @param copper
	 * @param playerIds
	 */
	public String reduceCopper(int copper, String[] playerIds);

	/**
	 * 根据playerId获取partnerUserId
	 * 
	 * @param playerId
	 * @param serverId
	 * @return
	 */
	public String getPartnerUserId(int playerId, String serverId);

	/**
	 * 获取除了装备和武将以外的其他道具
	 * 
	 * @return
	 */
	public List<SystemTool> getAllOtherTool();

	/**
	 * 发送道具
	 * 
	 * @param toolIdArr
	 * @param playerIdArr
	 */
	public String addTools(String[] toolIdArr, String[] playerIdArr);

	/**
	 * 获取支付设置
	 * 
	 * @return
	 */
	public List<SystemGoldSet> getPaySettings();

	/**
	 * 发送道具，可指定数量
	 * 
	 * @param toolIds
	 * @param playerIds
	 * @param nums
	 */
	String addTools(String[] toolIds, String[] playerIds, int nums);

	/**
	 * 发送道具或装备
	 * 
	 * @param attachList
	 * @param playerIds
	 * @param nums
	 */
	Map<String, List<AdminSendAttachBO>> addToolsOrEquipMent(List<AdminSendAttachBO> attachList, String[] playerIds, boolean isAllUser);

	/**
	 * 增加用户武将背包上限
	 * 
	 * @param playerIds
	 * @param nums
	 * @return
	 */
	String addUserHeroBag(String[] playerIds, int nums);

	/**
	 * 增加用户装备背包上限
	 * 
	 * @param playerIds
	 * @param nums
	 * @return
	 */
	String addUserEquipBag(String[] playerIds, int nums);

	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 */
	public User getUserInfo(String userId);

	/**
	 * 获取用户英雄列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserHeroBO> getUserHeroList(String userId);

	/**
	 * 获取用户装备列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserEquipBO> getUserEquipList(String userId);

	/**
	 * 获取用户道具列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserToolBO> getUserToolList(String userId);

	/**
	 * 添加商城打折活动信息
	 */
	public SystemMallDiscountActivity addMallDiscount(String activityId, String startTime, String endTime);

	/**
	 * 添加商城打折活动商品信息
	 */
	public List<SystemMallDiscountItems> addMallDiscountItems(String activityId, String mallIds, String discounts);

	/**
	 * 删除商城打折活动信息
	 */
	public void delActivity(String activityId);

	/**
	 * 删除商城打折活动商品
	 */
	public void delItems(String activityId);

	/**
	 * 发送系统邮件
	 * 
	 * @param title
	 * @param content
	 * @param toolIds
	 * @param target
	 * @param userLodoIds
	 */
	public void sendMail(String title, String content, String toolIds, int target, String userLodoIds, String sourceId, Date date, String partner);

	/**
	 * 封号
	 * 
	 * @param dueTime
	 * @return
	 */
	public boolean banUser(String userId, String dueTime);

	/**
	 * 设置 VIP 等级
	 * 
	 * @param userId
	 * @param vipLevel
	 * @return
	 */
	public boolean assignVipLevel(String userId, int vipLevel);

	/**
	 * 创建用户
	 * 
	 * @param username
	 */
	public boolean createUser(String username, String userId);

	/**
	 * 添加活动
	 * 
	 * @param systemActivity
	 */
	public boolean addActivity(SystemActivity systemActivity);

	/**
	 * 修改活动信息
	 * 
	 * @param systemActivity
	 */
	public boolean modifyActivity(SystemActivity systemActivity);

	/**
	 * 更新FB次数
	 * 
	 * @param forcesId
	 * @param times
	 * @return
	 */
	public int modifyForcesTimes(int forcesId, int times);

	/**
	 * 数据同步
	 * 
	 * @param table
	 * @param sqls
	 * @return
	 */
	public boolean dataSync(String table, String sqls);

	/**
	 * 修改用户等级
	 * 
	 * @param userId
	 * @param level
	 * @param exp
	 * @return
	 */
	public boolean updateUserLevel(String userId, int level, int exp);
}
