package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemToolDropDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemToolDrop;

public class SystemToolDropDaoCacheImpl implements SystemToolDropDao, ReloadAble {

	private Map<Integer, List<SystemToolDrop>> systemToolDropMap = new ConcurrentHashMap<Integer, List<SystemToolDrop>>();

	private SystemToolDropDao systemToolDropDaoMysqlImpl;

	public void setSystemToolDropDaoMysqlImpl(SystemToolDropDao systemToolDropDaoMysqlImpl) {
		this.systemToolDropDaoMysqlImpl = systemToolDropDaoMysqlImpl;
	}

	@Override
	public List<SystemToolDrop> getSystemToolDropList(int toolId) {

		List<SystemToolDrop> systemToolDropList = null;

		if (systemToolDropMap.containsKey(toolId)) {
			return this.systemToolDropMap.get(toolId);
		}

		systemToolDropList = this.systemToolDropDaoMysqlImpl.getSystemToolDropList(toolId);
		systemToolDropMap.put(toolId, systemToolDropList);

		return systemToolDropList;
	}

	@Override
	public void reload() {
		this.systemToolDropMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
