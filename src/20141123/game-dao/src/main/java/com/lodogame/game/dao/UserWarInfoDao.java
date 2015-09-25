package com.lodogame.game.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.lodogame.model.UserWarInfo;
import com.lodogame.model.WarAttackRank;
import com.lodogame.model.WarAttackRankReward;
import com.lodogame.model.WarCity;

public interface UserWarInfoDao {

	public Collection<String> getWarUserIdList();

	/**
	 * 用户上次国战信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserWarInfo getUserWarInfo(String userId);

	/**
	 * 上次国战结束的城池状态
	 * 
	 * @return
	 */
	public List<WarCity> getCityStatus();

	/**
	 * 每个国家的人数
	 * 
	 * @return
	 */
	public List<WarCity> getCountryPeopleNum();

	/**
	 * 设置领奖时间
	 * 
	 * @param time
	 */
	public void setDrawTime(String userId, Date time);

	/**
	 * 城池的人数
	 * 
	 * @param cityId
	 * @return
	 */
	public int getCityPeopleNum(Integer cityId);

	/**
	 * 清楚行动CD
	 * 
	 * @param time
	 */
	public void clearActionCD(String userId, Date time);

	/**
	 * 清楚复活CD
	 * 
	 * @param time
	 */
	public void clearLiftCD(String userId, Date time);

	/**
	 * 鼓舞
	 * 
	 * @param inspireDefense
	 * @param inspireAttack
	 */
	public void inspire(String userId, Date time);

	/**
	 * 
	 * @param userWarInfo
	 */
	public void add(UserWarInfo userWarInfo);

	/**
	 * 重置用户坐标
	 * 
	 * @param userId
	 */
	public void setUserCity(String userId, Integer cityId);

	/**
	 * 给定城池的用户
	 * 
	 * @param cityId
	 * @return
	 */
	public List<UserWarInfo> getAllUserByCityId(Integer cityId);

	/**
	 * 设置复活时间
	 * 
	 * @param userId
	 * @param date
	 */
	public void setLifeTime(String userId, Date date);

	/**
	 * 设置行动时间
	 * 
	 * @param userId
	 * @param date
	 */
	public void setActionTime(String userId, Date date);

	/**
	 * 防守胜利次数
	 * 
	 * @param userId
	 */
	public void setDefenseNum(String userId);

	/**
	 * 防守胜利次数
	 * 
	 * @param userId
	 */
	public void clearDefenseNum(String userId);

	/**
	 * 攻击胜利次数
	 * 
	 * @param userId
	 */
	public void setAttackNum(String userId);

	/**
	 * 添加国战记录
	 * 
	 * @param date
	 */
	public void addWarLog(String date);

	/**
	 * 是否有国战记录
	 * 
	 * @param date
	 * @return
	 */
	public boolean isWarLog(String date);

	/**
	 * 清除旧数据
	 */
	public void cleanData();

	/**
	 * 设置银币清除行动时间次数
	 */
	public void setCopperClearTime(String userId, int num);

	/**
	 * 备份数据
	 */
	public void backupData();

	/**
	 * 设置战斗力
	 * 
	 * @param userId
	 * @param power
	 */
	public void setPower(String userId, int power);

	/**
	 * 备份排行数据
	 */
	public void backUpRankData();

	/**
	 * 保存排行数据
	 */
	public void saveRankData();

	/**
	 * 更新击杀榜领取状态
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	public boolean updateWarRankAttackStatus(String userId, int status);

	/**
	 * 更新击杀榜领取状态为可领取
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	public boolean updateWarRankAttackStatus();

	/**
	 * 
	 * @param rank
	 * @return
	 */
	public WarAttackRankReward getAttackRankReward(int rank);

	/**
	 * 国战击杀排行榜
	 * 
	 * @param warAttackRank
	 */
	public void addAttackRank(WarAttackRank warAttackRank);

	/**
	 * 获取国战击杀排行榜
	 * 
	 * @return
	 */
	public List<WarAttackRank> getWarAttackRankList();

}
