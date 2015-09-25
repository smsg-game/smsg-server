package com.lodogame.game.dao.impl.redis;

import org.apache.commons.lang.StringUtils;

import com.lodogame.game.dao.UserMallInfoDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.UserMallInfo;

public class UserMallInfoDaoRedisImpl implements UserMallInfoDao {

	@Override
	public boolean add(String userId, int mallId, int totalBuyNum, int dayBuyNum) {
		String key = RedisKey.getUserMallInfoCacheKey(userId);
		JedisUtils.delFieldFromObject(key, String.valueOf(mallId));
		return true;
	}

	@Override
	public UserMallInfo get(String userId, int mallId) {
		String key = RedisKey.getUserMallInfoCacheKey(userId);
		String json = JedisUtils.getFieldFromObject(key, String.valueOf(mallId));
		if (StringUtils.isNotEmpty(json)) {
			return Json.toObject(json, UserMallInfo.class);
		}
		return null;
	}

	@Override
	public boolean add(UserMallInfo userMallInfo) {
		String key = RedisKey.getUserMallInfoCacheKey(userMallInfo.getUserId());
		String json = Json.toJson(userMallInfo);
		JedisUtils.setFieldToObject(key, String.valueOf(userMallInfo.getMallId()), json);
		return true;
	}

}
