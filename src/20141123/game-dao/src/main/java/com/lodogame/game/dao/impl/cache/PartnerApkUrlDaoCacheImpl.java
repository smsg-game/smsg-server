package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.PartnerApkUrlDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.PartnerApkUrl;

public class PartnerApkUrlDaoCacheImpl implements PartnerApkUrlDao, ReloadAble {

	private PartnerApkUrlDao partnerApkUrlDaoMysqlImpl;

	private Map<String, PartnerApkUrl> cache = new ConcurrentHashMap<String, PartnerApkUrl>();

	public void setPartnerApkUrlDaoMysqlImpl(PartnerApkUrlDao partnerApkUrlDaoMysqlImpl) {
		this.partnerApkUrlDaoMysqlImpl = partnerApkUrlDaoMysqlImpl;
	}

	@Override
	public PartnerApkUrl getByPartnerId(String partnerId) {

		if (cache.containsKey(partnerId)) {
			return cache.get(partnerId);
		}

		PartnerApkUrl partnerApiUrl = this.partnerApkUrlDaoMysqlImpl.getByPartnerId(partnerId);
		if (partnerApiUrl != null) {
			cache.put(partnerId, partnerApiUrl);
		}

		return partnerApiUrl;
	}

	@Override
	public void reload() {
		cache.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}
}
