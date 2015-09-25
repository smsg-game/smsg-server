package com.lodogame.game.dao.impl.cache;

import java.util.HashMap;
import java.util.Map;

import com.lodogame.game.dao.UserEquipPolishDao;
import com.lodogame.model.UserEquipPolish;

public class UserEquipPolishDaoCacheImpl implements UserEquipPolishDao {


	private Map<String, UserEquipPolish> userEquipPolishMap = new HashMap<String, UserEquipPolish>();
	
	private  UserEquipPolishDao userEquipPolishDaoMysqlImpl;

	public void setUserEquipPolishDaoMysqlImpl(
			UserEquipPolishDao userEquipPolishDaoMysqlImpl) {
		this.userEquipPolishDaoMysqlImpl = userEquipPolishDaoMysqlImpl;
	}

	@Override
	public UserEquipPolish getUserEquipPolishById(String userEquipId) {
		UserEquipPolish userEquipPolish = null;
		if (!userEquipPolishMap.containsKey(userEquipId)){
			userEquipPolish = this.userEquipPolishDaoMysqlImpl.getUserEquipPolishById(userEquipId);
			if (userEquipPolish != null) {
				this.userEquipPolishMap.put(userEquipId, userEquipPolish);
			}
		}
		return userEquipPolishMap.get(userEquipId);
	}

	@Override
	public boolean insertUserEquipPolish(UserEquipPolish userEquipPolish) {
		return this.userEquipPolishDaoMysqlImpl.insertUserEquipPolish(userEquipPolish);
	}

	@Override
	public boolean updateUserEquipPollish(String userId, String userEquipId,
			int attackRand, int lifeRand, int defenseRand) {
		userEquipPolishMap.remove(userEquipId);
		return this.userEquipPolishDaoMysqlImpl.updateUserEquipPollish(userId, userEquipId, attackRand, lifeRand, defenseRand);
	}
	



}
