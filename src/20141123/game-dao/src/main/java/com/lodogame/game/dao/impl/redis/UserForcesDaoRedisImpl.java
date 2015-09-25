package com.lodogame.game.dao.impl.redis;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;


import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.UserForces;
import com.lodogame.model.UserForcesCount;
import com.mysql.jdbc.StringUtils;

public class UserForcesDaoRedisImpl implements UserForcesDao {
	/**
	 * 初始化大关卡列表缓存
	 * @param userId
	 * @param sceneId
	 * @param list
	 */
	public void initUserSceneCache(String userId,int sceneId,List<UserForces> list){
		String key = RedisKey.getUserFoceSceneIdKey(userId);
		JedisUtils.setFieldToObject(key,sceneId+"", Json.toJson(list));
	}
	/**
	 * 初始化小关卡列表缓存
	 * @param userId
	 * @param forceId
	 * @param userForces
	 */
	public void initUserForceCache(String userId,int forceId,UserForces userForces){
		if(userForces==null){
			return;
		}
		String key = RedisKey.getUserFoceForceIdKey(userId);
		JedisUtils.setFieldToObject(key,forceId+"",Json.toJson(userForces));
	}
	/**
	 * 初始化关卡类型列表缓存
	 * @param userId
	 * @param forceType
	 * @param userForces
	 */
	public void initUserForceTypeCache(String userId,int forceType,UserForces userForces){
		if(userForces==null){
			return;
		}
		String key = RedisKey.getUserFoceForceTypeKey(userId);
		JedisUtils.setFieldToObject(key,forceType+"",Json.toJson(userForces));
	}
	
	@Override
	public List<UserForces> getUserForcesList(String userId,int sceneId) {
		String key = RedisKey.getUserFoceSceneIdKey(userId);
		List<UserForces> result=null;
		String str = JedisUtils.getFieldFromObject(key, sceneId+"");
		if(!StringUtils.isNullOrEmpty(str)){
			result = Json.toList(str, UserForces.class);
		}
		return result;
	}

	public void remove(String userId){
		JedisUtils.delete(RedisKey.getUserFoceSceneIdKey(userId));
		JedisUtils.delete(RedisKey.getUserFoceForceIdKey(userId));
		JedisUtils.delete(RedisKey.getUserFoceForceTypeKey(userId));
	}
	
	@Override
	public boolean add(UserForces userForces) {
//		String str = Json.toJson(userForces);
//		if(JedisUtils.exists(RedisKey.getUserFoceSceneIdKey(userForces.getUserId()))){
//			JedisUtils.setFieldToObject(RedisKey.getUserFoceSceneIdKey(userForces.getUserId()), userForces.getSceneId()+"", str);
//		}
//		if(JedisUtils.exists(RedisKey.getUserFoceForceIdKey(userForces.getUserId()))){
//			JedisUtils.setFieldToObject(RedisKey.getUserFoceForceIdKey(userForces.getUserId()), userForces.getForcesId()+"", str);
//		}
//		if(JedisUtils.exists(RedisKey.getUserFoceForceTypeKey(userForces.getUserId()))){
//			JedisUtils.setFieldToObject(RedisKey.getUserFoceForceTypeKey(userForces.getUserId()), userForces.getForcesType()+"", str);
//		}
		remove(userForces.getUserId());
		return true;
	}

	@Override
	public UserForces getUserCurrentForces(String userId, int forcesType) {
		String json = JedisUtils.getFieldFromObject(RedisKey.getUserFoceForceIdKey(userId), forcesType+"");
		if(!StringUtils.isNullOrEmpty(json)){
			return Json.toObject(json, UserForces.class);
		}
		return null;
	}

	@Override
	public UserForces get(String userId, int forcesId) {
		String key = RedisKey.getUserFoceForceIdKey(userId);
		String str = JedisUtils.getFieldFromObject(key, forcesId+"");
		UserForces result =null;
		if(!StringUtils.isNullOrEmpty(str)){
			result = Json.toObject(str, UserForces.class);
		}
		return result;
	}

	@Override
	public boolean updateStatus(String userId, int forcesId, int status, int times) {
		remove(userId);
		return true;
	}

	@Override
	public boolean updateTimes(String userId, int forcesId, int times) {
		remove(userId);
		return true;	
	}

	@Override
	public List<UserForcesCount> listOrderByForceCntDesc(int offset, int size) {
		throw new NotImplementedException();
	}
	
	@Override
	public boolean updateTimes(String uid, int times, List<Integer> forcesIds) {
		remove(uid);
		return true;
	}
	@Override
	public boolean resetForcesTimes(String userId, List<Integer> sceneIdList) {
		remove(userId);
		return true;
	}
	@Override
	public long getAmendEmbattleTime(String userId) {
		String key = RedisKey.getUserAmendEmbattleTimeCacheKey();
		String json = JedisUtils.getFieldFromObject(key, userId);
		if (!StringUtils.isNullOrEmpty(json)) {
			return Long.parseLong(json);
		}

		return 0;
	}

	@Override
	public void setAmendEmbattleTime(String userId, long timestamp) {
		String key = RedisKey.getUserAmendEmbattleTimeCacheKey();
		JedisUtils.setFieldToObject(key, userId, String.valueOf(timestamp));
	}
}
