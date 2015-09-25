package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.ActivityDrawTool;
import com.lodogame.model.SystemActivityDrawShow;
import com.lodogame.model.UserDrawTime;

public interface ActivityDrawDao {

	/**
	 * 用户抽奖积分 如果数据库没有记录默认为0
	 * 
	 * @param userId
	 * @return
	 */
	int getUserDrawScore(String userId);

	/**
	 * 奖品上限值
	 * 
	 * @param outID
	 * @return
	 */
	int getDrawToolUpper(int outID);

	/**
	 * 更新用户抽奖积分
	 * 
	 * @param userId
	 * @param time
	 */
	void updateUserDrawTime(String userId, String username, int time, int score);

	/**
	 * 
	 * @param userId
	 * @param outId
	 * @param num
	 */
	public void updateUserGainTotal(String userId, int outId, int num);

	/**
	 * 获取单个用户抽中的总数
	 * 
	 * @param userId
	 * @param outId
	 */
	public int getUserGainTotal(String userId, int outId);

	/**
	 * 获取所有用户抽中的总数
	 * 
	 * @param outId
	 */
	public int getAllUserGainTotal(int outId);

	/**
	 * 抽奖用户数据（抽奖次数，抽奖积分）
	 * 
	 * @param userId
	 * @return
	 */
	UserDrawTime getActivityDrawUserData(String userId);

	/**
	 * 抽奖活动积分排行榜
	 * 
	 * @return
	 */
	List<UserDrawTime> getActivityDrawScoreRank();

	/**
	 * 添加抽奖活动道具数量记录
	 * 
	 * @param num
	 * @param outId
	 * @return
	 */
	boolean addActivityDrowToolNumRecorde(int num, int outId);

	/**
	 * 拿今天抽了多少个道具
	 * 
	 * @param outId
	 * @return
	 */
	int getTodayDrowToolNum(int outId);

	/**
	 * 普通掉落
	 * 
	 * @param type
	 * @return
	 */
	public List<ActivityDrawTool> getNormalDropList(int type);

	/**
	 * 次数达到限制后的掉落
	 * 
	 * @param type
	 * @return
	 */
	public List<ActivityDrawTool> getLimitOverDropList();

	/**
	 * 获取每30次掉落
	 * 
	 * @return
	 */
	public List<ActivityDrawTool> getThirtyDropList();

	/**
	 * 修正道具id
	 * 
	 * @param time
	 * @return
	 */
	public int getOutId(int time);

	/**
	 * 拿指定输出道具
	 * 
	 * @param outId
	 * @param type
	 * @return
	 */
	public ActivityDrawTool getActivityDrawTool(int outId, int type);

	/**
	 * 
	 * @return
	 */
	public List<SystemActivityDrawShow> getActivityDrawShowList();

}
