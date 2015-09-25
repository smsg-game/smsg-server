package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemEquip;

public interface SystemEquipDao {

	/**
	 * 
	 * @return
	 */
	public List<SystemEquip> getSystemEquipList();

	/**
	 * 获取系统装备
	 * 
	 * @param equipId
	 * @return
	 */
	public SystemEquip get(int equipId);

	/**
	 * 添加系统装备(主要是redsi)
	 * 
	 * @param systemEquip
	 * @return
	 */
	public boolean add(SystemEquip systemEquip);

}
