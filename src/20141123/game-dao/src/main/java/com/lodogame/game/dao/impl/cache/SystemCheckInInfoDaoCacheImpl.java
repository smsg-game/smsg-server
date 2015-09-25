package com.lodogame.game.dao.impl.cache;

import java.util.Date;

import com.lodogame.game.dao.SystemCheckInInfoDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemCheckInInfo;

public class SystemCheckInInfoDaoCacheImpl implements SystemCheckInInfoDao, ReloadAble {

	private SystemCheckInInfoDao systemCheckInInfoDaoMysqlImpl;

	private SystemCheckInInfo cache;

	public void setSystemCheckInInfoDaoMysqlImpl(SystemCheckInInfoDao systemCheckInInfoDaoMysqlImpl) {
		this.systemCheckInInfoDaoMysqlImpl = systemCheckInInfoDaoMysqlImpl;
	}

	@Override
	public SystemCheckInInfo getSystemCheckInInfo() {
		return cache;
	}

	@Override
	public boolean setSystemCheckInInfo(int groupId, Date finishTime) {
		boolean result = this.systemCheckInInfoDaoMysqlImpl.setSystemCheckInInfo(groupId, finishTime);
		if (result) {
			initCache();
		}
		return result;
	}

	@Override
	public void reload() {
		initCache();
	}

	private void initCache() {
		cache = this.systemCheckInInfoDaoMysqlImpl.getSystemCheckInInfo();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		initCache();
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}
}
