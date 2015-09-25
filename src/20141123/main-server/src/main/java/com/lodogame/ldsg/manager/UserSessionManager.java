package com.lodogame.ldsg.manager;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于保存用户ID和Session的对应关系，根据用户ID获得相应的SessionId
 * 
 * @author CJ
 * 
 */
public class UserSessionManager {

	private Map<String, String> map;
	private static UserSessionManager mgr = null;

	private UserSessionManager() {
		map = new HashMap<String, String>();
	}

	public static UserSessionManager getInstance() {

		synchronized (UserSessionManager.class) {
			if (mgr == null) {
				mgr = new UserSessionManager();
			}
			return mgr;
		}
	}

	public void setUserSession(String userId, String sessionId) {
		map.put(userId, sessionId);
	}

	public String getUserSessionId(String userId) {
		return map.get(userId);
	}

	public void clearUserSessionId(String userId) {
		map.remove(userId);
	}
}
