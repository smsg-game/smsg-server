package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.ActivityDrawScoreRankBo;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.SystemActivityDrawShowBO;

public interface ActivityDrawService {

	public final static int NO_LUCK_ORDER_NUM = 2001;

	/**
	 * 活动抽奖
	 * 
	 * @param userId
	 * @param time
	 * @return
	 */
	CommonDropBO draw(String userId, int time);

	/**
	 * 活动抽奖积分排行
	 * 
	 * @return
	 */
	List<ActivityDrawScoreRankBo> getRank();

	/**
	 * 获取用户幸运珠
	 * 
	 * @param userId
	 * @return
	 */
	int getUserLuckOrder(String userId);

	/**
	 * 获取用户抽奖积分
	 * 
	 * @param userId
	 * @return
	 */
	int getUserDrawScore(String userId);

	public List<SystemActivityDrawShowBO> getSystemActivityDrawShowBOList();
}
