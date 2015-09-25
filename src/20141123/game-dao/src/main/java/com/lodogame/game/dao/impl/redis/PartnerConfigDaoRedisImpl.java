package com.lodogame.game.dao.impl.redis;

import com.lodogame.game.dao.PartnerConfigDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.PartnerConfig;

public class PartnerConfigDaoRedisImpl implements PartnerConfigDao {

	@Override
	public PartnerConfig getById(String partnerId) {
		String key = RedisKey.getPartnerConfigKey();
		String json = JedisUtils.getFieldFromObject(key, partnerId);
		return Json.toObject(json, PartnerConfig.class);
	}

	@Override
	public boolean save(PartnerConfig partnerConfig) {
		String key = RedisKey.getPartnerConfigKey();
		String json = Json.toJson(partnerConfig);
		JedisUtils.setFieldToObject(key, String.valueOf(partnerConfig.getPartnerId()), json);
		return true;
	}

}
