package com.lodogame.game.dao.impl.redis;

import java.util.Date;

import com.lodogame.game.dao.UserHeroExchangeDao;
import com.lodogame.game.dao.daobase.redis.RedisBase;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.model.UserHeroExchange;

public class UserHeroExchangeDaoRedisImpl extends RedisBase<UserHeroExchange> implements
		UserHeroExchangeDao {

	@Override
	public UserHeroExchange get(String userId) {
		return super.getEntry(userId);
	}

	@Override
	public boolean add(UserHeroExchange userHeroExchange) {
		if(userHeroExchange==null){
			return false;
		}
		this.updateEntry(userHeroExchange.getUserId(), userHeroExchange);
		return true;
	}

	@Override
	public boolean updateUserHeroExchange(String userId, int userWeek,
			int systemWeek, int times) {
		UserHeroExchange t = this.get(userId);
        if(t!=null){
        	t.setUserWeek(userWeek);
        	t.setSystemWeek(systemWeek);
        	t.setTimes(times);
        	t.setUpdatedTime(new Date());
        	this.add(t);
        }
		return true;
	}

	@Override
	public String getPreKey() {
		// TODO Auto-generated method stub
		return RedisKey.getUserHeroExchangeKeyPre();
	}

}
