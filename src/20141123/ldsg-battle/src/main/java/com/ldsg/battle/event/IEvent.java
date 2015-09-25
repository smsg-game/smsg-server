package com.ldsg.battle.event;

import java.util.List;

import com.ldsg.battle.Context;
import com.ldsg.battle.role.Hero;

public interface IEvent {

	/**
	 * 获取上下文
	 * 
	 * @return
	 */
	public Context getContext();

	/**
	 * 获取影响到的英雄列表
	 * 
	 * @return
	 */
	public List<Hero> getHeroList();

}
