package com.lodogame.game.dao.impl.redis;

import com.lodogame.game.dao.RankScoreCfgDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.RankScoreCfg;

public class RankScoreCfgDaoRedisImpl implements RankScoreCfgDao {

	@Override
	public RankScoreCfg getByRank(int rank) {
		String json = JedisUtils.getFieldFromObject(RedisKey.getRankScoreCfgCacheKey(), Integer.toString(rank));
		if (json != null) {
			return Json.toObject(json, RankScoreCfg.class);
		}
		return null;
	}
}
