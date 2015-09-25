package com.lodogame.ldsg.manager;

import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.UserToken;
import com.lodogame.ldsg.constants.RedisKeys;

public class TokenManager {
	private static TokenManager mgr = null;
	
	private TokenManager(){
		
	}
	
	public static TokenManager getInstance(){
		synchronized (TokenManager.class) {
			if(mgr == null){
				mgr = new TokenManager();
			}
			return mgr;
		}
	}
	
	public void setToken(String token, UserToken userToken){
		JedisUtils.setFieldToObject(RedisKey.getUserTokenCacheKey(), token, Json.toJson(userToken));
	}
	
	public UserToken getToken(String token){
		return Json.toObject(JedisUtils.getFieldFromObject(RedisKey.getUserTokenCacheKey(), token), UserToken.class);
	}
	
	public void removeToken(String token){
		JedisUtils.delFieldFromObject(RedisKey.getUserTokenCacheKey(), token);
	}
}
