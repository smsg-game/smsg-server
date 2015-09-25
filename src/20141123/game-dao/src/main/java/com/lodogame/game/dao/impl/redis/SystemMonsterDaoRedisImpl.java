package com.lodogame.game.dao.impl.redis;

import com.lodogame.game.dao.SystemMonsterDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemMonster;
import com.mysql.jdbc.StringUtils;

public class SystemMonsterDaoRedisImpl implements SystemMonsterDao {

	@Override
	public SystemMonster get(int monsterId) {
		String key = RedisKey.getSystemMonsterCacheKey(monsterId);
		String json = JedisUtils.get(key);
		if (!StringUtils.isNullOrEmpty(json)) {
			return Json.toObject(json, SystemMonster.class);
		}
		return null;
	}

	@Override
	public void add(SystemMonster systemMonster) {
		String key = RedisKey.getSystemMonsterCacheKey(systemMonster.getMonsterId());
		String json = Json.toJson(systemMonster);
		JedisUtils.setString(key, json);
	}

}
