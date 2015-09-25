package com.lodogame.game.dao;

import java.util.Map;

import com.lodogame.model.UserMallLog;

public interface UserMallLogDao {

	public boolean add(UserMallLog userMallLog);

	public Map<Integer,Integer> getUserTodayPurchaseNum(String userId);
	
	
}
