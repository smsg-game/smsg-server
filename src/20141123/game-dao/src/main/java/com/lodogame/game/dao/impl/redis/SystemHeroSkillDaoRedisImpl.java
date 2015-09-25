package com.lodogame.game.dao.impl.redis;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.lodogame.game.dao.SystemHeroSkillDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemHeroSkill;

public class SystemHeroSkillDaoRedisImpl implements SystemHeroSkillDao {

	@Override
	public List<SystemHeroSkill> getHeroSkillList(int heroId) {
		String key = RedisKey.getHeroSkillCacheKey();
		String json = JedisUtils.getFieldFromObject(key, String.valueOf(heroId));
		if (StringUtils.isNotEmpty(json)) {
			return Json.toList(json, SystemHeroSkill.class);
		}
		return null;
	}

}
