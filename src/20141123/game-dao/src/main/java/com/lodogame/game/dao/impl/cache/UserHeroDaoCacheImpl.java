package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.UserHeroDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.UserHero;

public class UserHeroDaoCacheImpl implements UserHeroDao, ReloadAble {

	private UserHeroDao userHeroDaoMysqlImpl;

	private UserHeroDao userHeroDaoRedisImpl;

	@Override
	public int getUserHeroCount(String userId) {

		int count = this.userHeroDaoRedisImpl.getUserHeroCount(userId);
		if (count == 0) {
			List<UserHero> userHeroList = this.getUserHeroList(userId);
			return userHeroList.size();
		}
		return count;
	}

	public void setUserHeroDaoMysqlImpl(UserHeroDao userHeroDaoMysqlImpl) {
		this.userHeroDaoMysqlImpl = userHeroDaoMysqlImpl;
	}

	public void setUserHeroDaoRedisImpl(UserHeroDao userHeroDaoRedisImpl) {
		this.userHeroDaoRedisImpl = userHeroDaoRedisImpl;
	}

	@Override
	public List<UserHero> getUserHeroList(String userId) {
		List<UserHero> userHeroList = this.userHeroDaoRedisImpl.getUserHeroList(userId);
		if (userHeroList != null) {
			return userHeroList;
		}
		userHeroList = this.userHeroDaoMysqlImpl.getUserHeroList(userId);
		if (!userHeroList.isEmpty()) {
			this.userHeroDaoRedisImpl.setUserHeroList(userId, userHeroList);
		}

		return userHeroList;
	}

	@Override
	public void setUserHeroList(String userId, List<UserHero> userHeroList) {
		throw new NotImplementedException();
	}

	@Override
	public List<UserHero> getUserHeroList(String userId, int systemHeroId) {
		List<UserHero> userHeroList = this.getUserHeroList(userId);
		List<UserHero> list = new ArrayList<UserHero>();
		for (UserHero userHero : userHeroList) {
			if (userHero.getSystemHeroId() != systemHeroId) {
				continue;
			}
			list.add(userHero);
		}
		return list;
	}

	@Override
	public UserHero get(String userId, String userHeroId) {
		UserHero userHero = this.userHeroDaoRedisImpl.get(userId, userHeroId);
		if (userHero == null) {
			this.getUserHeroList(userId);
		}
		return this.userHeroDaoRedisImpl.get(userId, userHeroId);
	}

	@Override
	public UserHero getUserHeroByPos(String userId, int pos) {
		List<UserHero> userHeroList = this.getUserHeroList(userId);
		for (UserHero userHero : userHeroList) {
			if (userHero.getPos() == pos) {
				return userHero;
			}
		}
		return null;
	}

	@Override
	public boolean addUserHero(UserHero userHero) {
		boolean success = this.userHeroDaoMysqlImpl.addUserHero(userHero);
		if (success) {
			UserHero u = this.userHeroDaoMysqlImpl.get(userHero.getUserId(), userHero.getUserHeroId());
			this.userHeroDaoRedisImpl.addUserHero(u);
		}
		return success;
	}

	@Override
	public boolean addUserHero(List<UserHero> userHeroList) {
		boolean success = this.userHeroDaoMysqlImpl.addUserHero(userHeroList);
		if (success) {
			// UserHero u = this.userHeroDaoMysqlImpl.get(userHero.getUserId(),
			// userHero.getUserHeroId());
			this.userHeroDaoRedisImpl.addUserHero(userHeroList);
		}
		return success;
	}

	@Override
	public boolean changePos(String userId, String userHeroId, int pos) {
		boolean success = this.userHeroDaoMysqlImpl.changePos(userId, userHeroId, pos);
		if (success) {
			UserHero userHero = this.userHeroDaoMysqlImpl.get(userId, userHeroId);
			this.userHeroDaoRedisImpl.addUserHero(userHero);
		}
		return success;
	}

	@Override
	public boolean changeSystemHeroId(String userId, String userHeroId, int systemHeroId) {
		boolean success = this.userHeroDaoMysqlImpl.changeSystemHeroId(userId, userHeroId, systemHeroId);
		if (success) {
			UserHero userHero = this.userHeroDaoMysqlImpl.get(userId, userHeroId);
			this.userHeroDaoRedisImpl.addUserHero(userHero);
		}
		return success;
	}

	@Override
	public boolean delete(String userId, String userHeroId) {
		boolean success = this.userHeroDaoMysqlImpl.delete(userId, userHeroId);
		if (success) {
			this.userHeroDaoRedisImpl.delete(userId, userHeroId);
		}
		return success;
	}

	@Override
	public boolean update(String userId, String userHeroId, int systemHeroId, int level, int exp) {
		boolean success = this.userHeroDaoMysqlImpl.update(userId, userHeroId, systemHeroId, level, exp);
		if (success) {
			UserHero userHero = this.userHeroDaoMysqlImpl.get(userId, userHeroId);
			this.userHeroDaoRedisImpl.addUserHero(userHero);
		}
		return success;
	}

	@Override
	public int delete(String userId, List<String> userHeroIdList) {
		int effectCount = this.userHeroDaoMysqlImpl.delete(userId, userHeroIdList);
		if (effectCount > 0) {
			this.userHeroDaoRedisImpl.delete(userId, userHeroIdList);
		}
		return effectCount;
	}

	@Override
	public boolean updateExpLevel(String userId, String userHeroId, int exp, int level) {
		boolean success = this.userHeroDaoMysqlImpl.updateExpLevel(userId, userHeroId, exp, level);
		if (success) {
			UserHero userHero = this.userHeroDaoMysqlImpl.get(userId, userHeroId);
			this.userHeroDaoRedisImpl.addUserHero(userHero);
		}
		return success;
	}

	@Override
	public int getBattleHeroCount(String userId) {
		List<UserHero> userHeroList = this.getUserHeroList(userId);
		int count = 0;
		for (UserHero userHero : userHeroList) {
			if (userHero.getPos() > 0) {
				count += 1;
			}
		}
		return count;
	}

	@Override
	public List<UserHero> listByHeroLevelDesc(int offset, int size) {
		return userHeroDaoMysqlImpl.listByHeroLevelDesc(offset, size);
	}

	@Override
	public void reload() {

	}

	@Override
	public void init() {

	}

	@Override
	public List<UserHero> listUserHeroByLevelAsc(String userId, int systemHeroId) {
		return this.userHeroDaoMysqlImpl.listUserHeroByLevelAsc(userId, systemHeroId);
	}

	@Override
	public boolean upgradeStage(String userId, String userHeroId) {
		boolean success =  this.userHeroDaoMysqlImpl.upgradeStage(userId, userHeroId);
		if (success) {
			UserHero userHero = this.userHeroDaoMysqlImpl.get(userId, userHeroId);
			this.userHeroDaoRedisImpl.addUserHero(userHero);
		}
		return success;
	}

	@Override
	public boolean lockHero(String userId, String userHeroId) {
		boolean success =  this.userHeroDaoMysqlImpl.lockHero(userId, userHeroId);
		if (success) {
			UserHero userHero = this.userHeroDaoMysqlImpl.get(userId, userHeroId);
			this.userHeroDaoRedisImpl.addUserHero(userHero);
		}
		return success;
	}

	@Override
	public boolean unlockHero(String userId, String userHeroId) {
		boolean success =  this.userHeroDaoMysqlImpl.unlockHero(userId, userHeroId);
		if (success) {
			UserHero userHero = this.userHeroDaoMysqlImpl.get(userId, userHeroId);
			this.userHeroDaoRedisImpl.addUserHero(userHero);
		}
		return success;
	}

	@Override
	public boolean upgradeDeifyNodeLevel(String userId, String userHeroId,
			int deifyNodeLevel) {
		boolean success =  this.userHeroDaoMysqlImpl.upgradeDeifyNodeLevel(userId, userHeroId, deifyNodeLevel);
		if (success) {
			UserHero userHero = this.userHeroDaoMysqlImpl.get(userId, userHeroId);
			this.userHeroDaoRedisImpl.addUserHero(userHero);
		}
		return success;
	}

	@Override
	public List<UserHero> getDeifyPeopleList(String userId) {
		List<UserHero> userHeroList = this.userHeroDaoRedisImpl.getUserHeroList(userId);
		if (userHeroList != null) {
			return userHeroList;
		}
		userHeroList = this.userHeroDaoMysqlImpl.getDeifyPeopleList(userId);
		if (!userHeroList.isEmpty()) {
			this.userHeroDaoRedisImpl.setUserHeroList(userId, userHeroList);
		}

		return userHeroList;
	}
}
