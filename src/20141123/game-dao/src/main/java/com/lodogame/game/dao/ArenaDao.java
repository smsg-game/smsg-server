package com.lodogame.game.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lodogame.model.ArenaHero;
import com.lodogame.model.ArenaReg;

/**
 * 百人斩dao
 * 
 * @author jacky
 * 
 */
public interface ArenaDao {

	public boolean add(ArenaReg arenaReg);

	/**
	 * 根据连胜数获取分组
	 * 
	 * @param winCount
	 * @param status
	 * @return
	 */
	public List<ArenaReg> getByWinCount(int winCount, int status);

	/**
	 * 根据用户id获取玩家报名信息
	 * 
	 * @param userId
	 * @return
	 */
	public ArenaReg getByUserId(String userId);

	/**
	 * 获取连胜组ID号
	 * 
	 * @return
	 */
	public List<Integer> getWinCountList();

	/**
	 * 获取排行列表
	 * 
	 * @return
	 */
	public List<ArenaReg> getRankList();

	/**
	 * 清除数据，重新开始
	 */
	public void cleanData();

	/**
	 * 获取用户武将
	 * 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public ArenaHero getArenaHero(String userId, String userHeroId);

	/**
	 * 添加用户武将
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param arenaHero
	 * @return
	 */
	public boolean addArenaHero(String userId, String userHeroId, ArenaHero arenaHero);

	/**
	 * 获取所有报名列表
	 * 
	 * @return
	 */
	public Collection<ArenaReg> getArenaRegList();

	/**
	 * 获取用户的百人斩武将信息
	 * 
	 * @return
	 */
	public Map<String, ArenaHero> getUserArenaHero(String userId);

	/**
	 * 重置用户武将血量
	 * 
	 * @param userId
	 * @return
	 */
	public boolean resetUserHero(String userId);

	/**
	 * 获取用户排名
	 * 
	 * @param userId
	 * @return
	 */
	public int getRank(String userId);

	/**
	 * 获取报名总人总
	 * 
	 * @return
	 */
	public int getRegCount();
}