package com.lodogame.game.dao.impl.redis;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.SystemUserLevelDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemUserLevel;

public class SystemUserLevelDaoRedisImpl implements SystemUserLevelDao {

	public SystemUserLevel getUserLevel(long exp) {
		throw new NotImplementedException();
	}

	public SystemUserLevel get(int level) {

		String key = RedisKey.getSystemUserLevelKey();

		String json = JedisUtils.getFieldFromObject(key, String.valueOf(level));

		if (json != null) {
			return Json.toObject(json, SystemUserLevel.class);
		}

		return null;
	}

	public void add(SystemUserLevel systemUserLevel) {

		String key = RedisKey.getSystemUserLevelKey();

		String json = Json.toJson(systemUserLevel);

		JedisUtils.setFieldToObject(key, String.valueOf(systemUserLevel.getUserLevel()), json);
	}

}
