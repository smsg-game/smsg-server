package com.lodogame.game.dao.impl.redis;

import java.util.List;

import com.lodogame.game.dao.PkAwardDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.PkAward;

public class PkAwardDaoRedisImpl implements PkAwardDao {

	@Override
	public List<PkAward> getAll() {
		List<String> list = JedisUtils.getMapValues(RedisKey.getPkAwardKey());
		return Json.toList(list, PkAward.class);
	}

	@Override
	public PkAward getById(int awardId) {
		String pkAwardStr = JedisUtils.getFieldFromObject(RedisKey.getPkAwardKey(), Integer.toString(awardId));
		return Json.toObject(pkAwardStr, PkAward.class);
	}

	@Override
	public boolean isAwardSended(String date) {
		return true;
	}

	@Override
	public boolean addAwardSendLog(String date) {
		return true;
	}

}
