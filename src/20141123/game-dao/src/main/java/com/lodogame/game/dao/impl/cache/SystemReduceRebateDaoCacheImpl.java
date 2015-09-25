package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemReduceRebateDao;
import com.lodogame.game.dao.impl.mysql.SystemReduceRebateDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemReduceRebate;

public class SystemReduceRebateDaoCacheImpl implements SystemReduceRebateDao,ReloadAble {

	private Map<Integer, List<SystemReduceRebate>> cacheList = new ConcurrentHashMap<Integer, List<SystemReduceRebate>>();
	private Map<Integer, SystemReduceRebate> cacheMap = new ConcurrentHashMap<Integer, SystemReduceRebate>();
	
	private SystemReduceRebateDaoMysqlImpl systemReduceRebateDaoMysqlImpl;
	
	public void setSystemReduceRebateDaoMysqlImpl(SystemReduceRebateDaoMysqlImpl systemReduceRebateDaoMysqlImpl) {
		this.systemReduceRebateDaoMysqlImpl = systemReduceRebateDaoMysqlImpl;
	}

	@Override
	public void reload() {
		cacheList.clear();
		cacheMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
		
	}

	@Override
	public SystemReduceRebate get(int id) {
		if(cacheMap.containsKey(id)){
			return cacheMap.get(id);
		}
		SystemReduceRebate systemReduceRebate = this.systemReduceRebateDaoMysqlImpl.get(id);
		cacheMap.put(id, systemReduceRebate);
		return systemReduceRebate;
	}

	@Override
	public List<SystemReduceRebate> getAllByType(int type) {
		if(cacheList.containsKey(type)){
			return cacheList.get(type);
		}
		List<SystemReduceRebate> list = this.systemReduceRebateDaoMysqlImpl.getAllByType(type);
		cacheList.put(type, list);
		return list;
	}

}
