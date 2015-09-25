package com.lodogame.game.dao.impl.redis;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.SystemTaskDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemTask;
import com.mysql.jdbc.StringUtils;

public class SystemTaskDaoRedisImpl implements SystemTaskDao {

	@Override
	public SystemTask get(int systemTaskId) {
		String key = RedisKey.getSystemTaskKey();
		String json = JedisUtils.getFieldFromObject(key, String.valueOf(systemTaskId));
		if (!StringUtils.isNullOrEmpty(json)) {
			return Json.toObject(json, SystemTask.class);
		}
		return null;
	}

	@Override
	public List<SystemTask> getPosTaskList(int systemTaskId) {
		throw new NotImplementedException();
	}

	@Override
	public List<SystemTask> getByTaskTargetType(int targetType) {
		throw new NotImplementedException();
	}
}
