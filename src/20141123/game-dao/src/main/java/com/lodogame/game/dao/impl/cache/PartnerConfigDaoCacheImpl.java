package com.lodogame.game.dao.impl.cache;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.PartnerConfigDao;
import com.lodogame.game.dao.impl.mysql.PartnerConfigDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.PartnerConfigDaoRedisImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.PartnerConfig;

public class PartnerConfigDaoCacheImpl implements PartnerConfigDao, ReloadAble {

	@Autowired
	private PartnerConfigDaoMysqlImpl partnerConfigDaoMysqlImpl;

	@Autowired
	private PartnerConfigDaoRedisImpl partnerConfigDaoRedisImpl;

	@Override
	public PartnerConfig getById(String partnerId) {
		PartnerConfig partnerConfig = this.partnerConfigDaoRedisImpl.getById(partnerId);
		if (partnerConfig != null) {
			return partnerConfig;
		}

		partnerConfig = this.partnerConfigDaoMysqlImpl.getById(partnerId);
		if (partnerConfig != null) {
			this.partnerConfigDaoRedisImpl.save(partnerConfig);
		}

		return partnerConfig;
	}

	public PartnerConfigDaoRedisImpl getPartnerConfigDaoRedisImpl() {
		return partnerConfigDaoRedisImpl;
	}

	public void setPartnerConfigDaoRedisImpl(PartnerConfigDaoRedisImpl partnerConfigDaoRedisImpl) {
		this.partnerConfigDaoRedisImpl = partnerConfigDaoRedisImpl;
	}

	@Override
	public boolean save(PartnerConfig partnerConfig) {
		return partnerConfigDaoMysqlImpl.save(partnerConfig);
	}

	public PartnerConfigDaoMysqlImpl getPartnerConfigDaoMysqlImpl() {
		return partnerConfigDaoMysqlImpl;
	}

	public void setPartnerConfigDaoMysqlImpl(PartnerConfigDaoMysqlImpl partnerConfigDaoMysqlImpl) {
		this.partnerConfigDaoMysqlImpl = partnerConfigDaoMysqlImpl;
	}

	@Override
	public void reload() {

	}

	@Override
	public void init() {

	}

}
