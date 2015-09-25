package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemHeroSkill;

public interface SystemHeroSkillDao {

	/**
	 * 获取武将组合技
	 * 
	 * @param heroId
	 * @return
	 */
	public List<SystemHeroSkill> getHeroSkillList(int heroId);

}
