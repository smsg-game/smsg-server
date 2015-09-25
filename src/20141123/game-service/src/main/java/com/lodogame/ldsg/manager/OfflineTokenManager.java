package com.lodogame.ldsg.manager;

import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.OfflineUserToken;
import com.lodogame.ldsg.bo.UserToken;

/**
 * 离线token 管理
 * 
 * @author jacky
 * 
 */
public class OfflineTokenManager {
	private static OfflineTokenManager mgr = null;

	private OfflineTokenManager() {

	}

	public static OfflineTokenManager getInstance() {
		synchronized (OfflineTokenManager.class) {
			if (mgr == null) {
				mgr = new OfflineTokenManager();
			}
			return mgr;
		}
	}

	public void setToken(String token, OfflineUserToken userToken) {
		JedisUtils.setFieldToObject(RedisKey.getOfflineUserTokenCacheKey(), token, Json.toJson(userToken));
	}

	public OfflineUserToken getToken(String token) {
		return Json.toObject(JedisUtils.getFieldFromObject(RedisKey.getOfflineUserTokenCacheKey(), token), OfflineUserToken.class);
	}

	public void removeToken(String token) {
		JedisUtils.delFieldFromObject(RedisKey.getOfflineUserTokenCacheKey(), token);
	}
}
