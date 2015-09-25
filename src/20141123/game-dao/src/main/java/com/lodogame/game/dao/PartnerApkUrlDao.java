package com.lodogame.game.dao;

import com.lodogame.model.PartnerApkUrl;

/**
 * API的下载URL
 * @author zyz
 *
 */
public interface PartnerApkUrlDao {
	
	/**
	 * 根据partnerId获取Url
	 * @param partnerId
	 * @return
	 */
	public PartnerApkUrl getByPartnerId(String partnerId);
}
