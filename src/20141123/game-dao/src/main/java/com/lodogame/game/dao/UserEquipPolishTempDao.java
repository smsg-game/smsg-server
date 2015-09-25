package com.lodogame.game.dao;


import com.lodogame.model.UserEquipPolishTemp;

public interface UserEquipPolishTempDao {

	public UserEquipPolishTemp getUserEquipPolishTempById(String userEquipId);
	
	public boolean insertUserEquipPolishTemp(UserEquipPolishTemp userEquipPolishTemp);
	
	public boolean updateUserEquipPollishTemp(String userId, String userEquipId, int attackRand, int lifeRand, int defenseRand);

}
