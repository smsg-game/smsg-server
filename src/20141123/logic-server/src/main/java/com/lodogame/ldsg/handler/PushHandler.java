package com.lodogame.ldsg.handler;

import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.ChatBO;
import com.lodogame.ldsg.bo.Message;
import com.lodogame.ldsg.bo.UserToolBO;

public interface PushHandler {

	/**
	 * 推送用户信息
	 * 
	 * @param uid
	 */
	public void pushUser(String uid);

	/**
	 * 推送用户武将信息
	 * 
	 * @param uid
	 * @param userHeroId
	 * @param pushType
	 */
	public void pushHero(String uid, String userHeroId, int pushType);

	/**
	 * 推送武将列表
	 * 
	 * @param uid
	 */
	public void pushHeroList(String uid);

	/**
	 * 推送用户装备
	 * 
	 * @param uid
	 * @param userEquipId
	 * @param pushType
	 */
	public void pushEquip(String uid, String userEquipId, int pushType);

	/**
	 * 推送装备列表
	 * 
	 * @param uid
	 */
	public void pushEquipList(String uid);

	/**
	 * 推送用户酒馆信息
	 * 
	 * @param uid
	 */
	public void pushTavernInfo(String uid);

	/**
	 * 推送用户任务
	 * 
	 * @param uid
	 * @param systemTaskId
	 * @param pushType
	 */
	public void pushTask(String uid, int systemTaskId, int pushType);

	/**
	 * 推送任务列表
	 * 
	 * @param uid
	 */
	public void pushTaskList(String uid);

	/**
	 * 推送用户道具
	 * 
	 * @param uid
	 */
	public void pushToolList(String uid);

	/**
	 * 推送用户道具
	 * 
	 * @param uid
	 */
	public void pushToolList(String uid, List<UserToolBO> userToolBOList);

	/**
	 * 推送怪物列表
	 * 
	 * @param uid
	 */
	public void pushForcesList(String uid);

	/**
	 * 推送当前可打的怪物部队
	 * 
	 * @param uid
	 * @param tyupe
	 */
	public void pushCurrentForces(String uid, int type);

	/**
	 * 推送用户所有数据，主要用于登录时推送
	 * 
	 * @param uid
	 */
	public void pushUserData(String uid);

	/**
	 * 重新推送用户所有数据，主要用于登录时推送
	 * 
	 * @param uid
	 */
	public void pushUserData2(String uid);

	/**
	 * 凌晨推送
	 * 
	 * @param uid
	 */
	public void pushMidnightData(String uid);

	/**
	 * 推送消息
	 * 
	 * @param uid
	 * @param message
	 */
	public void pushMessage(String uid, List<Message> msgList);

	/**
	 * 判断用户在线
	 * 
	 * @param uid
	 */
	public void checkUserOnline(String uid);

	/**
	 * 推送数据
	 * 
	 * @param action
	 * @param params
	 */
	public void push(String action, Map<String, Object> params);

	/**
	 * 推送扫荡状态
	 * 
	 * @param userId
	 * @param status
	 */
	public void pushSweepStatus(String userId, int status);

	/**
	 * 推送单笔充值奖励激活信息
	 * 
	 * @param uid
	 */
	public void pushOncePayRewardActivated(String uid);

	/**
	 * 推送单笔充值奖励激活信息
	 * 
	 * @param uid
	 */
	public void pushTotalPayRewardActivated(String uid);

	/**
	 * 推送新邮件
	 * 
	 * @param uid
	 */
	public void pushNewMail(String uid);
	
	/**
	 * 推送积天充值充值奖励激活信息
	 * 
	 * @param uid
	 */
	public void pushTotalDayPayReward(String uid);
	
	/**
	 * 推送战斗开始
	 * @param type (1国战)
	 */
	public void pushBattleStart();

	public void pushDeifyStatus(String userId, Map<String, Object> params);

	/**
	 * 推送聊天
	 * 
	 * @param uid
	 */
	public void pushChat(String userId, ChatBO chatBO);

	public void pushMonthlyCard(String userId);
}
