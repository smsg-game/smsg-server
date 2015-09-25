package com.ldsg.battle.dao;

import java.util.List;

import com.ldsg.battle.model.EffectModel;
import com.ldsg.battle.model.SkillEffectModel;
import com.ldsg.battle.model.SkillModel;

public interface SkillDao {

	/**
	 * 获取所有技能列表
	 * 
	 * @return
	 */
	public List<SkillModel> getSkillList();

	/**
	 * 获取效果列表
	 * 
	 * @return
	 */
	public List<EffectModel> getEffectList();

	/**
	 * 获取技能效果列表
	 * 
	 * @return
	 */
	public List<SkillEffectModel> getSkillEffectList();
}
