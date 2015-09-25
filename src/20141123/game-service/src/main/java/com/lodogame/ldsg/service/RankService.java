package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.AttackRankBO;
import com.lodogame.ldsg.bo.DefenceRankBO;
import com.lodogame.ldsg.bo.ForcesRankBO;
import com.lodogame.ldsg.bo.HeroColorRankBO;
import com.lodogame.ldsg.bo.HpRankBO;
import com.lodogame.ldsg.bo.PowerRankBO;
import com.lodogame.ldsg.bo.WealthRankBO;

public interface RankService {
	/**
	 * 获取战斗力排行榜
	 * 
	 * @return
	 */
	public List<PowerRankBO> getPowerRanks();

	/**
	 * 获取财富排行榜
	 * 
	 * @return
	 */
	public List<WealthRankBO> getWealthRanks();

	/**
	 * 获取关卡排行
	 * 
	 * @return
	 */
	public List<ForcesRankBO> getForcesRanks();

	/**
	 * 获取武将攻击力排行榜
	 * 
	 * @return
	 */
	public List<AttackRankBO> getAttackRanks();

	/**
	 * 获取防御排名
	 * 
	 * @return
	 */
	public List<DefenceRankBO> getDefenceRanks();

	/**
	 * 获取生命值排名
	 * 
	 * @return
	 */
	public List<HpRankBO> getHpRanks();

	/**
	 * 获取武将品质排行
	 * 
	 * @return
	 */
	public List<HeroColorRankBO> getHeroColorRanks();

	/**
	 * 获取当前排名统计时间
	 * 
	 * @return
	 */
	public long getRankStatTime();

	/**
	 * 排名统计
	 */
	public void rankStat();

	/**
	 * 材富排名统计
	 */
	public void wealthRankStat();

	/**
	 * 统计战力排名，并返回列表，指定返回多少名
	 * 
	 * @param statTime
	 * @param limit
	 */
	public List<PowerRankBO> powerRankStat(long statTime, int limit);
}
