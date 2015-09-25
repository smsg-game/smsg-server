package com.lodogame.game.dao.impl.redis;

import com.lodogame.game.dao.RankLogDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.RankLog;

public class RankLogDaoRedisImpl implements RankLogDao {

	@Override
	public boolean add(RankLog rankLog) {
		String date = rankLog.getDate();
		String key = rankLog.getRankKey();
		JedisUtils.setFieldToObject(date, key, Json.toJson(rankLog));
		return true;
	}

	@Override
	public RankLog getRankLog(String date, String rankKey) {
		String rankLogStr = JedisUtils.getFieldFromObject(date, rankKey);
		return Json.toObject(rankLogStr, RankLog.class);
	}

	@Override
	public boolean delete(String date, String rankKey) {
		JedisUtils.delFieldFromObject(date, rankKey);
		return true;
	}

}
