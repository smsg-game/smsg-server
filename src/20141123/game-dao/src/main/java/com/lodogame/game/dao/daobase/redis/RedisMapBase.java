package com.lodogame.game.dao.daobase.redis;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.json.Json;

/**
 * map类型redis的实现基类
 * 
 * @author foxwang
 * 
 * @param <T>
 */
public abstract class RedisMapBase<T> implements IRedisMap<Map<String, T>, T> {

	private Class<T> entityClass;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RedisMapBase() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		entityClass = (Class) params[0];
	}

	@Override
	public void initEntry(String userId, Map<String, T> t) {
		if (t != null && !t.isEmpty()) {
			String key = getMyKey(userId);
			Map<String, String> mapTemp = new HashMap<String, String>();
			for (String keyTemp : t.keySet()) {
				mapTemp.put(keyTemp, Json.toJson(t.get(keyTemp)));
			}
			JedisUtils.setObject(key, mapTemp);
		}
	}

	@Override
	public void updateEntry(String userId, Map<String, T> t) {
		initEntry(userId, t);
	}

	@Override
	public void delEntry(String userId) {
		String key = getMyKey(userId);
		JedisUtils.delete(key);
	}

	@Override
	public Map<String, T> getEntry(String userId) {
		String key = getMyKey(userId);
		Map<String, String> map = JedisUtils.getMap(key);
		Map<String, T> result = new HashMap<String, T>();
		if (map != null && map.size() > 0) {
			T t = null;
			for (String keyTemp : map.keySet()) {
				String value = map.get(keyTemp);
				t = Json.toObject(value, entityClass);
				result.put(keyTemp, t);
			}
			return result;
		}
		return null;
	}

	@Override
	public void delEntryEntry(String userId, String entryKey) {
		String key = getMyKey(userId);
		JedisUtils.delFieldFromObject(key, entryKey);
	}

	@Override
	public T getEntryEntry(String userId, String entryKey) {
		String key = getMyKey(userId);
		String value = JedisUtils.getFieldFromObject(key, entryKey);
		if (value != null && !value.equals("")) {
			T result = Json.toObject(value, entityClass);
			return result;
		}
		return null;
	}

	@Override
	public boolean updateEntryEntry(String userId, String entryKey, T t) {
		if (t == null) {
			return false;
		}
		String key = getMyKey(userId);
		JedisUtils.setFieldToObject(key, entryKey, Json.toJson(t));
		return true;
	}

	@Override
	public boolean existUserId(String userId) {
		String key = getMyKey(userId);
		return JedisUtils.exists(key);
	}

	@Override
	public List<T> getAllEntryValue(String userId) {
		String key = getMyKey(userId);
		List<String> listResult = JedisUtils.getMapValues(key);
		if (listResult != null && listResult.size() > 0) {
			List<T> result = Json.toList(listResult, entityClass);
			return result;
		}
		return null;
	}

	private String getMyKey(String userId) {
		return this.getPreKey() + "_" + userId;
	}
}
