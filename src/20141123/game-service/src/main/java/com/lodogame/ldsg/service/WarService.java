package com.lodogame.ldsg.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.ChooseResultBO;
import com.lodogame.ldsg.bo.CityBo;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserWarInfoBo;
import com.lodogame.ldsg.bo.WarAllCDBO;
import com.lodogame.ldsg.bo.WarAttackRankBO;
import com.lodogame.ldsg.bo.WarAwardBo;
import com.lodogame.ldsg.bo.WarEnterBo;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.model.UserWarInfo;

public interface WarService {

	/**
	 * 国家人数已满
	 */
	public final static int COUNTRY_FULL = 2002;

	/**
	 * 国战还没开始
	 */
	public final static int NO_START = 2003;

	/**
	 * 元宝不足
	 */
	public final static int NO_GOLD = 2004;

	/**
	 * 银币不足
	 */
	public final static int NO_COPPER = 2005;

	/**
	 * 等级不够
	 */
	public final static int LEVEL_NOT_ENOUGH = 2006;

	/**
	 * 鼓舞超过最大次数
	 */
	public final static int MAX_INSPIRE = 2007;

	/**
	 * 领取奖励还没到时
	 */
	public final static int DRAW_TIME_OUT = 2008;

	/**
	 * 城池选择错误
	 */
	public final static int CITY_ERROR = 2009;

	/**
	 * 行动CD
	 */
	public final static int ACTION_TIME_ERROR = 3001;

	/**
	 * 木有行动CD
	 */
	public final static int NO_ACTION_TIME_ERROR = 2010;

	/**
	 * 鼓舞CD
	 */
	public final static int INSPIRE_TIME_ERROR = 3002;

	/**
	 * 英雄背包不足
	 */
	public final static int EXCHANGE_HERO_BAG_NOT_ENOUGH = 3003;

	/**
	 * 装备背包不足
	 */
	public final static int EXCHANGE_EQUIP_BAG_NOT_ENOUGH = 3004;

	/**
	 * 剩余声望不足
	 */
	public final static int EXCHANGE_REPUTATION_NOT_ENOUGH = 3005;

	/**
	 * 银币清空次数已用
	 */
	public final static int COPPER_CLEAR_ACTION_TIME_NOT_ENOUGH = 3006;

	/**
	 * 没有奖励可以领取
	 */
	public final static int ATTACK_REWARD_NOT_REWARD = 2001;

	/**
	 * 国战入口
	 * 
	 * @return
	 */
	public WarEnterBo enter(String userId);

	/**
	 * 领取城池停留奖励
	 * 
	 * @return
	 */
	public CommonDropBO drawStay(String userId, Integer point);

	/**
	 * 选择国家
	 */
	public ChooseResultBO choose(String userId, int cid);

	/**
	 * 攻打城池
	 * 
	 * @param point
	 */
	public Map<String, Object> attackCity(String attackUserId, Integer point, EventHandle handle);

	/**
	 * 清除行动CD
	 * 
	 * @param userId
	 */
	public void clearActionCD(String userId, int grantType);

	/**
	 * 清楚复活CD
	 * 
	 * @param userId
	 */
	public void clearLiftCD(String userId);

	/**
	 * 鼓舞
	 * 
	 * @param userID
	 */
	public void inspire(String userID);

	/**
	 * 侦察
	 * 
	 * @param userId
	 * @return
	 */
	public List<CityBo> look(String userId);

	/**
	 * 冷却CD和攻击胜利次数总接口
	 * 
	 * @param userId
	 * @return
	 */
	public WarAllCDBO getCDAndAttackNum(String userId);

	public List<CityBo> getAllCity();

	public UserWarInfoBo getUserWarInfoBo(String userId);

	/**
	 * 获取奖励列表
	 * 
	 * @return
	 */
	public List<WarAwardBo> getAwardList();

	/**
	 * 兑换奖励
	 * 
	 * @param awardId
	 *            奖励ID
	 * @return
	 */
	public CommonDropBO exchange(String userId, int awardId, int num);

	/**
	 * 
	 * @param point
	 * @return
	 */
	public List<UserWarInfo> getAllUserByPoint(Integer point);

	public UserWarInfo get(String userId);

	/**
	 * 退出国战
	 * 
	 * @param userId
	 */
	public void exitWar(String userId);

	/**
	 * 国战开始时间
	 * 
	 * @return
	 */
	public Date getStartTime();

	/**
	 * 国战结束时间
	 * 
	 * @return
	 */
	public Date getEndTime();

	/**
	 * 获取排行榜
	 * 
	 * @return
	 */
	public List<WarAttackRankBO> getWarAttackRankBOList(String userId);

	/**
	 * 领取排行奖励
	 * 
	 * @param userId
	 * @return
	 */
	public CommonDropBO receive(String userId);

}
