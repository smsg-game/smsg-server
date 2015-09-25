package com.lodogame.game.dao.impl.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.SystemHeroDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemHero;

public class SystemHeroDaoRedisImpl implements SystemHeroDao {

	public List<SystemHero> getSysHeroList() {

		String key = RedisKey.getSystemHeroKey();
		List<String> jsonList = JedisUtils.getMapValues(key);
		List<SystemHero> list = new ArrayList<SystemHero>();
		for (String json : jsonList) {
			SystemHero systemHero = Json.toObject(json, SystemHero.class);
			list.add(systemHero);
		}
		return list;

	}

	public SystemHero get(Integer systemHeroId) {

		String key = RedisKey.getSystemHeroKey();
		String json = JedisUtils.getFieldFromObject(key, String.valueOf(systemHeroId));
		return Json.toObject(json, SystemHero.class);

	}

	public void add(SystemHero systemHero) {

		String key = RedisKey.getSystemHeroKey();

		String json = Json.toJson(systemHero);

		JedisUtils.setFieldToObject(key, String.valueOf(systemHero.getSystemHeroId()), json);

	}

	public void add(List<SystemHero> systemHeroList) {

		String key = RedisKey.getSystemHeroKey();

		Map<String, String> hash = new HashMap<String, String>();
		for (SystemHero systemHero : systemHeroList) {
			hash.put(String.valueOf(systemHero.getSystemHeroId()), Json.toJson(systemHero));
		}
		JedisUtils.setFieldsToObject(key, hash);
	}

	@Override
	public int getSystemHeroId(Integer heroId, Integer heroColor) {
		throw new NotImplementedException();
	}
}
