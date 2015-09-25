package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

import com.lodogame.game.dao.UserEquipDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.UserEquip;

public class UserEquipDaoCacheImpl implements UserEquipDao, ReloadAble {

	UserEquipDao userEquipDaoMysqlImpl;

	UserEquipDao userEquipDaoRedisImpl;

	public void setUserEquipDaoMysqlImpl(UserEquipDao userEquipDaoMysqlImpl) {
		this.userEquipDaoMysqlImpl = userEquipDaoMysqlImpl;
	}

	public void setUserEquipDaoRedisImpl(UserEquipDao userEquipDaoRedisImpl) {
		this.userEquipDaoRedisImpl = userEquipDaoRedisImpl;
	}

	@Override
	public boolean add(UserEquip userEquip) {
		boolean success = this.userEquipDaoMysqlImpl.add(userEquip);
		if (success) {
			// UserEquip ue = this.get(userEquip.getUserId(),
			// userEquip.getUserEquipId());
			this.userEquipDaoRedisImpl.add(userEquip);
		}
		return success;
	}

	@Override
	public List<UserEquip> getUserEquipList(String userId) {
		List<UserEquip> userEquipList = this.userEquipDaoRedisImpl.getUserEquipList(userId);
		if (userEquipList != null) {
			return userEquipList;
		}

		userEquipList = this.userEquipDaoMysqlImpl.getUserEquipList(userId);
		if (!userEquipList.isEmpty()) {
			this.userEquipDaoRedisImpl.setUserEquipList(userId, userEquipList);
		}

		return userEquipList;

	}

	@Override
	public List<UserEquip> getHeroEquipList(String userId, String userHeroId) {
		List<UserEquip> userEquipList = this.getUserEquipList(userId);
		List<UserEquip> list = new ArrayList<UserEquip>();
		for (UserEquip userEquip : userEquipList) {
			if (StringUtils.equalsIgnoreCase(userEquip.getUserHeroId(), userHeroId)) {
				list.add(userEquip);
			}
		}
		return list;
	}

	@Override
	public boolean updateEquipHero(String userId, String userEquipId, String userHeroId) {
		boolean success = this.userEquipDaoMysqlImpl.updateEquipHero(userId, userEquipId, userHeroId);
		if (success) {
			UserEquip userEquip = this.userEquipDaoMysqlImpl.get(userId, userEquipId);
			this.userEquipDaoRedisImpl.add(userEquip);
		}
		return true;
	}

	@Override
	public boolean updateEquipLevel(String userId, String userEquipId, int addLevel, int maxLevel) {
		boolean success = this.userEquipDaoMysqlImpl.updateEquipLevel(userId, userEquipId, addLevel, maxLevel);
		if (success) {
			UserEquip userEquip = this.userEquipDaoMysqlImpl.get(userId, userEquipId);
			this.userEquipDaoRedisImpl.add(userEquip);
		}
		return true;
	}

	@Override
	public boolean delete(String userId, String userEquipId) {
		boolean success = this.userEquipDaoMysqlImpl.delete(userId, userEquipId);
		if (success) {
			this.userEquipDaoRedisImpl.delete(userId, userEquipId);
		}
		return success;
	}

	@Override
	public boolean delete(String userId, List<String> userEquipIdList) {
		boolean success = this.userEquipDaoMysqlImpl.delete(userId, userEquipIdList);
		if (success) {
			this.userEquipDaoRedisImpl.delete(userId, userEquipIdList);
		}
		return success;
	}

	@Override
	public boolean updateEquipId(String userId, String userEquipId, int equipId) {
		boolean success = this.userEquipDaoMysqlImpl.updateEquipId(userId, userEquipId, equipId);
		if (success) {
			UserEquip userEquip = this.userEquipDaoMysqlImpl.get(userId, userEquipId);
			this.userEquipDaoRedisImpl.add(userEquip);
		}
		return success;
	}

	@Override
	public UserEquip get(String userId, String userEquipId) {
		UserEquip userEquip = this.userEquipDaoRedisImpl.get(userId, userEquipId);
		if (userEquip == null) {
			this.getUserEquipList(userId);
		}
		return this.userEquipDaoRedisImpl.get(userId, userEquipId);
	}

	@Override
	public void setUserEquipList(String userId, List<UserEquip> userEquipList) {
		throw new NotImplementedException();
	}

	@Override
	public int getUserEquipCount(String userId) {
		int count = this.userEquipDaoRedisImpl.getUserEquipCount(userId);
		if (count == 0) {
			List<UserEquip> userEquipList = this.getUserEquipList(userId);
			return userEquipList.size();
		}
		return count;
	}

	@Override
	public boolean addEquips(List<UserEquip> userEquipList) {
		boolean success = this.userEquipDaoMysqlImpl.addEquips(userEquipList);
		if (success) {
			// UserHero u = this.userHeroDaoMysqlImpl.get(userHero.getUserId(),
			// userHero.getUserHeroId());
			this.userEquipDaoRedisImpl.addEquips(userEquipList);
		}
		return success;
	}

	@Override
	public void reload() {

	}

	@Override
	public void init() {

	}

	@Override
	public List<UserEquip> getUserEquipList(String userId, int equipId) {
		return this.userEquipDaoMysqlImpl.getUserEquipList(userId, equipId);
	}

	@Override
	public List<UserEquip> listUserEquipByLevelAsc(String userId, int equipId) {
		return this.userEquipDaoMysqlImpl.listUserEquipByLevelAsc(userId, equipId);
	}

	@Override
	public boolean updateUserDeify(String userId, int equipId, int equipLevel, String userEquipId) {
		
		boolean success = this.userEquipDaoMysqlImpl.updateUserDeify(userId, equipId, equipLevel, userEquipId);
		if (success) {
			UserEquip userEquip = this.userEquipDaoMysqlImpl.get(userId, userEquipId);
			this.userEquipDaoRedisImpl.add(userEquip);
		}

		return success;
	}

}
