package com.lodogame.game.dao;


import com.lodogame.model.UserEquipPolish;

public interface UserEquipPolishDao {

	public UserEquipPolish getUserEquipPolishById(String userEquipId);
	
	public boolean insertUserEquipPolish(UserEquipPolish userEquipPolish);
	
	public boolean updateUserEquipPollish(String userId, String userEquipId, int attackRand, int lifeRand, int defenseRand);

}
