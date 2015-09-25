package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemActivityDao;
import com.lodogame.game.dao.impl.mysql.SystemActivityDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemActivity;

public class SystemActivityDaoCacheImpl implements SystemActivityDao, ReloadAble {

	private SystemActivityDao systemActivityDaoMysqlImpl;

	private Map<Integer, List<SystemActivity>> cacheMap = new ConcurrentHashMap<Integer, List<SystemActivity>>();

	private Map<Integer, SystemActivity> activityCache = new ConcurrentHashMap<Integer, SystemActivity>();

	/**
	 * 显示在活动面板的活动列表
	 */
	private List<SystemActivity> displayActivityLiset = null;

	@Override
	public void reload() {
		cacheMap.clear();
		activityCache.clear();
		displayActivityLiset.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	@Override
	public List<SystemActivity> getList(int activityType) {
		if (cacheMap.containsKey(activityType)) {
			return cacheMap.get(activityType);
		}
		List<SystemActivity> list = systemActivityDaoMysqlImpl.getList(activityType);
		if (list != null) {
			cacheMap.put(activityType, list);
		}
		return list;
	}

	@Override
	public SystemActivity get(int activityId) {
		if (activityCache.containsKey(activityId)) {
			return activityCache.get(activityId);
		}

		SystemActivity activity = systemActivityDaoMysqlImpl.get(activityId);
		if (activity != null) {
			activityCache.put(activityId, activity);
		}
		return activity;
	}

	public void setSystemActivityDaoMysqlImpl(SystemActivityDaoMysqlImpl systemActivityDaoMysqlImpl) {
		this.systemActivityDaoMysqlImpl = systemActivityDaoMysqlImpl;
	}

	@Override
	public List<SystemActivity> getDisplayActivityLiset() {

		if (displayActivityLiset == null || displayActivityLiset.isEmpty()) {
			displayActivityLiset = this.systemActivityDaoMysqlImpl.getDisplayActivityLiset();
		}

		return displayActivityLiset;

	}

	@Override
	public boolean addActivity(SystemActivity systemActivity) {
		return this.systemActivityDaoMysqlImpl.addActivity(systemActivity);

	}

	@Override
	public boolean modifyActivity(SystemActivity systemActivity) {
		return this.systemActivityDaoMysqlImpl.modifyActivity(systemActivity);

	}

	@Override
	public boolean execute(String sql) {
		return false;
		//return this.systemActivityDaoMysqlImpl.execute(sql);
	}

}
