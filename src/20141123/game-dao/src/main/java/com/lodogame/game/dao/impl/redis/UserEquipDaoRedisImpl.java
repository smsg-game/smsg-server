package com.lodogame.game.dao.impl.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

import com.lodogame.game.dao.UserEquipDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.UserEquip;

public class UserEquipDaoRedisImpl implements UserEquipDao {

	@Override
	public boolean add(UserEquip userEquip) {
		String key = RedisKey.getUserEquipListCacheKey(userEquip.getUserId());

		if (!JedisUtils.exists(key)) {
			return false;
		}

		JedisUtils.setFieldToObject(key, userEquip.getUserEquipId(), Json.toJson(userEquip));
		return true;
	}

	@Override
	public List<UserEquip> getUserEquipList(String userId) {
		String key = RedisKey.getUserEquipListCacheKey(userId);
		List<String> jsonList = JedisUtils.getMapValues(key);
		if (jsonList != null && jsonList.size() > 0) {
			return Json.toList(jsonList, UserEquip.class);
		}
		return null;
	}

	@Override
	public List<UserEquip> getHeroEquipList(String userId, String userHeroId) {
		List<UserEquip> userEquipList = this.getUserEquipList(userId);
		List<UserEquip> list = new ArrayList<UserEquip>();
		for (UserEquip userEquip : userEquipList) {
			if (StringUtils.equalsIgnoreCase(userEquip.getUserHeroId(), userHeroId)) {
				list.add(userEquip);
			}
		}
		return list;
	}

	@Override
	public void setUserEquipList(String userId, List<UserEquip> userEquipList) {

		String key = RedisKey.getUserEquipListCacheKey(userId);
		Map<String, String> hash = new HashMap<String, String>();
		for (UserEquip userEquip : userEquipList) {
			hash.put(userEquip.getUserEquipId(), Json.toJson(userEquip));
		}
		JedisUtils.setFieldsToObject(key, hash);
	}

	@Override
	public UserEquip get(String userId, String userEquipId) {
		String key = RedisKey.getUserEquipListCacheKey(userId);
		String json = JedisUtils.getFieldFromObject(key, userEquipId);
		if (StringUtils.isNotEmpty(json)) {
			return Json.toObject(json, UserEquip.class);
		}

		return null;
	}

	@Override
	public boolean updateEquipHero(String userId, String userEquipId, String userHeroId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean updateEquipLevel(String userId, String userEquipId, int addLevel, int maxLevel) {
		throw new NotImplementedException();
	}

	@Override
	public boolean delete(String userId, String userEquipId) {
		String key = RedisKey.getUserEquipListCacheKey(userId);
		JedisUtils.delFieldFromObject(key, userEquipId);
		return true;
	}

	@Override
	public boolean delete(String userId, List<String> userEquipIdList) {
		for (String userEquipId : userEquipIdList) {
			delete(userId, userEquipId);
		}
		return userEquipIdList.size() > 0;
	}

	@Override
	public boolean updateEquipId(String userId, String userEquipId, int equipId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getUserEquipCount(String userId) {
		String key = RedisKey.getUserEquipListCacheKey(userId);
		return (int) JedisUtils.hlen(key);
	}

	@Override
	public boolean addEquips(List<UserEquip> userEquipList) {
		if (userEquipList.isEmpty()) {
			return false;
		}

		String key = RedisKey.getUserEquipListCacheKey(userEquipList.get(0).getUserId());

		if (!JedisUtils.exists(key)) {
			return false;
		}

		Map<String, String> hash = new HashMap<String, String>();
		for (UserEquip userEquip : userEquipList) {
			String json = Json.toJson(userEquip);
			hash.put(userEquip.getUserEquipId(), json);
		}

		JedisUtils.setFieldsToObject(key, hash);
		return true;
	}

	@Override
	public List<UserEquip> getUserEquipList(String userId, int equipId) {
		throw new NotImplementedException();
	}

	@Override
	public List<UserEquip> listUserEquipByLevelAsc(String userId, int equipId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean updateUserDeify(String userId, int equipId, int equipLevel, String userEquipId) {
		throw new NotImplementedException();
	}

}
