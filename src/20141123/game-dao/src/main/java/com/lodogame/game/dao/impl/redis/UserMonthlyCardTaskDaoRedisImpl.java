package com.lodogame.game.dao.impl.redis;

import com.lodogame.game.dao.UserMonthlyCardTaskDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.User;
import com.lodogame.model.UserMonthlyCardTask;
import com.mysql.jdbc.StringUtils;

public class UserMonthlyCardTaskDaoRedisImpl implements UserMonthlyCardTaskDao{

	@Override
	public UserMonthlyCardTask getByUserId(String userId) {
		String key = RedisKey.getUserMonthlyCardTaskKey();
		String json = JedisUtils.getFieldFromObject(key, userId);
		if (!StringUtils.isNullOrEmpty(json)) {
			return Json.toObject(json, UserMonthlyCardTask.class);
		}

		return null;
	}

	@Override
	public boolean add(UserMonthlyCardTask task) {
		String key = RedisKey.getUserMonthlyCardTaskKey();
		String json = Json.toJson(task);
		JedisUtils.setFieldToObject(key, task.getUserId(), json);
		return true;
	}

	@Override
	public void update(UserMonthlyCardTask task) {
		String key = RedisKey.getUserMonthlyCardTaskKey();
		JedisUtils.delFieldFromObject(key, task.getUserId());
	}

}
