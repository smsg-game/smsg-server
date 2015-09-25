package com.lodogame.game.dao;

/**
 * 转生模块 Dao
 * @author liaocheng
 *
 */
public interface UserHeroRegenerateDao {
	/**
	 * 根据转生前 system_hero_id 查询转生后 system_hero_id
	 */
	public int getPostHeroId(int preHeroId);
	
	/**
	 * 根据转生前 system_hero_id 查询所需的转生丹数量
	 */
	public int getPillNum(int preHeroId);
	
	/**
	 * 根据转生前 system_hero_id 查询所需的鬼之合约数量
	 */
	public int getContractNum(int preHeroId);
	
	/**
	 * 根据转生前 system_hero_id 查询所需的相同武将数量
	 */
	public int getHeroNum(int preHeroId);
	
}
