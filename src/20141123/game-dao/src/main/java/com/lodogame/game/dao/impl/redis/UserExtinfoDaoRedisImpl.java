package com.lodogame.game.dao.impl.redis;

import java.util.Date;

import com.lodogame.game.dao.UserExtinfoDao;
import com.lodogame.game.dao.daobase.redis.RedisBase;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.model.UserExtinfo;

public class UserExtinfoDaoRedisImpl extends RedisBase<UserExtinfo> implements UserExtinfoDao {

	@Override
	public boolean add(UserExtinfo userExtinfo) {
		Date now = new Date();
		if (userExtinfo.getLastBuyPowerTime() == null) {
			userExtinfo.setLastBuyPowerTime(now);
		}
		if (userExtinfo.getLastBuyCopperTime() == null) {
			userExtinfo.setLastBuyCopperTime(now);
		}
		this.initEntry(userExtinfo.getUserId(), userExtinfo);
		return true;
	}

	@Override
	public UserExtinfo get(String userId) {
		return this.getEntry(userId);
	}

	@Override
	public boolean updateRewardVipLevel(String userId, int vipLevel) {
		UserExtinfo userExtInfo = get(userId);
		if (userExtInfo != null) {
			userExtInfo.setRewardVipLevel(vipLevel);
			this.updateEntry(userId, userExtInfo);
		}
		return true;
	}

	@Override
	public boolean updateBuyCopperTimes(String userId, int times) {
		UserExtinfo userExtInfo = get(userId);
		if (userExtInfo != null) {
			userExtInfo.setBuyCopperTimes(times);
			userExtInfo.setLastBuyCopperTime(new Date());
			this.updateEntry(userId, userExtInfo);
		}
		return true;
	}

	@Override
	public boolean updateBuyPowerTimes(String userId, int times) {
		UserExtinfo userExtInfo = get(userId);
		if (userExtInfo != null) {
			userExtInfo.setBuyPowerTimes(times);
			userExtInfo.setLastBuyPowerTime(new Date());
			this.updateEntry(userId, userExtInfo);
		}
		return true;
	}

	@Override
	public boolean updateHeroMax(String userId, int heroBag) {
		UserExtinfo userExtInfo = get(userId);
		if (userExtInfo != null) {
			userExtInfo.setHeroMax(userExtInfo.getHeroMax() + heroBag);
			this.updateEntry(userId, userExtInfo);
		}
		return true;
	}

	@Override
	public boolean updateEquipMax(String userId, int equipBag) {
		UserExtinfo userExtInfo = get(userId);
		if (userExtInfo != null) {
			userExtInfo.setEquipMax(userExtInfo.getEquipMax() + equipBag);
			this.updateEntry(userId, userExtInfo);
		}
		return true;
	}

	@Override
	public boolean updateGuideStep(String userId, int step) {
		UserExtinfo userExtInfo = get(userId);
		if (userExtInfo != null) {
			userExtInfo.setGuideStep(step);
			this.updateEntry(userId, userExtInfo);
		}
		return true;
	}

	@Override
	public boolean recordGuideStep(String userId, String newStep) {
		UserExtinfo userExtInfo = get(userId);
		if (userExtInfo != null) {
			userExtInfo.setRecordGuideStep(newStep);
			this.updateEntry(userId, userExtInfo);
		}
		return true;
	}

	@Override
	public boolean updateFightRecode(String userId, boolean isWin) {
		UserExtinfo userExtInfo = get(userId);
		if (userExtInfo != null) {
			if (!isWin) {
				userExtInfo.setLoseCount(userExtInfo.getLoseCount() + 1);
			} else {
				userExtInfo.setWinCount(userExtInfo.getWinCount() + 1);
			}
			this.updateEntry(userId, userExtInfo);
		}
		return true;
	}

	@Override
	public String getPreKey() {
		return RedisKey.getUserExtinfoCacheKeyPreFix();
	}

}
