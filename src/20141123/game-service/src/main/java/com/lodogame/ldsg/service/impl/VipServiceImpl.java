package com.lodogame.ldsg.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemVipLevelDao;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.model.SystemVipLevel;
import com.lodogame.model.User;

public class VipServiceImpl implements VipService {

	@Autowired
	private SystemVipLevelDao systemVipLevelDao;

	@Autowired
	private UserService userService;

	@Override
	public int getPkTimesLimit(String userId) {
		User user = this.userService.get(userId);
		SystemVipLevel systemVipLevel = this.systemVipLevelDao.get(user.getVipLevel());
		return systemVipLevel.getPkLimit();
	}

	@Override
	public int getPkTimesLimit(int vipLevel) {
		SystemVipLevel systemVipLevel = this.systemVipLevelDao.get(vipLevel);
		return systemVipLevel.getPkLimit();
	}

	@Override
	public int getResetForcesTimesLimit(int vipLevel) {
		SystemVipLevel systemVipLevel = this.systemVipLevelDao.get(vipLevel);
		return systemVipLevel.getResetForcesTimesLimit();
	}

	@Override
	public int getPowerMax(int vipLevel) {
		SystemVipLevel systemVipLevel = this.systemVipLevelDao.get(vipLevel);
		return systemVipLevel.getPowerMax();
	}

	@Override
	public double getExpAddRatio(int vipLevel) {
		SystemVipLevel systemVipLevel = this.systemVipLevelDao.get(vipLevel);
		return systemVipLevel.getUserExpAddRatio();
	}

	@Override
	public double getCopperAddRatio(int vipLevel) {
		SystemVipLevel systemVipLevel = this.systemVipLevelDao.get(vipLevel);
		return systemVipLevel.getForcesCopperAddRatio();
	}

	@Override
	public int getBuyPowerLimit(int vipLevel) {
		SystemVipLevel systemVipLevel = this.systemVipLevelDao.get(vipLevel);
		return systemVipLevel.getBuyPowerLimit();
	}

	@Override
	public int getResetTowerTimesLimit(int vipLevel) {
		SystemVipLevel systemVipLevel = this.systemVipLevelDao.get(vipLevel);
		return systemVipLevel.getResetTowerTimesLimit();
	}

}
