package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.UserExtinfoDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserExtinfoDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserExtinfoDaoRedisImpl;
import com.lodogame.model.UserExtinfo;

public class UserExtinfoDaoCacheImpl implements UserExtinfoDao, ClearCacheOnLoginOut {

	private UserExtinfoDaoRedisImpl userExtinfoDaoRedisImpl;

	private UserExtinfoDaoMysqlImpl userExtinfoDaoMysqlImpl;

	@Override
	public boolean add(UserExtinfo userExtinfo) {
		boolean result = userExtinfoDaoMysqlImpl.add(userExtinfo);
		if (result) {
			userExtinfoDaoRedisImpl.add(userExtinfo);
		}
		return result;
	}

	@Override
	public UserExtinfo get(String userId) {
		UserExtinfo result = userExtinfoDaoRedisImpl.get(userId);
		if (result != null) {
			return result;
		}
		result = userExtinfoDaoMysqlImpl.get(userId);
		if (result != null) {
			userExtinfoDaoRedisImpl.updateEntry(userId, result);
		}
		return result;
	}

	@Override
	public boolean updateBuyCopperTimes(String userId, int times) {
		boolean result = userExtinfoDaoMysqlImpl.updateBuyCopperTimes(userId, times);
		if (result) {
			userExtinfoDaoRedisImpl.updateBuyCopperTimes(userId, times);
		}
		return result;
	}

	@Override
	public boolean updateBuyPowerTimes(String userId, int times) {
		boolean result = userExtinfoDaoMysqlImpl.updateBuyPowerTimes(userId, times);
		if (result) {
			userExtinfoDaoRedisImpl.updateBuyPowerTimes(userId, times);
		}
		return result;
	}

	@Override
	public boolean updateHeroMax(String userId, int heroBag) {
		boolean result = userExtinfoDaoMysqlImpl.updateHeroMax(userId, heroBag);
		if (result) {
			userExtinfoDaoRedisImpl.updateHeroMax(userId, heroBag);
		}
		return result;
	}

	@Override
	public boolean updateEquipMax(String userId, int equipBag) {
		boolean result = userExtinfoDaoMysqlImpl.updateEquipMax(userId, equipBag);
		if (result) {
			userExtinfoDaoRedisImpl.updateEquipMax(userId, equipBag);
		}
		return result;
	}

	@Override
	public boolean updateGuideStep(String userId, int step) {
		boolean result = userExtinfoDaoMysqlImpl.updateGuideStep(userId, step);
		if (result) {
			userExtinfoDaoRedisImpl.updateGuideStep(userId, step);
		}
		return result;
	}

	@Override
	public boolean recordGuideStep(String userId, String newStep) {
		boolean result = userExtinfoDaoMysqlImpl.recordGuideStep(userId, newStep);
		if (result) {
			userExtinfoDaoRedisImpl.recordGuideStep(userId, newStep);
		}
		return result;
	}

	@Override
	public boolean updateFightRecode(String userId, boolean isWin) {
		boolean result = userExtinfoDaoMysqlImpl.updateFightRecode(userId, isWin);
		if (result) {
			userExtinfoDaoRedisImpl.updateFightRecode(userId, isWin);
		}
		return result;
	}

	public void setUserExtinfoDaoRedisImpl(UserExtinfoDaoRedisImpl userExtinfoDaoRedisImpl) {
		this.userExtinfoDaoRedisImpl = userExtinfoDaoRedisImpl;
	}

	public void setUserExtinfoDaoMysqlImpl(UserExtinfoDaoMysqlImpl userExtinfoDaoMysqlImpl) {
		this.userExtinfoDaoMysqlImpl = userExtinfoDaoMysqlImpl;
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		userExtinfoDaoRedisImpl.delEntry(userId);
	}

	@Override
	public boolean updateRewardVipLevel(String userId, int vipLevel) {
		boolean result = userExtinfoDaoMysqlImpl.updateRewardVipLevel(userId, vipLevel);
		if (result) {
			userExtinfoDaoRedisImpl.updateRewardVipLevel(userId, vipLevel);
		}
		return result;
	}

}
