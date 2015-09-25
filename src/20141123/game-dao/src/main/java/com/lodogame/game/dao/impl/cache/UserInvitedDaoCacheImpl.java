package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.UserInvitedDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.UserInvited;

public class UserInvitedDaoCacheImpl implements UserInvitedDao, ReloadAble {

	private UserInvitedDao userInvitedDaoMysqlImpl;

	private Map<String, UserInvited> cache = new ConcurrentHashMap<String, UserInvited>();

	@Override
	public boolean add(UserInvited userInvited) {
		boolean success = userInvitedDaoMysqlImpl.add(userInvited);
		if (success) {
			cache.put(userInvited.getInvitedUserId(), userInvited);
		}
		return success;
	}

	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	public void reload() {
		cache.clear();
	}

	public void setUserInvitedDaoMysqlImpl(UserInvitedDao userInvitedDaoMysqlImpl) {
		this.userInvitedDaoMysqlImpl = userInvitedDaoMysqlImpl;
	}

	@Override
	public boolean update(String invitedUserId, String finishTaskIds) {
		boolean success = this.userInvitedDaoMysqlImpl.update(invitedUserId, finishTaskIds);
		if (success) {
			cache.remove(invitedUserId);
		}
		return success;
	}

	@Override
	public UserInvited getByInvitedUserId(String invitedUserId) {
		UserInvited userInvited = cache.get(invitedUserId);

		if (userInvited != null) {
			return userInvited;
		} else {
			userInvited = userInvitedDaoMysqlImpl.getByInvitedUserId(invitedUserId);
			if (userInvited != null) {
				cache.put(userInvited.getInvitedUserId(), userInvited);
			}

			return userInvited;
		}
	}

}
