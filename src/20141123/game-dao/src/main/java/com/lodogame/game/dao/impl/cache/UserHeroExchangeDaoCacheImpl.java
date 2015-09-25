package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.UserHeroExchangeDao;
import com.lodogame.game.dao.impl.mysql.UserHeroExchangeDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserHeroExchangeDaoRedisImpl;
import com.lodogame.model.UserHeroExchange;

public class UserHeroExchangeDaoCacheImpl implements UserHeroExchangeDao {

	private UserHeroExchangeDaoRedisImpl userHeroExchangeDaoRedisImpl;
	private UserHeroExchangeDaoMysqlImpl userHeroExchangeDaoMysqlImpl;
	@Override
	public UserHeroExchange get(String userId) {
		UserHeroExchange result = userHeroExchangeDaoRedisImpl.get(userId);
		if(result==null){
			result = userHeroExchangeDaoMysqlImpl.get(userId);
			if(result != null){
				userHeroExchangeDaoRedisImpl.add(result);
			}
		}
		return result;
	}

	@Override
	public boolean add(UserHeroExchange userHeroExchange) {
		if(userHeroExchangeDaoMysqlImpl.add(userHeroExchange)){
			userHeroExchangeDaoRedisImpl.add(userHeroExchange);
			return true;
		}
		return false;
	}

	@Override
	public boolean updateUserHeroExchange(String userId, int userWeek,
			int systemWeek, int times) {
		if(userHeroExchangeDaoMysqlImpl.updateUserHeroExchange(userId, userWeek, systemWeek, times)){
			userHeroExchangeDaoRedisImpl.updateUserHeroExchange(userId, userWeek, systemWeek, times);
		    return true;
		}
		return false;
	}

	public void setUserHeroExchangeDaoRedisImpl(UserHeroExchangeDaoRedisImpl userHeroExchangeDaoRedisImpl) {
		this.userHeroExchangeDaoRedisImpl = userHeroExchangeDaoRedisImpl;
	}

	public void setUserHeroExchangeDaoMysqlImpl(
			UserHeroExchangeDaoMysqlImpl userHeroExchangeDaoMysqlImpl) {
		this.userHeroExchangeDaoMysqlImpl = userHeroExchangeDaoMysqlImpl;
	}

}
