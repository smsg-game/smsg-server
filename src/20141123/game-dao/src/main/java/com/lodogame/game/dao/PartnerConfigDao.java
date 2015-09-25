package com.lodogame.game.dao;

import com.lodogame.model.PartnerConfig;

public interface PartnerConfigDao {
	/**
	 * 根据partnerId获取配置
	 * @param partnerId
	 * @return
	 */
	public PartnerConfig getById(String partnerId);
	
	public boolean save(PartnerConfig partnerConfig);
}
