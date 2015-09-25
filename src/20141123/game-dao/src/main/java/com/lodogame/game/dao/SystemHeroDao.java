package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemHero;

/**
 * 系统武将DAO
 * 
 * @author jacky
 * 
 */

public interface SystemHeroDao {

	/**
	 * 获取系统武将列表
	 * 
	 * @return
	 */
	public List<SystemHero> getSysHeroList();

	/**
	 * 获取系统武将
	 * 
	 * @param systemHeroId
	 * @return
	 */
	public SystemHero get(Integer systemHeroId);

	/**
	 * 增加系统武将
	 * 
	 * @param systemHero
	 * @return
	 */
	public void add(SystemHero systemHero);
	
	/**
	 * 获取 systemHeroId
	 */
	public int getSystemHeroId(Integer heroId, Integer heroColor);

}
