package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.UserGiftbagDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserGiftbagDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserGiftbagDaoRedisImpl;
import com.lodogame.model.UserGiftbag;

public class UserGiftbagDaoCacheImpl implements UserGiftbagDao,ClearCacheOnLoginOut {

	private UserGiftbagDaoMysqlImpl userGiftbagDaoMysqlImpl;
	private UserGiftbagDaoRedisImpl userGiftbagDaoRedisImpl;
	@Override
	public UserGiftbag getLast(String userId, int type) {
		if(userGiftbagDaoRedisImpl.existUserId(userId)){
			return userGiftbagDaoRedisImpl.getLast(userId, type);
		}
		//初始化缓存
		List<UserGiftbag> list =userGiftbagDaoMysqlImpl.getAllUserGiftBagByUserId(userId);
		userGiftbagDaoRedisImpl.initUserGiftbag(userId, list);
		return userGiftbagDaoRedisImpl.getLast(userId, type);
	}

	@Override
	public boolean addOrUpdateUserGiftbag(String userId, int type, int giftBagId) {
		
		boolean result = userGiftbagDaoMysqlImpl.addOrUpdateUserGiftbag(userId, type, giftBagId);
		if(result){
			userGiftbagDaoRedisImpl.addOrUpdateUserGiftbag(userId, type, giftBagId);
		}
		return result;
	}
	
	@Override
	public int getCount(String userId, int type, int giftBagId) {
		int count = userGiftbagDaoRedisImpl.getCount(userId, type, giftBagId);
		if (count == -1) {
			count = userGiftbagDaoMysqlImpl.getCount(userId, type, giftBagId);

			// 初始化缓存
			List<UserGiftbag> list = userGiftbagDaoMysqlImpl.getAllUserGiftBagByUserId(userId);
			userGiftbagDaoRedisImpl.initUserGiftbag(userId, list);
		}

		return count;
	}

	public void setUserGiftbagDaoMysqlImpl(UserGiftbagDaoMysqlImpl userGiftbagDaoMysqlImpl) {
		this.userGiftbagDaoMysqlImpl = userGiftbagDaoMysqlImpl;
	}

	public void setUserGiftbagDaoRedisImpl(UserGiftbagDaoRedisImpl userGiftbagDaoRedisImpl) {
		this.userGiftbagDaoRedisImpl = userGiftbagDaoRedisImpl;
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		userGiftbagDaoRedisImpl.delEntry(userId);
	}

	
}
