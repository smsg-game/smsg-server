package com.lodogame.game.dao.impl.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.UserHeroDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.UserHero;
import com.mysql.jdbc.StringUtils;

public class UserHeroDaoRedisImpl implements UserHeroDao {

	@Override
	public List<UserHero> getUserHeroList(String userId) {
		String key = RedisKey.getUserHeroListCacheKey(userId);
		List<String> jsonList = JedisUtils.getMapValues(key);
		if (jsonList != null && jsonList.size() > 0) {
			List<UserHero> userHeroList = new ArrayList<UserHero>();
			for (String json : jsonList) {
				UserHero userHeor = Json.toObject(json, UserHero.class);
				userHeroList.add(userHeor);
			}
			return userHeroList;
		}
		return null;
	}

	@Override
	public void setUserHeroList(String userId, List<UserHero> userHeroList) {
		String key = RedisKey.getUserHeroListCacheKey(userId);
		Map<String, String> hash = new HashMap<String, String>();
		for (UserHero userHero : userHeroList) {
			hash.put(userHero.getUserHeroId(), Json.toJson(userHero));
		}
		JedisUtils.setFieldsToObject(key, hash);
	}

	@Override
	public List<UserHero> getUserHeroList(String userId, int systemHeroId) {
		throw new NotImplementedException();
	}

	@Override
	public UserHero get(String userId, String userHeroId) {
		String key = RedisKey.getUserHeroListCacheKey(userId);
		String json = JedisUtils.getFieldFromObject(key, userHeroId);
		if (!StringUtils.isNullOrEmpty(json)) {
			return Json.toObject(json, UserHero.class);
		}
		return null;
	}

	@Override
	public UserHero getUserHeroByPos(String userId, int pos) {
		throw new NotImplementedException();
	}

	@Override
	public boolean addUserHero(UserHero userHero) {
		if (userHero == null) {
			return false;
		}

		String key = RedisKey.getUserHeroListCacheKey(userHero.getUserId());

		if (!JedisUtils.exists(key)) {
			return false;
		}
		String json = Json.toJson(userHero);
		JedisUtils.setFieldToObject(key, userHero.getUserHeroId(), json);
		return true;
	}

	@Override
	public boolean addUserHero(List<UserHero> userHeroList) {

		if (userHeroList.isEmpty()) {
			return false;
		}

		String key = RedisKey.getUserHeroListCacheKey(userHeroList.get(0).getUserId());

		if (!JedisUtils.exists(key)) {
			return false;
		}

		Map<String, String> hash = new HashMap<String, String>();
		for (UserHero userHero : userHeroList) {
			String json = Json.toJson(userHero);
			hash.put(userHero.getUserHeroId(), json);
		}

		JedisUtils.setFieldsToObject(key, hash);
		return true;
	}

	@Override
	public boolean changePos(String userId, String userHeroId, int pos) {
		throw new NotImplementedException();
	}

	@Override
	public boolean changeSystemHeroId(String userId, String userHeroId, int systemHeroId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean delete(String userId, String userHeroId) {
		String key = RedisKey.getUserHeroListCacheKey(userId);
		JedisUtils.delFieldFromObject(key, userHeroId);
		return true;
	}

	@Override
	public int getUserHeroCount(String userId) {
		String key = RedisKey.getUserHeroListCacheKey(userId);
		return (int) JedisUtils.hlen(key);
	}

	@Override
	public int delete(String userId, List<String> userHeroIdList) {
		for (String userHeroId : userHeroIdList) {
			delete(userId, userHeroId);
		}
		return userHeroIdList.size();
	}

	@Override
	public boolean updateExpLevel(String userId, String userHeroId, int exp, int level) {
		throw new NotImplementedException();
	}

	@Override
	public int getBattleHeroCount(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public List<UserHero> listByHeroLevelDesc(int offset, int size) {
		throw new NotImplementedException();
	}

	@Override
	public List<UserHero> listUserHeroByLevelAsc(String userId, int systemHeroId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean update(String userId, String userHeroId, int systemHeroId, int level, int exp) {
		return this.delete(userId, userHeroId);
	}

	@Override
	public boolean upgradeStage(String userId, String userHeroId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean lockHero(String userId, String userHeroId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean unlockHero(String userId, String userHeroId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean upgradeDeifyNodeLevel(String userId, String userHeroId,
			int deifyNodeLevel) {
		throw new NotImplementedException();
	}

	@Override
	public List<UserHero> getDeifyPeopleList(String userId) {
		String key = RedisKey.getUserHeroListCacheKey(userId);
		List<String> jsonList = JedisUtils.getMapValues(key);
		if (jsonList != null && jsonList.size() > 0) {
			List<UserHero> userHeroList = new ArrayList<UserHero>();
			for (String json : jsonList) {
				UserHero userHeor = Json.toObject(json, UserHero.class);
				if(userHeor.getDeifyNodeLevel() == 6){
					userHeroList.add(userHeor);
				}
			}
			return userHeroList;
		}
		return null;
	}
}
