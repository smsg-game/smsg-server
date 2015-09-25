package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemRecivePowerDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemRecivePower;

public class SystemRecivePowerDaoCacheImpl implements SystemRecivePowerDao, ReloadAble {

	private SystemRecivePowerDao systemRecivePowerDaoMysqlImpl;

	private Map<Integer, SystemRecivePower> map = new ConcurrentHashMap<Integer, SystemRecivePower>();

	public void setSystemRecivePowerDaoMysqlImpl(SystemRecivePowerDao systemRecivePowerDaoMysqlImpl) {
		this.systemRecivePowerDaoMysqlImpl = systemRecivePowerDaoMysqlImpl;
	}

	@Override
	public List<SystemRecivePower> getList() {
		List<SystemRecivePower> result = new ArrayList<SystemRecivePower>();
		result.addAll(map.values());
		Collections.sort(result, new Comparator<SystemRecivePower>() {
			@Override
			public int compare(SystemRecivePower o1, SystemRecivePower o2) {
				if (o1.getType() > o2.getType()) {
					return 1;
				} else if (o1.getType() < o2.getType()) {
					return -1;
				}
				return 0;
			}
		});
		return result;
	}

	@Override
	public SystemRecivePower get(int type) {
		return map.get(type);
	}

	@Override
	public void reload() {
		initCache();
	}

	private void initCache() {
		map.clear();
		List<SystemRecivePower> list = systemRecivePowerDaoMysqlImpl.getList();
		for (SystemRecivePower systemRecivePower : list) {
			map.put(systemRecivePower.getType(), systemRecivePower);
		}
	}

	@Override
	public void init() {
		initCache();
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
