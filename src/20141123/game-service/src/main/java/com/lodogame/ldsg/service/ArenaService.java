package com.lodogame.ldsg.service;

import java.util.Date;
import java.util.List;

import com.lodogame.ldsg.bo.ArenaConfigBO;
import com.lodogame.ldsg.bo.ArenaRankBO;
import com.lodogame.ldsg.bo.ArenaRegBO;
import com.lodogame.ldsg.bo.ArenaRewardBO;

/**
 * 百人斩service
 * 
 * @author jacky
 * 
 */
public interface ArenaService {

	/**
	 * 活动未开始
	 */
	public final static int ENTER_ARENA_NOT_START = 2001;

	/**
	 * 活动已经结束
	 */
	public final static int ENTER_ARENA_HAS_FINISH = 2002;

	/**
	 * 鼓舞金币不足
	 */
	public final static int ENCOURAGE_NOT_ENOUHT_MONEY = 2001;

	/**
	 * 鼓舞已经达到最大次数
	 */
	public final static int ENCOURAGE_OVER_MAX_TIMES = 2002;

	/**
	 * 进入百人斩
	 * 
	 * @param userId
	 * @return
	 */
	public boolean enter(String userId);

	/**
	 * 退出百人斩
	 * 
	 * @param userId
	 * @return
	 */
	public boolean quit(String userId);

	/**
	 * 鼓舞
	 * 
	 * @param userId
	 * @return
	 */
	public boolean encourage(String userId);

	/**
	 * 开始排队
	 * 
	 * @param userId
	 * @return
	 */
	public boolean startMatcher(String userId);

	/**
	 * 获取排行榜
	 * 
	 * @return
	 */
	public List<ArenaRankBO> getRankList();

	/**
	 * 获取用户个人信息
	 * 
	 * @param userId
	 * @return
	 */
	public ArenaRegBO getRegBO(String userId);

	public ArenaRewardBO giveReward(String userId);

	/**
	 * 获取百人斩配置
	 * 
	 * @return
	 */
	public ArenaConfigBO getConfigBO();

	/**
	 * 执行命令
	 * 
	 * @param cmd
	 */
	public void execute(String cmd);

	/**
	 * 获取开始时间
	 * 
	 * @return
	 */
	public Date getStartTime();

	/**
	 * 获取结束时间
	 * 
	 * @return
	 */
	public Date getEndTime();

}
