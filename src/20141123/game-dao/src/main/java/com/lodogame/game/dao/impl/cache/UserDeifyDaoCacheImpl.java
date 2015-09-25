package com.lodogame.game.dao.impl.cache;

import java.util.HashMap;
import java.util.Map;

import com.lodogame.game.dao.UserDeifyDao;
import com.lodogame.game.dao.impl.mysql.UserDeifyDaoMysqlImpl;
import com.lodogame.model.UserDeifyInfo;

public class UserDeifyDaoCacheImpl implements UserDeifyDao{

	private UserDeifyDaoMysqlImpl userDeifyDaoMysqlImpl;
	public void setUserDeifyDaoMysqlImpl(UserDeifyDaoMysqlImpl userDeifyDaoMysqlImpl) {
		this.userDeifyDaoMysqlImpl = userDeifyDaoMysqlImpl;
	}

	/*
	 * key是userId
	 */
	private Map<String, UserDeifyInfo> cacheMap = new HashMap<String, UserDeifyInfo>();

	@Override
	public UserDeifyInfo getUserDeifyInfo(String uid) {
		UserDeifyInfo userDeifyInfo = cacheMap.get(uid);
		if (userDeifyInfo == null) {
			userDeifyInfo = userDeifyDaoMysqlImpl.getUserDeifyInfo(uid);
			if (userDeifyInfo != null) {
				cacheMap.put(uid, userDeifyInfo);
			}
		}
		return userDeifyInfo;
	}

	@Override
	public boolean add(UserDeifyInfo userDeifyInfo) {
		cacheMap.put(userDeifyInfo.getUserId(), userDeifyInfo);
		return userDeifyDaoMysqlImpl.add(userDeifyInfo);
	}

}
