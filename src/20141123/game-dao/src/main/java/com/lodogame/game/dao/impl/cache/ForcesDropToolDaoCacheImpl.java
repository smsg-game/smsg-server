package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.ForcesDropToolDao;
import com.lodogame.game.dao.impl.mysql.ForcesDropToolDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.ForcesDropTool;

public class ForcesDropToolDaoCacheImpl implements ForcesDropToolDao, ReloadAble {

	private ForcesDropToolDaoMysqlImpl forcesDropToolDaoMysqlImpl;

	private Map<Integer, List<ForcesDropTool>> cacheMap = new ConcurrentHashMap<Integer, List<ForcesDropTool>>();

	@Override
	public List<ForcesDropTool> getForcesDropToolList(int forcesId) {

		if (cacheMap.containsKey(forcesId)) {
			return cacheMap.get(forcesId);
		}
		List<ForcesDropTool> list = forcesDropToolDaoMysqlImpl.getForcesDropToolList(forcesId);
		if (list != null) {
			cacheMap.put(forcesId, list);
		}
		return list;
	}

	@Override
	public void reload() {
		cacheMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	public void setForcesDropToolDaoMysqlImpl(ForcesDropToolDaoMysqlImpl forcesDropToolDaoMysqlImpl) {
		this.forcesDropToolDaoMysqlImpl = forcesDropToolDaoMysqlImpl;
	}

}
