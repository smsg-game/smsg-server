package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.SystemMailDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.model.SystemMail;

public class SystemMailDaoCacheImpl implements ReloadAble, SystemMailDao {

	private SystemMailDao systemMailDaoMysqlImpl;

	private SystemMailDao systemMailDaoRedisImpl;

	public void setSystemMailDaoMysqlImpl(SystemMailDao systemMailDaoMysqlImpl) {
		this.systemMailDaoMysqlImpl = systemMailDaoMysqlImpl;
	}

	public void setSystemMailDaoRedisImpl(SystemMailDao systemMailDaoRedisImpl) {
		this.systemMailDaoRedisImpl = systemMailDaoRedisImpl;
	}

	@Override
	public List<SystemMail> getSystemMailByTime(Date date) {

		List<SystemMail> systemMailList = this.systemMailDaoRedisImpl.getSystemList();
		List<SystemMail> list = new ArrayList<SystemMail>();
		for (SystemMail systemMail : systemMailList) {

			if (date == null || systemMail.getCreatedTime().after(date)) {
				list.add(systemMail);
			}

		}

		return list;

	}

	@Override
	public List<SystemMail> getSystemList() {
		throw new NotImplementedException();
	}

	@Override
	public SystemMail get(String systemMailId) {
		Date startTime = DateUtils.addDays(new Date(), -15);
		SystemMail systemMail = this.systemMailDaoRedisImpl.get(systemMailId);
		if (systemMail != null && systemMail.getCreatedTime().after(startTime)) {
			return systemMail;
		}
		return null;
	}

	@Override
	public boolean add(SystemMail systemMail) {
		boolean success = this.systemMailDaoMysqlImpl.add(systemMail);
		if (success) {
			this.systemMailDaoRedisImpl.add(systemMail);
		}
		return success;
	}

	private void initCache() {

		List<SystemMail> systemMailList = this.systemMailDaoMysqlImpl.getSystemList();
		for (SystemMail systemMail : systemMailList) {
			this.systemMailDaoRedisImpl.add(systemMail);
		}

	}

	@Override
	public void reload() {
		initCache();
	}

	@Override
	public void init() {
		initCache();
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	@Override
	public int getMaxSourceId() {
		return this.systemMailDaoMysqlImpl.getMaxSourceId();
	}

}
