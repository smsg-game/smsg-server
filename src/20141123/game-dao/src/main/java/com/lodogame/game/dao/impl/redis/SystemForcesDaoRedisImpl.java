package com.lodogame.game.dao.impl.redis;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemForces;

public class SystemForcesDaoRedisImpl implements SystemForcesDao {

	@Override
	public List<SystemForces> getSceneForcesList(int sceneId) {
		throw new NotImplementedException();
	}

	@Override
	public SystemForces get(int forcesId) {

		String key = RedisKey.getSystemForcesCacheKey(forcesId);
		String json = JedisUtils.get(key);
		if (json != null) {
			return Json.toObject(json, SystemForces.class);
		}

		return null;
	}

	@Override
	public List<SystemForces> getSystemForcesByPreForcesId(int preForcesId) {
		throw new NotImplementedException();
	}

	@Override
	public List<SystemForces> getSystemForcesByType(int type) {
		throw new NotImplementedException();
	}

	@Override
	public int updateTimes(int forcesId, int times) {
		throw new NotImplementedException();
	}
}
