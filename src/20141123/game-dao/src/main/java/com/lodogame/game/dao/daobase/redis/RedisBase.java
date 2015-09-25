package com.lodogame.game.dao.daobase.redis;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.json.Json;

public abstract class RedisBase<T> implements IRedisBean<T> {

	private Class<T> entityClass;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RedisBase(){
		Type genType = getClass().getGenericSuperclass();   
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();   
		entityClass =  (Class)params[0];  
	}
	@Override
	public void initEntry(String userId,T t) {
		if(t==null){
			return;
		}
		String key = getCacheKey(userId);
		String json = Json.toJson(t);
		JedisUtils.setString(key, json);
	}

	@Override
	public void updateEntry(String userId,T t) {
		if(t==null){return;}
		String key = getCacheKey(userId);
		String json = Json.toJson(t);
		JedisUtils.setString(key, json);
	}

	@Override
	public void delEntry(String userId) {
		String key = getCacheKey(userId);
		if(JedisUtils.exists(key)){
			JedisUtils.delete(key);
		}
	}
	@Override
    public boolean existUserId(String userId){
		String key = getCacheKey(userId);
		return JedisUtils.exists(key);
    }
	@Override
	public T getEntry(String userId) {
		String value = JedisUtils.get(getCacheKey(userId));
		if(value!=null&&!value.equals("")){
			T t = Json.toObject(value, entityClass);
			return t;
		}
		return null;
	}   
	
	private String getCacheKey(String userId){
		return this.getPreKey()+"_"+userId;
	}
}
