package com.lodogame.game.dao.impl.redis;

import com.lodogame.game.dao.LogOperatorDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;

public class LogOperatorDaoRedisImpl implements LogOperatorDao {

	private String getKey() {
		return RedisKey.getLogOperatorKey();
	}

	@Override
	public boolean add(String sql) {

		return true;

		// String key = getKey();
		// JedisUtils.pushMsg(key, sql);
		// return true;
	}

	@Override
	public String get() {
		String key = getKey();
		String json = JedisUtils.blockPopMsg(0, key);
		return json;
	}

}
