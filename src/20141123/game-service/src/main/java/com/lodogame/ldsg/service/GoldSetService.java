package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.SystemGoldSetBO;
import com.lodogame.model.SystemGoldSet;

public interface GoldSetService {

	/**
	 * 获取当前的充值套餐
	 * 
	 * @return
	 */
	public List<SystemGoldSetBO> getGoldSetList();

	public SystemGoldSet getByPayAmount(double amount);
	
	public SystemGoldSet getMaxGoldSet();

}
