package com.lodogame.game.dao.impl.cache;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.User;
import com.lodogame.model.UserFaction;
import com.lodogame.model.UserShareLog;

public class UserDaoCacheImpl implements UserDao, ReloadAble {

	private UserDao userDaoMysqlImpl;

	private UserDao userDaoRedisImpl;

	public void setUserDaoMysqlImpl(UserDao userDaoMysqlImpl) {
		this.userDaoMysqlImpl = userDaoMysqlImpl;
	}

	public void setUserDaoRedisImpl(UserDao userDaoRedisImpl) {
		this.userDaoRedisImpl = userDaoRedisImpl;
	}

	public boolean add(User user) {
		return this.userDaoMysqlImpl.add(user);
	}

	public User get(String userId) {
		User user = this.userDaoRedisImpl.get(userId);
		if (user != null) {
			return user;
		}
		user = this.userDaoMysqlImpl.get(userId);
		if (user != null) {
			this.userDaoRedisImpl.add(user);
		}
		return user;
	}

	public User getByPlayerId(String playerId) {
		User user = this.userDaoRedisImpl.getByPlayerId(playerId);
		if (user != null) {
			return user;
		}
		user = this.userDaoMysqlImpl.getByPlayerId(playerId);
		if (user != null) {
			this.userDaoRedisImpl.add(user);
		}
		return user;
	}

	public User getByName(String username) {
		return this.userDaoMysqlImpl.getByName(username);
	}

	public boolean addCopper(String userId, int copper) {
		boolean success = this.userDaoMysqlImpl.addCopper(userId, copper);
		if (success) {
			this.userDaoRedisImpl.addCopper(userId, copper);
		}
		return success;
	}

	public boolean reduceCopper(String userId, int copper) {
		boolean success = this.userDaoMysqlImpl.reduceCopper(userId, copper);
		if (success) {
			this.userDaoRedisImpl.reduceCopper(userId, copper);
		}
		return success;
	}

	public boolean reduceGold(String userId, int gold) {
		boolean success = this.userDaoMysqlImpl.reduceGold(userId, gold);
		if (success) {
			this.userDaoRedisImpl.reduceGold(userId, gold);
		}
		return success;
	}

	public boolean updateVIPLevel(String userId, int VIPLevel, Date vipExpiredTime) {
		boolean success = this.userDaoMysqlImpl.updateVIPLevel(userId, VIPLevel, vipExpiredTime);
		if (success) {
			this.userDaoRedisImpl.updateVIPLevel(userId, VIPLevel, vipExpiredTime);
		}
		return success;
	}

	public boolean addGold(String userId, int gold) {
		boolean success = this.userDaoMysqlImpl.addGold(userId, gold);
		if (success) {
			this.userDaoRedisImpl.addGold(userId, gold);
		}
		return success;
	}

	public boolean addExp(String userId, int exp, int level, int resumePower) {
		boolean success = this.userDaoMysqlImpl.addExp(userId, exp, level, resumePower);
		if (success) {
			this.userDaoRedisImpl.addExp(userId, exp, level, resumePower);
		}
		return success;
	}

	@Override
	public boolean addPower(String userId, int power, int maxPower, Date powerAddTime) {
		boolean success = this.userDaoMysqlImpl.addPower(userId, power, maxPower, powerAddTime);
		if (success) {
			this.userDaoRedisImpl.addPower(userId, power, maxPower, powerAddTime);
		}
		return success;
	}

	@Override
	public boolean resetPowerAddTime(String userId, Date powerAddTime) {
		boolean success = this.userDaoMysqlImpl.resetPowerAddTime(userId, powerAddTime);
		if (success) {
			this.userDaoRedisImpl.resetPowerAddTime(userId, powerAddTime);
		}
		return success;
	}

	@Override
	public boolean reducePowre(String userId, int power) {
		boolean success = this.userDaoMysqlImpl.reducePowre(userId, power);
		if (success) {
			this.userDaoRedisImpl.reducePowre(userId, power);
		}
		return success;
	}

	@Override
	public Set<String> getOnlineUserIdList() {
		return this.userDaoRedisImpl.getOnlineUserIdList();
	}

	@Override
	public boolean setOnline(String userId, boolean online) {
		return this.userDaoRedisImpl.setOnline(userId, online);
	}

	@Override
	public boolean isOnline(String userId) {
		return userDaoRedisImpl.isOnline(userId);
	}

	@Override
	public boolean cleanCacheData(String userId) {
		return this.userDaoRedisImpl.cleanCacheData(userId);
	}

	@Override
	public List<User> listOrderByLevelDesc(int offset, int size) {
		return this.userDaoMysqlImpl.listOrderByLevelDesc(offset, size);
	}

	@Override
	public List<User> listOrderByCopperDesc(int offset, int size) {
		return this.userDaoMysqlImpl.listOrderByCopperDesc(offset, size);
	}

	@Override
	public void reload() {

	}

	@Override
	public void init() {

	}

	@Override
	public List<String> getAllUserIds() {
		return userDaoMysqlImpl.getAllUserIds();
	}

	@Override
	public boolean reduceExp(String userId, int amount) {
		return userDaoMysqlImpl.reduceExp(userId, amount);
	}

	@Override
	public boolean banUser(String userId, Date dueTime) {
		boolean success = this.userDaoMysqlImpl.banUser(userId, dueTime);
		if (success) {
			this.userDaoRedisImpl.banUser(userId, dueTime);
		}
		return success;
	}

	@Override
	public boolean reduceReputation(String userId, int reputation) {
		boolean success = this.userDaoMysqlImpl.reduceReputation(userId, reputation);
		if (success) {
			this.userDaoRedisImpl.reduceReputation(userId, reputation);
		}
		return success;
	}

	@Override
	public boolean addReputation(String userId, int reputation) {
		boolean success = this.userDaoMysqlImpl.addReputation(userId, reputation);
		if (success) {
			this.userDaoRedisImpl.addReputation(userId, reputation);
		}
		return success;
	}

	@Override
	public boolean updateUserLevel(String userId, int level, int exp) {
		boolean success = this.userDaoMysqlImpl.updateUserLevel(userId, level, exp);
		if (success) {
			this.userDaoRedisImpl.updateUserLevel(userId, level, exp);
		}
		return success;
	}

	@Override
	public UserShareLog getUserLastShareLog(String userId) {

		return this.userDaoMysqlImpl.getUserLastShareLog(userId);
	}

	@Override
	public boolean addUserShareLog(UserShareLog userShareLog) {
		return this.userDaoMysqlImpl.addUserShareLog(userShareLog);
	}

	@Override
	public boolean reduceMind(String userId, int mind) {
		boolean success = this.userDaoMysqlImpl.reduceMind(userId, mind);
		if (success) {
			this.userDaoRedisImpl.reduceMind(userId, mind);
		}
		return success;
	}

	@Override
	public boolean addMind(String userId, int mind) {
		boolean success = this.userDaoMysqlImpl.addMind(userId, mind);
		if (success) {
			this.userDaoRedisImpl.addMind(userId, mind);
		}
		return success;
	}
	
	@Override
	public boolean bannedToPost(String userId) {
		boolean success = this.userDaoMysqlImpl.bannedToPost(userId);
		if (success) {
			this.userDaoRedisImpl.bannedToPost(userId);
		}
		return success;
	}

	@Override
	public boolean updateUserFactionId(String userId, int factionId) {
		boolean success = this.userDaoMysqlImpl.updateUserFactionId(userId, factionId);
		if (success) {
			this.userDaoRedisImpl.updateUserFactionId(userId, factionId);
		}
		return success;
	}

	@Override
	public boolean addPkScore(String userId, int pkScore) {
		boolean success = this.userDaoMysqlImpl.addPkScore(userId, pkScore);
		if (success) {
			this.userDaoRedisImpl.addPkScore(userId, pkScore);
		}
		return success;
	}

	@Override
	public boolean reducePkScore(String userId, int pkScore) {
		boolean success = this.userDaoMysqlImpl.reducePkScore(userId, pkScore);
		if (success) {
			this.userDaoRedisImpl.reducePkScore(userId, pkScore);
		}
		return success;
	}

	@Override
	public boolean updateAllFactionByFactionId(int factionId,
			List<UserFaction> list) {
		boolean success = this.userDaoMysqlImpl.updateAllFactionByFactionId(factionId,list);
		if (success) {
			this.userDaoRedisImpl.updateAllFactionByFactionId(factionId,list);
		}
		return success;
	}

}
