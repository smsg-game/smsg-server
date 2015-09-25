package com.lodogame.game.dao;

import com.lodogame.model.UserDeifyInfo;

public interface UserDeifyDao {

	public UserDeifyInfo getUserDeifyInfo(String uid);

	public boolean add(UserDeifyInfo userDeifyInfo);

}
