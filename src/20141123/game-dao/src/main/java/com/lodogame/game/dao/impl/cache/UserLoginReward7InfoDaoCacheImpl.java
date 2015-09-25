package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.UserLoginReward7InfoDao;
import com.lodogame.model.UserLoginReward7Info;

public class UserLoginReward7InfoDaoCacheImpl implements UserLoginReward7InfoDao{

	private UserLoginReward7InfoDao userLoginReward7InfoDaoMysqlImpl;
	
	@Override
	public boolean addUserLoginReward7Info(UserLoginReward7Info loginInfo) {
		return userLoginReward7InfoDaoMysqlImpl.addUserLoginReward7Info(loginInfo);
	}

	@Override
	public UserLoginReward7Info getLastLogin(String userId) {
		return userLoginReward7InfoDaoMysqlImpl.getLastLogin(userId);
	}

	@Override
	public List<UserLoginReward7Info> getLoginInfoList(String userId, int day) {
		return userLoginReward7InfoDaoMysqlImpl.getLoginInfoList(userId, day);
	}

	public void setUserLoginReward7InfoDaoMysqlImpl(
			UserLoginReward7InfoDao userLoginReward7InfoDaoMysqlImpl) {
		this.userLoginReward7InfoDaoMysqlImpl = userLoginReward7InfoDaoMysqlImpl;
	}
	
	
}
