package com.lodogame.game.dao.impl.cache;

import java.util.HashMap;
import java.util.Map;

import com.lodogame.game.dao.UserEquipPolishTempDao;
import com.lodogame.model.UserEquipPolishTemp;

public class UserEquipPolishTempDaoCacheImpl implements UserEquipPolishTempDao {


	private Map<String, UserEquipPolishTemp> userEquipPolishTempMap = new HashMap<String, UserEquipPolishTemp>();
	
	private  UserEquipPolishTempDao userEquipPolishTempDaoMysqlImpl;

	public void setUserEquipPolishTempDaoMysqlImpl(
			UserEquipPolishTempDao userEquipPolishTempDaoMysqlImpl) {
		this.userEquipPolishTempDaoMysqlImpl = userEquipPolishTempDaoMysqlImpl;
	}

	@Override
	public UserEquipPolishTemp getUserEquipPolishTempById(String userEquipId) {
		UserEquipPolishTemp userEquipPolishTemp = null;
		if (!userEquipPolishTempMap.containsKey(userEquipId)){
			userEquipPolishTemp = this.userEquipPolishTempDaoMysqlImpl.getUserEquipPolishTempById(userEquipId);
			if (userEquipPolishTemp != null) {
				this.userEquipPolishTempMap.put(userEquipId, userEquipPolishTemp);
			}
		}
		return userEquipPolishTempMap.get(userEquipId);
	}

	@Override
	public boolean insertUserEquipPolishTemp(UserEquipPolishTemp userEquipPolishTemp) {
		return this.userEquipPolishTempDaoMysqlImpl.insertUserEquipPolishTemp(userEquipPolishTemp);
	}

	@Override
	public boolean updateUserEquipPollishTemp(String userId, String userEquipId,
			int attackRand, int lifeRand, int defenseRand) {
		userEquipPolishTempMap.remove(userEquipId);
		return this.userEquipPolishTempDaoMysqlImpl.updateUserEquipPollishTemp(userId, userEquipId, attackRand, lifeRand, defenseRand);
	}

}
